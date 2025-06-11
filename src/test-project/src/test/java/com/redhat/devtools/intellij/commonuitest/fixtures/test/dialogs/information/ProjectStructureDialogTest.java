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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.dialogs.information;

import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information.ProjectStructureDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TipDialog test and ProjectStructure test via SearchEverywherePopup
 *
 * @author zcervink@redhat.com, olkornii@redhat.com
 */
class ProjectStructureDialogTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "tip_dialog_java_project";

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, NewProjectType.PLAIN_JAVA);
    }

    @AfterAll
    static void closeProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void projectStructureDialogTest() {
        dialogTest(() -> {
            ProjectStructureDialog projectStructureDialog = remoteRobot.find(ProjectStructureDialog.class, Duration.ofSeconds(10));
            projectStructureDialog.cancel();
        });
    }

    private void dialogTest(Runnable runnable) {
        makeSureDialogIsVisible();
        assertTrue(isDialogVisible(), "The 'Project Structure' dialog should be visible but is not.");
        runnable.run();
        assertFalse(isDialogVisible(), "The 'Project Structure' dialog should be visible but is not.");
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilAllBgTasksFinish();
    }

    private void makeSureDialogIsVisible() {
        try {
            remoteRobot.find((Class<? extends CommonContainerFixture>) ProjectStructureDialog.class, Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
            mainIdeWindow.invokeCmdUsingSearchEverywherePopup("Project Structure");
        }
    }

    private boolean isDialogVisible() {
        try {
            remoteRobot.find((Class<? extends CommonContainerFixture>) ProjectStructureDialog.class, Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }
}