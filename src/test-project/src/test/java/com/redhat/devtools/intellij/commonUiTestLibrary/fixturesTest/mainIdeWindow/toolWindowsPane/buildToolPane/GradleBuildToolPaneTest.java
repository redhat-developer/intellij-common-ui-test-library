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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.mainIdeWindow.toolWindowsPane.buildToolPane;

import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.BuildView;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Gradle Build Tool Pane test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
class GradleBuildToolPaneTest extends LibraryTestBase {
    private static final String projectName = "gradle_build_tool_pane_java_project";
    private static ToolWindowsPane toolWindowsPane;
    private static GradleBuildToolPane gradleBuildToolPane;

    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, projectName, CreateCloseUtils.NewProjectType.GRADLE);
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        toolWindowsPane.openGradleBuildToolPane();
        gradleBuildToolPane = toolWindowsPane.find(GradleBuildToolPane.class, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    public void buildProjectTest() {
        gradleBuildToolPane.buildProject();
        toolWindowsPane.find(BuildView.class, Duration.ofSeconds(10)).testIfBuildIsSuccessful();
    }

    @Test
    public void reloadAllGradleProjects() {
        gradleBuildToolPane.reloadAllGradleProjects();
    }

    @Test
    public void expandAll() {
        gradleBuildToolPane.collapseAll();
        int itemsCountBeforeExpanding = gradleTaskTree().collectRows().size();
        gradleBuildToolPane.expandAll();
        int itemsCountAfterExpanding = gradleTaskTree().collectRows().size();
        assertTrue(itemsCountAfterExpanding > itemsCountBeforeExpanding, "The 'Expand All' operation was unsuccessful.");
    }

    @Test
    public void collapseAll() {
        gradleTaskTree().expandAll();
        int itemsCountBeforeCollapsing = gradleTaskTree().collectRows().size();
        gradleBuildToolPane.collapseAll();
        int itemsCountAfterCollapsing = gradleTaskTree().collectRows().size();
        assertTrue(itemsCountAfterCollapsing < itemsCountBeforeCollapsing, "The 'Collapse All' operation was unsuccessful.");
    }

    private JTreeFixture gradleTaskTree() {
        return gradleBuildToolPane.find(JTreeFixture.class, JTreeFixture.Companion.byType(), Duration.ofSeconds(10));
    }
}