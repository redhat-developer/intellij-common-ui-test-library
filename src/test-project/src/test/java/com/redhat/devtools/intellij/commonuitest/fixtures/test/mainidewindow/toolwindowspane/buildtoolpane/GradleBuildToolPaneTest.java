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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.mainidewindow.toolwindowspane.buildtoolpane;

import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.AbstractToolWinPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Gradle Build Tool Pane test
 *
 * @author zcervink@redhat.com
 */
class GradleBuildToolPaneTest extends LibraryTestBase {
    private static final String PROJECT_NAME = "gradle_build_tool_pane_java_project";
    private static AbstractToolWinPane toolWinPane;
    private static GradleBuildToolPane gradleBuildToolPane;

    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, CreateCloseUtils.NewProjectType.GRADLE);
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        } else {
            toolWinPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        }
        toolWinPane.openGradleBuildToolPane();
        gradleBuildToolPane = toolWinPane.find(GradleBuildToolPane.class, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    public void buildProjectTest() {
        gradleBuildToolPane.buildProject();
        boolean isBuildSuccessful = toolWinPane.find(BuildView.class, Duration.ofSeconds(10)).isBuildSuccessful();
        assertTrue(isBuildSuccessful, "The build should be successful but is not.");
    }

    @Test
    public void reloadAllGradleProjects() {
        gradleBuildToolPane.reloadAllGradleProjects();
    }

    @Test
    public void expandAll() {
        gradleBuildToolPane.collapseAll();
        int itemsCountBeforeExpanding = gradleBuildToolPane.gradleTaskTree().collectRows().size();
        gradleBuildToolPane.expandAll();
        int itemsCountAfterExpanding = gradleBuildToolPane.gradleTaskTree().collectRows().size();
        assertTrue(itemsCountAfterExpanding > itemsCountBeforeExpanding, "The 'Expand All' operation was unsuccessful.");
    }

    @Test
    public void collapseAll() {
        try {
            gradleBuildToolPane.gradleTaskTree().expandAll();
        } catch (Exception ignored) {}
        int itemsCountBeforeCollapsing = gradleBuildToolPane.gradleTaskTree().collectRows().size();
        gradleBuildToolPane.collapseAll();
        int itemsCountAfterCollapsing = gradleBuildToolPane.gradleTaskTree().collectRows().size();
        assertTrue(itemsCountAfterCollapsing < itemsCountBeforeCollapsing, "The 'Collapse All' operation was unsuccessful.");
    }
}