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

import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane.ToolToBuildProject.GRADLE;
import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane.ToolToBuildProject.MAVEN;

/**
 * ToolWindowsPane test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
class ToolWindowsPaneTest extends LibraryTestBase {
    private final String mavenProjectName = "tool_windows_pane_java_maven_project";
    private final String gradleProjectName = "tool_windows_pane_java_gradle_project";

    @AfterEach
    public void closeCurrentProject() {
        super.closeProject();
    }

    @Test
    public void toolWindowsPaneMavenTest() {
        prepareAndBuildNewMavenOrGradleProject(mavenProjectName, MAVEN);
    }

    @Test
    public void toolWindowsPaneGradleTest() {
        prepareAndBuildNewMavenOrGradleProject(gradleProjectName, GRADLE);
    }

    private void prepareAndBuildNewMavenOrGradleProject(String projectName, ToolWindowsPane.ToolToBuildProject projectType) {
        createNewProject(projectName, projectType.toString());
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        toolWindowsPane.buildProject(projectType);
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilAllBgTasksFinish();
        toolWindowsPane.testIfBuildIsSuccessful();
    }
}