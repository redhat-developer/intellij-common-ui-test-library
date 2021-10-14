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
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Maven Build Tool Pane test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
class MavenBuildToolPaneTest extends LibraryTestBase {
    private static final String projectName = "maven_build_tool_pane_java_project";
    private static ToolWindowsPane toolWindowsPane;
    private static MavenBuildToolPane mavenBuildToolPane;

    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, projectName, CreateCloseUtils.NewProjectType.MAVEN);
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        toolWindowsPane.openMavenBuildToolPane();
        mavenBuildToolPane = toolWindowsPane.find(MavenBuildToolPane.class, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    public void buildProjectTest() {
        mavenBuildToolPane.buildProject();
        toolWindowsPane.find(BuildView.class, Duration.ofSeconds(10)).testIfBuildIsSuccessful();
    }

    @Test
    public void reloadAllMavenProjectsTest() {
        mavenBuildToolPane.reloadAllMavenProjects();
    }

    @Test
    public void collapseAllTest() {
        mavenTargetTree().expandAll();
        int itemsCountBeforeCollapsing = mavenTargetTree().collectRows().size();
        mavenBuildToolPane.collapseAll();
        int itemsCountAfterCollapsing = mavenTargetTree().collectRows().size();
        assertTrue(itemsCountAfterCollapsing < itemsCountBeforeCollapsing, "The 'Collapse All' operation was unsuccessful.");
    }

    private JTreeFixture mavenTargetTree() {
        return mavenBuildToolPane.find(JTreeFixture.class, JTreeFixture.Companion.byType(), Duration.ofSeconds(10));
    }
}