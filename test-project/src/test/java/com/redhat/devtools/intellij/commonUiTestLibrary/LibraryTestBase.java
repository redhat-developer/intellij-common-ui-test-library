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
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.NewProjectDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog.closeTipDialogIfItAppears;

/**
 * Base class for all JUnit tests in the IntelliJ common UI test library
 *
 * @author zcervink@redhat.com
 */
public class LibraryTestBase {
    protected static RemoteRobot remoteRobot;
    private static boolean intelliJHasStarted = false;

    @BeforeAll
    protected static void startIntelliJ() {
        if (!intelliJHasStarted) {
            remoteRobot = UITestRunner.runIde(UITestRunner.IdeaVersion.V_2020_3, 8580);
            intelliJHasStarted = true;
            Runtime.getRuntime().addShutdownHook(new CloseIntelliJBeforeQuit());
        }
    }

    @AfterEach
    protected void finishTestRun() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.clearExceptions();
        flatWelcomeFrame.clearWorkspace();
    }

    protected void createNewProject(String projectName, String projectType) {
        openNewProjectDialogFromWelcomeDialog();
        NewProjectDialog newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
        newProjectDialog.selectNewProjectType(projectType.toString());
        newProjectDialog.setProjectSdkIfAvailable("11");
        newProjectDialog.next();
        // Plain java project has more pages in the 'New project' dialog
        if (projectType.equals("Java")) {
            newProjectDialog.next();
            newProjectDialog.setProjectNameForJavaProject(projectName);
        } else {
            newProjectDialog.setProjectNameForMavenOrGradleProject(projectName);
        }
        newProjectDialog.finish();
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilProjectImportIsComplete();
        closeTipDialogIfItAppears(remoteRobot);
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(5));
        mainIdeWindow.maximizeIdeWindow();
        ideStatusBar.waitUntilAllBgTasksFinish();
    }

    protected void openNewProjectDialogFromWelcomeDialog() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.createNewProject();
    }

    private static class CloseIntelliJBeforeQuit extends Thread {
        public void run() {
            UITestRunner.closeIde();
        }
    }
}
