package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.dialogs.navigation;
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
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.ProjectStructureDialog;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog.closeTipDialogIfItAppears;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TipDialog test and ProjectStructure test via SearchEverywherePopup
 *
 * @author zcervink@redhat.com
 */
public class ProjectStructureAndTipDialogs extends LibraryTestBase {
    private static final String projectName = "tip_dialog_java_project";

    @BeforeAll
    public static void prepareProject() {
        createNewProject(projectName, "Java");
    }

    @AfterAll
    public static void closeProject() {
        LibraryTestBase.closeProject();
    }


    @Test
    public void projectStructureDialogTest() {
        makeSureDialogIsVisible(ProjectStructureDialog.class, "Project Structure");
        assertTrue(isDialogVisible(ProjectStructureDialog.class), "The 'Project Structure' dialog should be visible but is not.");
        ProjectStructureDialog projectStructureDialog = remoteRobot.find(ProjectStructureDialog.class, Duration.ofSeconds(10));
        projectStructureDialog.cancel();
        assertTrue(!isDialogVisible(ProjectStructureDialog.class), "The 'Project Structure' dialog should be closed but is not.");
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilAllBgTasksFinish();
    }

    @Test
    public void tipDialogTest() {
        makeSureDialogIsVisible(TipDialog.class, "Tip of the Day");
        assertTrue(isDialogVisible(TipDialog.class), "The 'Tip of the Day' dialog should be visible but is not.");
        closeTipDialogIfItAppears(remoteRobot);
        assertTrue(!isDialogVisible(TipDialog.class), "The 'Tip of the Day' dialog should be closed but is not.");
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilAllBgTasksFinish();
    }

    private void makeSureDialogIsVisible(Class dialogClass, String dialogTitle) {
        try {
            remoteRobot.find(dialogClass, Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
            mainIdeWindow.invokeCmdUsingSearchEverywherePopup(dialogTitle);
        }
    }

    private boolean isDialogVisible(Class dialogClass) {
        try {
            remoteRobot.find(dialogClass, Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }
}