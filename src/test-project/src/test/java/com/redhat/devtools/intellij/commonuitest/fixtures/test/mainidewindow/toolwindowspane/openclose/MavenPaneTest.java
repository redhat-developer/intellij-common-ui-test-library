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

import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.labels.ButtonLabels;
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
class MavenPaneTest extends AbstractToolWindowsPaneTest {
    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, MAVEN_PROJECT_NAME, CreateCloseUtils.NewProjectType.MAVEN);
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
    }

    @BeforeEach
    public void preparePanes() {
        if (isPaneOpened(MavenBuildToolPane.class)) {
            closePane(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, MavenBuildToolPane.class);
        }
    }

    @Test
    public void mavenBuildToolPaneOpenCloseTest() {
        toolWindowsPane.openMavenBuildToolPane();
        assertTrue(isPaneOpened(MavenBuildToolPane.class), "The 'Maven Build Tool Pane' should be opened but is closed.");
        toolWindowsPane.closeMavenBuildToolPane();
        assertFalse(isPaneOpened(MavenBuildToolPane.class), "The 'Maven Build Tool Pane' should be closed but is opened.");
    }
}