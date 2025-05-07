/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.HeavyWeightWindowFixture;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.fixtures.JListFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.intellij.remoterobot.utils.UtilsKt;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.errors.IdeFatalErrorsDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings.SettingsDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings.pages.NotificationsPage;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ProjectLocation;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.steps.SharedSteps;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Welcome to IntelliJ IDEA dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "FlatWelcomeFrame type", xpath = XPathDefinitions.FLAT_WELCOME_FRAME)
@FixtureName(name = "Welcome To IntelliJ IDEA Dialog")
public class FlatWelcomeFrame extends CommonContainerFixture {
    private static final Logger LOGGER = Logger.getLogger(FlatWelcomeFrame.class.getName());
    private static final String PROJECTS_BUTTON = "Projects";
    private static final String TIP_OF_THE_DAY = "Tip of the Day";
    private final RemoteRobot remoteRobot;
    private final int ideaVersionInt = UITestRunner.getIdeaVersionInt();

    public FlatWelcomeFrame(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Click on the 'New Project' link
     */
    public void createNewProject() {
        clickOnLink("New Project");
    }

    /**
     * CLick on existing project from the Welcome Dialog
     *
     * @param projectName name of existing project
     */
    public void openProject(String projectName) {
        JTreeFixture existingProjectFixture = find(JTreeFixture.class, byXpath("//div[contains(@visible_text, '" + projectName + "')]"), Duration.ofSeconds(2));
        existingProjectFixture.clickRow(0);
    }

    /**
     * Click on the link according to given label
     *
     * @param label label of the link to click on
     */
    public void clickOnLink(String label) {
        // Code for IntelliJ IDEA 2020.3 or newer
        if (ideaVersionInt >= 20203) {
            welcomeFrameLink(label).click();
        }
        // Code for IntelliJ IDEA 2020.2 or earlier
        else {
            actionLink(label).click();
        }
    }

    /**
     * Clear the workspace by deleting the content of the IdeaProjects folder and clearing all the projects' links in the 'Welcome to IntelliJ IDEA' dialog
     */
    public void clearWorkspace() {
        // Remove projects on disk
        try {
            String pathToDirToMakeEmpty = ProjectLocation.PROJECT_LOCATION;
            Path path = Paths.get(pathToDirToMakeEmpty);
            boolean doesProjectDirExists = Files.exists(path);
            if (doesProjectDirExists) {
                try (Stream<Path> pathStream = Files.walk(new File(pathToDirToMakeEmpty).toPath())) {
                    pathStream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                }
            } else {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        // Remove projects from FlatWelcomeFrame's recent projects
        for (int i = 0; i < projectsCount(); i++) {
            removeTopProjectFromRecentProjects();
        }
    }

    /**
     * Clear all exceptions
     */
    public void clearExceptions() {
        try {
            ideErrorsIcon().click();
            find(IdeFatalErrorsDialog.class, Duration.ofSeconds(10)).clearAll();
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.INFO, "No fatal errors dialog found to clear.");
            try {
                find(IdeFatalErrorsDialog.class, Duration.ofSeconds(10)).clearAll();
            } catch (Exception e2) {
                LOGGER.log(Level.INFO, "Second attempt to clear fatal errors dialog also failed.");
            }
        }
    }

    /**
     * Open the 'Preferences' dialog
     */
    public void openSettingsDialog() {
        if (ideaVersionInt <= 20202) {
            clickOnLink("Configure");
            HeavyWeightWindowFixture heavyWeightWindowFixture = find(HeavyWeightWindowFixture.class, Duration.ofSeconds(5));
            heavyWeightWindowFixture.findText("Preferences").click();
        } else if (ideaVersionInt <= 20212) {
            JListFixture jListFixture = remoteRobot.find(JListFixture.class, byXpath(XPathDefinitions.JBLIST));
            jListFixture.clickItem("Customize", false);
            remoteRobot.find(ContainerFixture.class, byXpath(XPathDefinitions.DIALOG_PANEL)).findText("All settings" + '\u2026').click();
        } else {
            JTreeFixture jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(XPathDefinitions.TREE));
            jTreeFixture.findText("Customize").click();
            if (remoteRobot.isMac()) {
                resizeWelcomeWindow();
            }
            remoteRobot.find(ContainerFixture.class, byXpath(XPathDefinitions.DIALOG_PANEL)).findText("All settings" + '\u2026').click();
        }
    }

    /**
     * Resize the Welcome to IntelliJ IDEA window
     */
    private void resizeWelcomeWindow() {
        try {
            remoteRobot.callJs(String.format("""
                    importClass(java.awt.Frame);
                    importClass(javax.swing.SwingUtilities);
                    var frames = Frame.getFrames();
                    var resized = false;
                    for (var i = 0; i < frames.length; i++) {
                        var frame = frames[i];
                        if (frame.isShowing() && frame.getClass().getName().contains("FlatWelcomeFrame")) {
                            SwingUtilities.invokeLater(function() {
                                frame.setSize(frame.getWidth(), %d);
                                frame.validate();
                            });
                            resized = true;
                            break;
                        }
                    }
                    resized;
                """, 900));
            Thread.sleep(5000);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to resize the Welcome window: {0}", e.getMessage());
            /* Clean up whatever needs to be handled before interrupting  */
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Open the 'Tip Of the Day' dialog
     *
     * @return fixture for the 'Tip Of the Day' dialog
     */
    public TipDialog openTipDialog() {
        if (ideaVersionInt >= 20211) {
            FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(2));
            if (ideaVersionInt >= 20223) {         //  COMMUNITY_V_2022_3 and higher version have different labels for Learn button
                flatWelcomeFrame.findText(ButtonLabels.LEARN_LABEL).click();
            } else {
                flatWelcomeFrame.findText(ButtonLabels.LEARN_INTELLIJ_IDEA_LABEL).click();
            }
            SharedSteps.waitForComponentByXpath(remoteRobot, 2, 200, byXpath(XPathDefinitions.TIP_DIALOG_2));
            flatWelcomeFrame.findText(TIP_OF_THE_DAY).click();
        } else if (ideaVersionInt <= 20202) {
            clickOnLink("Get Help");
            HeavyWeightWindowFixture heavyWeightWindowFixture = find(HeavyWeightWindowFixture.class, Duration.ofSeconds(5));
            heavyWeightWindowFixture.findText(TIP_OF_THE_DAY).click();
        } else if (ideaVersionInt == 20203) {          // IJ 2020.3
            actionLink("Help").click();
            HeavyWeightWindowFixture heavyWeightWindowFixture = find(HeavyWeightWindowFixture.class, Duration.ofSeconds(5));
            heavyWeightWindowFixture.findText(TIP_OF_THE_DAY).click();
        }

        return remoteRobot.find(TipDialog.class, Duration.ofSeconds(10));
    }

    /**
     * Open the 'Preferences' dialog
     */
    public void disableNotifications() {
        openSettingsDialog();
        SettingsDialog settingsDialog = remoteRobot.find(SettingsDialog.class, Duration.ofSeconds(5));
        settingsDialog.navigateTo("Appearance & Behavior", "Notifications");
        NotificationsPage notificationsPage = remoteRobot.find(NotificationsPage.class, Duration.ofSeconds(5));
        notificationsPage.toggleNotifications(false);
        settingsDialog.ok();
        switchToProjectsPage();
    }

    /**
     * Prevent the 'Tip of the Day' dialog from opening after project import
     */
    public void preventTipDialogFromOpening() {
        TipDialog tipDialog = openTipDialog();
        tipDialog.dontShowTipsCheckBox().setValue(true);
        tipDialog.close();
        switchToProjectsPage();
    }

    /**
     * Switch to the 'Projects' page of flat welcome frame
     */
    public void switchToProjectsPage() {
        if (ideaVersionInt >= 20213) {
            JTreeFixture jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(XPathDefinitions.TREE));
            jTreeFixture.findText(PROJECTS_BUTTON).click();
        } else if (ideaVersionInt >= 20203) {
            JListFixture jListFixture = remoteRobot.find(JListFixture.class, byXpath(XPathDefinitions.JBLIST));
            jListFixture.clickItem(PROJECTS_BUTTON, false);
        }
    }

    private int projectsCount() {
        if (ideaVersionInt >= 20222) {
            try {
                JTreeFixture projects = remoteRobot.findAll(JTreeFixture.class, byXpath(XPathDefinitions.RECENT_PROJECT_PANEL_NEW_2)).get(0);
                return projects.findAllText().size() / 2;
            } catch (IndexOutOfBoundsException e) {
                return 0;
            }
        } else {
            try {
                ContainerFixture projectWrapper = find(ContainerFixture.class, byXpath(XPathDefinitions.RECENT_PROJECT_PANEL_NEW));
                JListFixture projectList = projectWrapper.find(JListFixture.class, byXpath(XPathDefinitions.MY_LIST));
                return projectList.collectItems().size();
            } catch (WaitForConditionTimeoutException e) {
                return 0;
            }
        }
    }

    // Works for IntelliJ Idea 2020.3+
    private JButtonFixture welcomeFrameLink(String label) {
        if (UtilsKt.hasAnyComponent(this, byXpath(XPathDefinitions.RECENT_PROJECT_PANEL_NEW))) {
            return button(byXpath(XPathDefinitions.jBOptionButton(label)), Duration.ofSeconds(2));
        }
        if (ideaVersionInt >= 20241 && label.equals("New Project")) {
            return button(byXpath(XPathDefinitions.CREATE_NEW_PROJECT), Duration.ofSeconds(2));
        }
        return button(byXpath(XPathDefinitions.nonOpaquePanel(label)), Duration.ofSeconds(2));
    }

    private ComponentFixture ideErrorsIcon() {
        return find(ComponentFixture.class, byXpath(XPathDefinitions.IDE_ERROR_ICON), Duration.ofSeconds(10));
    }

    private void removeTopProjectFromRecentProjects() {
        ComponentFixture recentProjects;
        if (ideaVersionInt >= 20222) {
            recentProjects = remoteRobot.findAll(JTreeFixture.class, byXpath(XPathDefinitions.RECENT_PROJECT_PANEL_NEW_2)).get(0);
        } else {
            recentProjects = jLists(byXpath(XPathDefinitions.RECENT_PROJECTS)).get(0);
        }

        // Clicks on X on first recent project to remove it from the recent projects list (visible only when hovered over with cursor)
        recentProjects.runJs("const horizontal_offset = component.getWidth()-22;\n" +
            "robot.click(component, new Point(horizontal_offset, 22), MouseButton.LEFT_BUTTON, 1);");

        if (ideaVersionInt >= 20231) {
            ComponentFixture removeDialog = remoteRobot.find(ComponentFixture.class, byXpath(XPathDefinitions.MY_DIALOG), Duration.ofSeconds(10));
            removeDialog.findText(ButtonLabels.REMOVE_FROM_LIST_LABEL).click();
        } else if (ideaVersionInt >= 20203) {         // Code for IntelliJ Idea 2020.3 or newer
            List<JPopupMenuFixture> jPopupMenuFixtures = jPopupMenus(JPopupMenuFixture.Companion.byType());
            if (!jPopupMenuFixtures.isEmpty()) {
                JPopupMenuFixture contextMenu = jPopupMenuFixtures.get(0);
                if (ideaVersionInt >= 20222) {
                    contextMenu.select("Remove from Recent Projects" + '\u2026');
                    button(byXpath(XPathDefinitions.REMOVE_PROJECT_BUTTON)).click();
                } else {
                    contextMenu.select("Remove from Recent Projects");
                }
            }
        }
    }
}
