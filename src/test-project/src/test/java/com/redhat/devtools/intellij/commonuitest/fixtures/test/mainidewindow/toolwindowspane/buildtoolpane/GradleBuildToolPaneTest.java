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

import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
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
class GradleBuildToolPaneTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "gradle_build_tool_pane_java_project";
    private static ToolWindowPane toolWinPane;
    private static GradleBuildToolPane gradleBuildToolPane;

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, NewProjectType.GRADLE);
        toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        toolWinPane.openGradleBuildToolPane();
        gradleBuildToolPane = toolWinPane.find(GradleBuildToolPane.class, Duration.ofSeconds(10));
    }

    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void buildProject() {
        gradleBuildToolPane.buildProject("build");
        BuildView buildView = toolWinPane.find(BuildView.class, Duration.ofSeconds(10));
        buildView.waitUntilBuildHasFinished();
        assertTrue(buildView.isBuildSuccessful(), "The build should be successful but is not.");
    }

    @Test
    void reloadAllGradleProjects() {
        assertTrue(gradleBuildToolPane.isShowing(), "The gradle view pane should be opened but is not.");
        gradleBuildToolPane.reloadAllProjects();
    }

    @Test
    void expandAll() {
        gradleBuildToolPane.collapseAll();
        int itemsCountBeforeExpanding = gradleBuildToolPane.getBuildTree().collectRows().size();
        gradleBuildToolPane.expandAll();
        int itemsCountAfterExpanding = gradleBuildToolPane.getBuildTree().collectRows().size();
        assertTrue(itemsCountAfterExpanding > itemsCountBeforeExpanding, "The 'Expand All' operation was unsuccessful.");
    }

    @Test
    void collapseAll() {
        gradleBuildToolPane.expandAll();
        int itemsCountBeforeCollapsing = gradleBuildToolPane.getBuildTree().collectRows().size();
        gradleBuildToolPane.collapseAll();
        int itemsCountAfterCollapsing = gradleBuildToolPane.getBuildTree().collectRows().size();
        assertTrue(itemsCountAfterCollapsing < itemsCountBeforeCollapsing, "The 'Collapse All' operation was unsuccessful.");
    }
}