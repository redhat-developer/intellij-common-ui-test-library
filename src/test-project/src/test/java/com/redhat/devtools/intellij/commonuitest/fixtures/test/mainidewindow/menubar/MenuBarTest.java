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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.mainidewindow.menubar;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.menubar.MenuBar;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.AbstractToolWinPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ProjectExplorer;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MenuBar test
 *
 * @author zcervink@redhat.com
 */
class MenuBarTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "project_view_tree_java_project";

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, CreateCloseUtils.NewProjectType.PLAIN_JAVA);
    }

    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void openTipDialogUsingMenuBarTest() {
        if (remoteRobot.isWin() || remoteRobot.isLinux()) {
            new MenuBar(remoteRobot).navigateTo("Help", "Tip of the Day");
            assertTrue(isTipDialogVisible(remoteRobot), "The 'Tip of the Day' dialog should be visible but is not");
            remoteRobot.find(TipDialog.class, Duration.ofSeconds(10)).button(ButtonLabels.CLOSE_LABEL).click();
        }
    }

    @Test
    void closeAndReopenProjectTest() {
        CreateCloseUtils.closeProject(remoteRobot);
        CreateCloseUtils.openProjectFromWelcomeDialog(remoteRobot, PROJECT_NAME);

        AbstractToolWinPane toolWinPane;
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        } else {
            toolWinPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        }
        toolWinPane.openProjectExplorer();
        ProjectExplorer projectExplorer = toolWinPane.find(ProjectExplorer.class, Duration.ofSeconds(10));
        assertTrue(projectExplorer.isItemPresent(PROJECT_NAME), "The project should be back open, but it is not");
    }

    private boolean isTipDialogVisible(RemoteRobot remoteRobot) {
        try {
            remoteRobot.find(TipDialog.class, Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }
}