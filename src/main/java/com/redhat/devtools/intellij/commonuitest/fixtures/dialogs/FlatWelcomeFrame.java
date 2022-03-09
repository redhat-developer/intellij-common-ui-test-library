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
import com.intellij.remoterobot.fixtures.JLabelFixture;
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
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Welcome to IntelliJ IDEA dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "FlatWelcomeFrame type", xpath = "//div[@class='FlatWelcomeFrame']")
@FixtureName(name = "Welcome To IntelliJ IDEA Dialog")
public class FlatWelcomeFrame extends CommonContainerFixture {
    private static final Logger LOGGER = Logger.getLogger(FlatWelcomeFrame.class.getName());
    private static final String JBLIST_XPATH = "//div[@class='JBList']";
    private static final String TREE_XPATH = "//div[@class='Tree']";
    private static final String PROJECTS_BUTTON = "Projects";
    private final RemoteRobot remoteRobot;
    private final UITestRunner.IdeaVersion intelliJVersion;
    private final int ideaVersion;

    public FlatWelcomeFrame(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
        this.intelliJVersion = UITestRunner.getIdeaVersion();
        this.ideaVersion = intelliJVersion.toInt();
    }

    /**
     * Click on the 'New Project' link
     */
    public void createNewProject() {
        clickOnLink("New Project");
    }

    /**
     * Click on the link according to given label
     *
     * @param label label of the link to click on
     */
    public void clickOnLink(String label) {
        // Code for IntelliJ IDEA 2020.3 or newer
        if (intelliJVersion.toInt() >= 20203) {
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
        List<JListFixture> jListFixtures = jLists(byXpath("//div[@accessiblename='Recent Projects']"));

        for (int i = 0; i < projectsCount(); i++) {
            JListFixture recentProjectsList = jListFixtures.get(0);
            recentProjectsList.runJs("const horizontal_offset = component.getWidth()-22;\n" +
                    "robot.click(component, new Point(horizontal_offset, 22), MouseButton.LEFT_BUTTON, 1);");
            // Code for IntelliJ Idea 2020.3 or newer
            if (intelliJVersion.toInt() >= 20203) {
                List<JPopupMenuFixture> jPopupMenuFixtures = jPopupMenus(JPopupMenuFixture.Companion.byType());
                if (!jPopupMenuFixtures.isEmpty()) {
                    JPopupMenuFixture contextMenu = jPopupMenuFixtures.get(0);
                    contextMenu.select("Remove from Recent Projects");
                }
            }
        }

        try {
            String pathToDirToMakeEmpty = System.getProperty("user.home") + File.separator + "IdeaProjects";
            boolean doesProjectDirExists = Files.exists(Paths.get(pathToDirToMakeEmpty));
            if (doesProjectDirExists) {
                FileUtils.cleanDirectory(new File(pathToDirToMakeEmpty));
            } else {
                Files.createDirectory(Paths.get(pathToDirToMakeEmpty));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
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
            LOGGER.log(Level.INFO, e.getMessage(), e);

            try {
                find(IdeFatalErrorsDialog.class, Duration.ofSeconds(10)).clearAll();
            } catch (Exception e2) {
                LOGGER.log(Level.INFO, e.getMessage(), e2);
            }
        }
    }

    /**
     * Open the 'Preferences' dialog
     */
    public void openSettingsDialog() {
        if (intelliJVersion.toInt() <= 20202) {
            clickOnLink("Configure");
            HeavyWeightWindowFixture heavyWeightWindowFixture = find(HeavyWeightWindowFixture.class, Duration.ofSeconds(5));
            heavyWeightWindowFixture.findText("Preferences").click();
        } else if (intelliJVersion.toInt() <= 20212) {
            JListFixture jListFixture = remoteRobot.find(JListFixture.class, byXpath(JBLIST_XPATH));
            jListFixture.clickItem("Customize", false);
            remoteRobot.find(ContainerFixture.class, byXpath("//div[@class='DialogPanel']")).findText("All settings" + '\u2026').click();
        } else {
            JTreeFixture jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(TREE_XPATH));
            jTreeFixture.findText("Customize").click();
            remoteRobot.find(ContainerFixture.class, byXpath("//div[@class='DialogPanel']")).findText("All settings" + '\u2026').click();
        }
    }

    /**
     * Open the 'Tip Of the Day' dialog
     *
     * @return fixture for the 'Tip Of the Day' dialog
     */
    public TipDialog openTipDialog() {
        if (ideaVersion <= 20202) {
            clickOnLink("Get Help");
            HeavyWeightWindowFixture heavyWeightWindowFixture = find(HeavyWeightWindowFixture.class, Duration.ofSeconds(5));
            heavyWeightWindowFixture.findText("Tip of the Day").click();
        } else if (ideaVersion <= 20203) {
            actionLink("Help").click();
            HeavyWeightWindowFixture heavyWeightWindowFixture = find(HeavyWeightWindowFixture.class, Duration.ofSeconds(5));
            heavyWeightWindowFixture.findText("Tip of the Day").click();
        } else if (ideaVersion <= 20212) {
            JListFixture jListFixture = remoteRobot.find(JListFixture.class, byXpath(JBLIST_XPATH));
            jListFixture.findText("Learn IntelliJ IDEA").click();
            remoteRobot.find(JLabelFixture.class, byXpath("//div[@text='Tip of the Day']")).click();
        } else {
            JTreeFixture jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(TREE_XPATH));
            jTreeFixture.findText("Learn IntelliJ IDEA").click();
            remoteRobot.find(JLabelFixture.class, byXpath("//div[@text='Tip of the Day']")).click();
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
        notificationsPage.disableNotifications();
        settingsDialog.ok();

        if (ideaVersion >= 20213) {
            JTreeFixture jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(TREE_XPATH));
            jTreeFixture.findText(PROJECTS_BUTTON).click();
        } else if (ideaVersion >= 20203) {
            JListFixture jListFixture = remoteRobot.find(JListFixture.class, byXpath(JBLIST_XPATH));
            jListFixture.clickItem(PROJECTS_BUTTON, false);
        }
    }

    /**
     * Prevent the 'Tip of the Day' dialog from opening after project import
     */
    public void preventTipDialogFromOpening() {
        TipDialog tipDialog = openTipDialog();
        tipDialog.dontShowTipsCheckBox().setValue(true);
        tipDialog.close();

        if (ideaVersion >= 20213) {
            JTreeFixture jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(TREE_XPATH));
            jTreeFixture.findText(PROJECTS_BUTTON).click();
        } else if (ideaVersion >= 20203) {
            JListFixture jListFixture = remoteRobot.find(JListFixture.class, byXpath(JBLIST_XPATH));
            jListFixture.clickItem(PROJECTS_BUTTON, false);
        }
    }

    private int projectsCount() {
        try {
            ContainerFixture projectWrapper = find(ContainerFixture.class, byXpath("//div[@class='NewRecentProjectPanel']"));
            JListFixture projectList = projectWrapper.find(JListFixture.class, byXpath("//div[@class='MyList']"));
            return projectList.collectItems().size();
        } catch (WaitForConditionTimeoutException e) {
            return 0;
        }
    }

    // Works for IntelliJ Idea 2020.3+
    private JButtonFixture welcomeFrameLink(String label) {
        if (UtilsKt.hasAnyComponent(this, byXpath("//div[@class='NewRecentProjectPanel']"))) {
            return button(byXpath("//div[@class='JBOptionButton' and @text='" + label + "']"), Duration.ofSeconds(2));
        }
        return button(byXpath("//div[@class='NonOpaquePanel'][./div[@text='" + label + "']]"), Duration.ofSeconds(2));
    }

    private ComponentFixture ideErrorsIcon() {
        return find(ComponentFixture.class, byXpath("//div[@class='IdeErrorsIcon']"), Duration.ofSeconds(10));
    }
}