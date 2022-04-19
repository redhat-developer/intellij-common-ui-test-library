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

import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.constans.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Maven Tool Windows Pane test
 *
 * @author zcervink@redhat.com
 */
class MavenPaneTest extends AbstractToolWinPane {
    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, MAVEN_PROJECT_NAME, CreateCloseUtils.NewProjectType.MAVEN);
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        } else {
            toolWinPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        }
    }

    @BeforeEach
    public void preparePanes() {
        if (isPaneOpened(MavenBuildToolPane.class)) {
            closePane(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, MavenBuildToolPane.class);
        }
    }

    @Test
    public void mavenBuildToolPaneOpenCloseTest() {
        toolWinPane.openMavenBuildToolPane();
        assertTrue(isPaneOpened(MavenBuildToolPane.class), "The 'Maven Build Tool Pane' should be opened but is closed.");
        toolWinPane.closeMavenBuildToolPane();
        assertFalse(isPaneOpened(MavenBuildToolPane.class), "The 'Maven Build Tool Pane' should be closed but is opened.");
    }
}