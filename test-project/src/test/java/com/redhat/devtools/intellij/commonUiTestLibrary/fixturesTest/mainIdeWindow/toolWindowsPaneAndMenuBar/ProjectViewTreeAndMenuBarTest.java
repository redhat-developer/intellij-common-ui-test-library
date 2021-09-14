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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.mainIdeWindow.toolWindowsPaneAndMenuBar;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.menuBar.MenuBar;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils.listOfRemoteTextToString;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProjectViewTree and MenuBar test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
class ProjectViewTreeAndMenuBarTest extends LibraryTestBase {
    private final String projectName = "project_view_tree_java_project";

    @BeforeEach
    public void prepareProject() {
        createNewProject(projectName, "Java");
    }

    @AfterEach
    public void closeCurrentProject() {
        super.closeProject();
    }

    @Test
    public void projectViewTreeTest() {
        ToolWindowsPane projectViewTree = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        String projectViewTreeContent = listOfRemoteTextToString(projectViewTree.projectViewTree().findAllText());
        assertTrue(projectViewTreeContent.contains(projectName), "The 'ProjectViewTree' does not contain the project name (" + projectName + ").");
    }

    @Test
    public void menuBarTest() {
        if (remoteRobot.isWin() || remoteRobot.isLinux()) {
            new MenuBar(remoteRobot).navigateTo("Help", "Tip of the Day");
            assertTrue(isTipDialogVisible(remoteRobot), "The 'Tip of the Day' dialog should be visible but is not");
            remoteRobot.find(TipDialog.class, Duration.ofSeconds(10)).button("Close").click();
        }
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