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
package com.redhat.devtools.intellij.commonuitest.utils.project;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information.CodeWithMeDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.AbstractNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.JavaNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.MavenGradleNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.NewProjectFirstPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Project creation utilities
 *
 * @author zcervink@redhat.com
 */
public class CreateCloseUtils {
    public static final String PROJECT_LOCATION = Optional.ofNullable(System.getProperty("testProjectLocation"))        // For more info on testProjectLocation please check README
            .filter(s -> !s.isEmpty())
            .orElseGet(() -> System.getProperty("user.home") + File.separator + "IdeaProjects" + File.separator + "intellij-ui-test-projects");

    /**
     * Create new project with given project name according to given project type
     *
     * @param remoteRobot    reference to the RemoteRobot instance
     * @param projectName    name of new project
     * @param newProjectType type of new project
     */
    public static void createNewProject(RemoteRobot remoteRobot, String projectName, NewProjectType newProjectType) {
        NewProjectDialogWizard newProjectDialogWizard = openNewProjectDialogFromWelcomeDialog(remoteRobot);
        NewProjectFirstPage newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, Duration.ofSeconds(10));

        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            newProjectFirstPage.selectNewProjectType("New Project");
            newProjectFirstPage.setLanguage("Java");
            switch (newProjectType) {
                case PLAIN_JAVA:
                    newProjectFirstPage.setBuildSystem("IntelliJ");
                    break;
                case MAVEN:
                case GRADLE:
                    newProjectFirstPage.setBuildSystem(newProjectType.toString());
                    break;
            }
        } else {
            newProjectFirstPage.selectNewProjectType(newProjectType.toString());
        }

        newProjectFirstPage.setProjectSdkIfAvailable("17");

        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            newProjectFirstPage.getProjectNameTextField().click(); // Click to gain focus on newProjectFirstPage

            newProjectFirstPage.setProjectName(projectName);
            newProjectFirstPage.setProjectLocation(PROJECT_LOCATION);
        } else {
            newProjectDialogWizard.next();
            // Plain java project has more pages in the 'New project' dialog
            if (newProjectType.equals(NewProjectType.PLAIN_JAVA)) {
                newProjectDialogWizard.next();
            }
            AbstractNewProjectFinalPage finalPage = getFinalPage(newProjectDialogWizard, newProjectType);
            finalPage.setProjectName(projectName);
        }

        newProjectDialogWizard.finish();

        waitAfterOpeningProject(remoteRobot);
    }

    /**
     * Create new empty type project with given project name
     *
     * @param remoteRobot    reference to the RemoteRobot instance
     * @param projectName    name of new project
     */
    public static void createEmptyProject(RemoteRobot remoteRobot, String projectName) {
        NewProjectDialogWizard newProjectDialogWizard = openNewProjectDialogFromWelcomeDialog(remoteRobot);
        NewProjectFirstPage newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, Duration.ofSeconds(10));

        newProjectFirstPage.selectNewProjectType(NewProjectType.EMPTY_PROJECT.toString());
        ensureEmptyProjectPageIsOpened(newProjectFirstPage, remoteRobot);

        newProjectFirstPage.setProjectName(projectName);
        newProjectFirstPage.setProjectLocation(PROJECT_LOCATION);

        newProjectDialogWizard.finish();
        waitAfterOpeningProject(remoteRobot);
    }

    /**
     * Wait after opening project for a complete import, also maximizing window
     *
     * @param remoteRobot reference to the RemoteRobot instance
     */
    public static void waitAfterOpeningProject(RemoteRobot remoteRobot) {
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilProjectImportIsComplete();
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(5));
        mainIdeWindow.maximizeIdeWindow();
        ideStatusBar.waitUntilAllBgTasksFinish(500);
        CodeWithMeDialog.closeCodeWithMePopupIfItAppears(remoteRobot);
    }

    /**
     * Open 'New Project' dialog from 'Welcome to IntelliJ IDEA' dialog
     *
     * @param remoteRobot reference to the RemoteRobot instance
     * @return NewProjectDialogWizard fixture
     */
    public static NewProjectDialogWizard openNewProjectDialogFromWelcomeDialog(RemoteRobot remoteRobot) {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.switchToProjectsPage();
        flatWelcomeFrame.createNewProject();
        return remoteRobot.find(NewProjectDialogWizard.class, Duration.ofSeconds(10));
    }

    /**
     * Close currently opened project
     *
     * @param remoteRobot reference to the RemoteRobot instance
     */
    public static void closeProject(RemoteRobot remoteRobot) {
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
        mainIdeWindow.closeProject();
        remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
    }

    /**
     * Open existing project from the Welcome Dialog
     *
     * @param remoteRobot reference to the RemoteRobot instance
     * @param projectName name of existing project
     */
    public static void openProjectFromWelcomeDialog(RemoteRobot remoteRobot, String projectName) {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.openProject(projectName);

        waitAfterOpeningProject(remoteRobot);
    }

    /**
     * Get appropriate final page instance
     *
     * @param newProjectDialogWizard instance of the 'New Project' dialog fixture
     * @param newProjectType         type of new project
     * @return final page instance
     */
    public static AbstractNewProjectFinalPage getFinalPage(NewProjectDialogWizard newProjectDialogWizard, NewProjectType newProjectType) {
        switch (newProjectType) {
            case PLAIN_JAVA:
                return newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
            case MAVEN:
            case GRADLE:
                return newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));
            default:
                throw new UITestException("Unsupported project type.");
        }
    }

    /**
     * Ensures that the Empty Project page is opened by checking for specific text on the page.
     * If verification fails, it waits for dialogs to disappear and reselects the Empty Project type.
     *
     * @param newProjectFirstPage the first page of the new project dialog
     * @param remoteRobot         reference to the RemoteRobot instance
     */
    private static void ensureEmptyProjectPageIsOpened(NewProjectFirstPage newProjectFirstPage, RemoteRobot remoteRobot) {
        int ideaVersionInt = UITestRunner.getIdeaVersionInt();
        boolean isEmptyProjectPageDisplayed;

        if (ideaVersionInt >= 20231) {  // For IntelliJ IDEA version 2023.1 and newer
            isEmptyProjectPageDisplayed = newProjectFirstPage.hasText("A basic project with free structure.");
        } else {  // For IntelliJ IDEA version 2022.1 and newer
            isEmptyProjectPageDisplayed = newProjectFirstPage.hasText("A basic project that allows working with separate files and compiling Java and Kotlin classes.");
        }

        if (!isEmptyProjectPageDisplayed) {
            // If the expected text is not found, wait for dialogs to disappear and reselect the Empty Project type
            waitForDialogsToDisappear(remoteRobot, Duration.ofSeconds(20));
            newProjectFirstPage.selectNewProjectType(NewProjectType.EMPTY_PROJECT.toString());
        }
    }

    /**
     * Waits until only the "New Project" dialog is open.
     * If any other dialogs are open, it waits for them to disappear.
     *
     * @param remoteRobot the RemoteRobot instance
     * @param timeout the maximum duration to wait for the other dialogs to disappear
     */
    private static void waitForDialogsToDisappear(RemoteRobot remoteRobot, Duration timeout) {
        waitFor(
                timeout,
                Duration.ofSeconds(2),
                "Waiting for only the New Project dialog to remain open",
                () -> "Extra dialogs did not disappear within the timeout",
                () -> {
                    List<ComponentFixture> allDialogs = remoteRobot.findAll(ComponentFixture.class, byXpath(XPathDefinitions.MY_DIALOG));
                    // Proceed if only one dialog is open, assumed to be the New Project dialog
                    return allDialogs.size() == 1;
                }
        );
    }

    /**
     * Enumeration for new project type
     */
    public enum NewProjectType {
        PLAIN_JAVA("Java"),
        MAVEN("Maven"),
        GRADLE("Gradle"),
        EMPTY_PROJECT("Empty Project");

        private final String projectType;

        NewProjectType(String projectType) {
            this.projectType = projectType;
        }

        @Override
        public String toString() {
            return this.projectType;
        }
    }
}
