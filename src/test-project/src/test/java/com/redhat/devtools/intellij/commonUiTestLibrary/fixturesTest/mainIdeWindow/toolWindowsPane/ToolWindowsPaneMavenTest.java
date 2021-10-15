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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.mainIdeWindow.toolWindowsPane;

import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * ToolWindowsPane Maven test
 *
 * @author zcervink@redhat.com
 */
class ToolWindowsPaneMavenTest extends LibraryTestBase {
    private static final String projectName = "tool_windows_pane_java_maven_project";
    private ToolWindowsPane toolWindowsPane;

    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, projectName, CreateCloseUtils.NewProjectType.MAVEN);
    }

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @BeforeEach
    public void createToolWindowsPaneFixture() {
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
    }

    @Test
    public void mavenBuildTest() {
        step("@Test - build maven project", () -> {
            toolWindowsPane.openMavenBuildToolPane();
            MavenBuildToolPane mavenBuildToolPane = toolWindowsPane.find(MavenBuildToolPane.class, Duration.ofSeconds(10));
            mavenBuildToolPane.buildProject("install");
        });
    }
}