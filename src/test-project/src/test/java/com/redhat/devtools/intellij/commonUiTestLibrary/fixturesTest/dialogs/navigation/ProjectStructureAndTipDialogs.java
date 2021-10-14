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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.dialogs.navigation;

import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.ProjectStructureDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TipDialog test and ProjectStructure test via SearchEverywherePopup
 *
 * @author zcervink@redhat.com, olkornii@redhat.com
 */
public class ProjectStructureAndTipDialogs extends LibraryTestBase {
    private static final String projectName = "tip_dialog_java_project";

    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, projectName, CreateCloseUtils.NewProjectType.PLAIN_JAVA);
    }

    @AfterAll
    public static void closeProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    public void projectStructureDialogTest() {
        dialogTest(() -> {
            ProjectStructureDialog projectStructureDialog = remoteRobot.find(ProjectStructureDialog.class, Duration.ofSeconds(10));
            projectStructureDialog.cancel();
        }, ProjectStructureDialog.class, "Project Structure");
    }

    @Test
    public void tipDialogTest() {
        dialogTest(() -> {
            TipDialog.closeTipDialogIfItAppears(remoteRobot);
        }, TipDialog.class, "Tip of the Day");
    }

    private void dialogTest(Runnable selectedImpl, Class dialogClass, String dialogTitle) {
        makeSureDialogIsVisible(dialogClass, dialogTitle);
        assertTrue(isDialogVisible(dialogClass), "The '" + dialogTitle + "' dialog should be visible but is not.");
        selectedImpl.run();
        assertTrue(!isDialogVisible(dialogClass), "The '" + dialogTitle + "' dialog should be visible but is not.");
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