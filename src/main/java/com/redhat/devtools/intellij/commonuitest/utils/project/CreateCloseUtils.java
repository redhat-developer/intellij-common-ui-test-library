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

import java.time.Duration;

/**
 * Project creation utilities
 *
 * @author zcervink@redhat.com
 */
public class CreateCloseUtils {
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
            newProjectFirstPage.setProjectName(projectName);
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
     * Enumeration for new project type
     */
    public enum NewProjectType {
        PLAIN_JAVA("Java"),
        MAVEN("Maven"),
        GRADLE("Gradle");

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
