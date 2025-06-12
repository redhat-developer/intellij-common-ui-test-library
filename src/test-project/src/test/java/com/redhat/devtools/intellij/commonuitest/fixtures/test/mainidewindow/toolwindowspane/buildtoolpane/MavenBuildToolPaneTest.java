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
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Maven Build Tool Pane test
 *
 * @author zcervink@redhat.com
 */
class MavenBuildToolPaneTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "maven_build_tool_pane_java_project";
    private static ToolWindowPane toolWinPane;
    private static MavenBuildToolPane mavenBuildToolPane;

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, NewProjectType.MAVEN);
        toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        toolWinPane.openMavenBuildToolPane();
        mavenBuildToolPane = toolWinPane.find(MavenBuildToolPane.class, Duration.ofSeconds(10));
    }

    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void buildProject() {
        mavenBuildToolPane.buildProject("verify", PROJECT_NAME);
        boolean isBuildSuccessful = toolWinPane.find(BuildView.class, Duration.ofSeconds(10)).isBuildSuccessful();
        assertTrue(isBuildSuccessful, "The build should be successful but is not.");
    }

    @Test
    void reloadAllMavenProjects() {
        assertTrue(mavenBuildToolPane.isShowing(), "The maven view pane should be opened but is not.");
        mavenBuildToolPane.reloadAllProjects();
    }

    @Test
    void expandAll() {
        mavenBuildToolPane.collapseAll();
        int itemsCountBeforeExpanding = mavenBuildToolPane.getBuildTree().collectRows().size();
        mavenBuildToolPane.expandAll();
        int itemsCountAfterExpanding = mavenBuildToolPane.getBuildTree().collectRows().size();
        assertTrue(itemsCountAfterExpanding > itemsCountBeforeExpanding, "The 'Expand All' operation was unsuccessful.");
    }

    @Test
    void collapseAll() {
        mavenBuildToolPane.expandAll();
        int itemsCountBeforeCollapsing = mavenBuildToolPane.getBuildTree().collectRows().size();
        assertTrue(itemsCountBeforeCollapsing > 1, "The Maven tree did not expand correctly");
        mavenBuildToolPane.collapseAll();
        int itemsCountAfterCollapsing = mavenBuildToolPane.getBuildTree().collectRows().size();
        assertEquals(1, itemsCountAfterCollapsing);
        assertTrue(itemsCountAfterCollapsing < itemsCountBeforeCollapsing, "The 'Collapse All' operation was unsuccessful.");
    }
}