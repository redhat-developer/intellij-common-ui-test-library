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
import com.intellij.remoterobot.fixtures.JButtonFixture;
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
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
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
        JTreeFixture existingProjectFixture = find(JTreeFixture.class, byXpath("//div[contains(@visible_text, '" + projectName + "')]"), UITestTimeouts.QUICK_TIMEOUT);
        existingProjectFixture.clickRow(0);
    }

    /**
     * Click on the link according to given label
     *
     * @param label label of the link to click on
     */
    public void clickOnLink(String label) {
        welcomeFrameLink(label).click();
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
            find(IdeFatalErrorsDialog.class, UITestTimeouts.SHORT_TIMEOUT).clearAll();
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.INFO, "No fatal errors dialog found to clear.");
            try {
                find(IdeFatalErrorsDialog.class, UITestTimeouts.SHORT_TIMEOUT).clearAll();
            } catch (Exception e2) {
                LOGGER.log(Level.INFO, "Second attempt to clear fatal errors dialog also failed.");
            }
        }
    }

    /**
     * Open the 'Preferences' dialog
     */
    public void openSettingsDialog() {
        JTreeFixture jTreeFixture;
        try {
            jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(XPathDefinitions.TREE));
        } catch (WaitForConditionTimeoutException e) {
            // workaround for 2022.3
            jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(XPathDefinitions.TREE_FOR_20223));
        }
        jTreeFixture.findText("Customize").click();
        if (remoteRobot.isMac()) {
            resizeWelcomeWindow();
        }
        remoteRobot.find(ContainerFixture.class, byXpath(XPathDefinitions.DIALOG_PANEL)).findText("All settings" + '\u2026').click();
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
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, UITestTimeouts.QUICK_TIMEOUT);
        flatWelcomeFrame.findText(ButtonLabels.LEARN_LABEL).click();
        SharedSteps.waitForComponentByXpath(remoteRobot, 2, 200, byXpath(XPathDefinitions.TIP_DIALOG_2));
        flatWelcomeFrame.findText(TIP_OF_THE_DAY).click();
        return remoteRobot.find(TipDialog.class, UITestTimeouts.FIXTURE_TIMEOUT);
    }

    /**
     * Open the 'Preferences' dialog
     */
    public void disableNotifications() {
        openSettingsDialog();
        SettingsDialog settingsDialog = remoteRobot.find(SettingsDialog.class, UITestTimeouts.SHORT_TIMEOUT);
        settingsDialog.navigateTo("Appearance & Behavior", "Notifications");
        NotificationsPage notificationsPage = remoteRobot.find(NotificationsPage.class, UITestTimeouts.SHORT_TIMEOUT);
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
        JTreeFixture jTreeFixture;
        try {
            jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(XPathDefinitions.TREE));
        } catch (WaitForConditionTimeoutException e) {
            // workaround for 2022.3
            jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(XPathDefinitions.TREE_FOR_20223));
        }
        jTreeFixture.findText(PROJECTS_BUTTON).click();
    }

    private int projectsCount() {
        try {
            JTreeFixture projects = remoteRobot.findAll(JTreeFixture.class, byXpath(XPathDefinitions.RECENT_PROJECT_PANEL_NEW_2)).get(0);
            return projects.findAllText().size() / 2;
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    // Works for IntelliJ Idea 2020.3+
    private JButtonFixture welcomeFrameLink(String label) {
        if (UtilsKt.hasAnyComponent(this, byXpath(XPathDefinitions.RECENT_PROJECT_PANEL_NEW))) {
            return button(byXpath(XPathDefinitions.jBOptionButton(label)), UITestTimeouts.UI_ELEMENT_TIMEOUT);
        }
        if (ideaVersionInt >= 20241 && label.equals("New Project")) {
            return button(byXpath(XPathDefinitions.CREATE_NEW_PROJECT), UITestTimeouts.UI_ELEMENT_TIMEOUT);
        }
        return button(byXpath(XPathDefinitions.nonOpaquePanel(label)), UITestTimeouts.UI_ELEMENT_TIMEOUT);
    }

    private ComponentFixture ideErrorsIcon() {
        return find(ComponentFixture.class, byXpath(XPathDefinitions.IDE_ERROR_ICON), UITestTimeouts.SHORT_TIMEOUT);
    }

    private void removeTopProjectFromRecentProjects() {
        ComponentFixture recentProjects = remoteRobot.findAll(JTreeFixture.class, byXpath(XPathDefinitions.RECENT_PROJECT_PANEL_NEW_2)).get(0);

        // Clicks on X on first recent project to remove it from the recent projects list (visible only when hovered over with cursor)
        recentProjects.runJs("const horizontal_offset = component.getWidth()-22;\n" +
            "robot.click(component, new Point(horizontal_offset, 22), MouseButton.LEFT_BUTTON, 1);");

        if (ideaVersionInt >= 20231) {
            ComponentFixture removeDialog = remoteRobot.find(ComponentFixture.class, byXpath(XPathDefinitions.MY_DIALOG), UITestTimeouts.FIXTURE_TIMEOUT);
            removeDialog.findText(ButtonLabels.REMOVE_FROM_LIST_LABEL).click();
        } else {
            List<JPopupMenuFixture> jPopupMenuFixtures = jPopupMenus(JPopupMenuFixture.Companion.byType());
            if (!jPopupMenuFixtures.isEmpty()) {
                JPopupMenuFixture contextMenu = jPopupMenuFixtures.get(0);
                contextMenu.select("Remove from Recent Projects" + '\u2026');
                button(byXpath(XPathDefinitions.REMOVE_PROJECT_BUTTON)).click();
            }
        }
    }
}
