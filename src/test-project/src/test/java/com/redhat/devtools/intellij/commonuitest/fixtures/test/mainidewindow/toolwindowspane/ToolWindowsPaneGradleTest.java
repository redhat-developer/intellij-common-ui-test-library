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
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ToolWindowsPane Gradle test
 *
 * @author zcervink@redhat.com
 */
class ToolWindowsPaneGradleTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "tool_windows_pane_java_gradle_project";
    private ToolWindowPane toolWinPane;

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, NewProjectType.GRADLE);
    }

    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @BeforeEach
    void createToolWindowsPaneFixture() {
        toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
    }

    @Test
    void gradleTest() {
        toolWinPane.openGradleBuildToolPane();
        GradleBuildToolPane gradleBuildToolPane = toolWinPane.find(GradleBuildToolPane.class, Duration.ofSeconds(10));
        gradleBuildToolPane.buildProject("build");
        BuildView buildView = toolWinPane.find(BuildView.class, Duration.ofSeconds(10));
        buildView.waitUntilBuildHasFinished();
        assertTrue(buildView.isBuildSuccessful(), "The build should be successful but is not.");
    }
}