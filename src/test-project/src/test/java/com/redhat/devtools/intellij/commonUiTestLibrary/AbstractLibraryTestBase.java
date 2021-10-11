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
package com.redhat.devtools.intellij.commonUiTestLibrary;

import com.intellij.remoterobot.RemoteRobot;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.JavaProjectSecondPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.NewProjectDialogFirstPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.abstractPages.AbstractTerminalPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;

import java.time.Duration;

/**
 * Abstract base class for all JUnit tests in the IntelliJ common UI test library
 *
 * @author zcervink@redhat.com
 */
public abstract class AbstractLibraryTestBase {
    protected static RemoteRobot remoteRobot;

    protected static void createNewProject(String projectName, String projectType) {
        openNewProjectDialogFromWelcomeDialog();
        NewProjectDialogWizard newProjectDialogWizard = remoteRobot.find(NewProjectDialogWizard.class, Duration.ofSeconds(10));
        NewProjectDialogFirstPage newProjectDialogFirstPage = newProjectDialogWizard.find(NewProjectDialogFirstPage.class, Duration.ofSeconds(10));
        newProjectDialogFirstPage.selectNewProjectType(projectType);
        newProjectDialogFirstPage.setProjectSdkIfAvailable("11");
        newProjectDialogFirstPage.next();
        // Plain java project has more pages in the 'New project' dialog
        if (projectType.equals("Java")) {
            newProjectDialogWizard.find(JavaProjectSecondPage.class, Duration.ofSeconds(10)).next();
        }
        AbstractTerminalPage abstractTerminalPage = newProjectDialogWizard.find(AbstractTerminalPage.class, Duration.ofSeconds(10));
        abstractTerminalPage.setProjectName(projectName);
        abstractTerminalPage.finish();
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilProjectImportIsComplete();
        TipDialog.closeTipDialogIfItAppears(remoteRobot);
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(5));
        mainIdeWindow.maximizeIdeWindow();
        ideStatusBar.waitUntilAllBgTasksFinish();
    }

    protected static void openNewProjectDialogFromWelcomeDialog() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.createNewProject();
    }

    protected static void closeProject() {
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
        mainIdeWindow.closeProject();
    }
}
