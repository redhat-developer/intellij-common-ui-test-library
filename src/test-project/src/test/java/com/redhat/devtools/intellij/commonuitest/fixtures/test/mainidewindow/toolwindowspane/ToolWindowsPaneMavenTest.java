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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.mainidewindow.toolwindowspane;

import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ToolWindowsPane Maven test
 *
 * @author zcervink@redhat.com
 */
class ToolWindowsPaneMavenTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "tool_windows_pane_java_maven_project";
    private ToolWindowPane toolWinPane;

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, NewProjectType.MAVEN);
    }

    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @BeforeEach
    void createToolWindowsPaneFixture() {
        toolWinPane = remoteRobot.find(ToolWindowPane.class, UITestTimeouts.FIXTURE_TIMEOUT);
    }

    @Test
    void mavenBuildTest() {
        toolWinPane.openMavenBuildToolPane();
        MavenBuildToolPane mavenBuildToolPane = toolWinPane.find(MavenBuildToolPane.class, UITestTimeouts.FIXTURE_TIMEOUT);
        mavenBuildToolPane.buildProject("verify", PROJECT_NAME);
        BuildView buildView = toolWinPane.find(BuildView.class, UITestTimeouts.FIXTURE_TIMEOUT);
        buildView.waitUntilBuildHasFinished();
        assertTrue(buildView.isBuildSuccessful(), "The build should be successful but is not.");
    }
}