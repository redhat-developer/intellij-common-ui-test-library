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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.mainidewindow.toolwindowspane.openclose;

import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ProjectExplorer;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project Explorer Tool Windows Pane test
 *
 * @author zcervink@redhat.com
 */
class ProjectExplorerPaneTest extends AbstractToolWinPaneTest {
    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PLAIN_PROJECT_NAME, NewProjectType.PLAIN_JAVA);
        toolWinPane = remoteRobot.find(ToolWindowPane.class, UITestTimeouts.FIXTURE_TIMEOUT);
    }

    @BeforeEach
    void preparePanes() {
        if (toolWinPane.isPaneOpened(ProjectExplorer.class)) {
            toolWinPane.closeProjectExplorer();
        }
    }

    @Test
    void projectExplorerPaneOpenCloseTest() {
        toolWinPane.openProjectExplorer();
        assertTrue(toolWinPane.isPaneOpened(ProjectExplorer.class), "The 'Project Explorer' should be opened but is closed.");
        toolWinPane.closeProjectExplorer();
        assertFalse(toolWinPane.isPaneOpened(ProjectExplorer.class), "The 'Project Explorer' should be closed but is opened.");
    }

}