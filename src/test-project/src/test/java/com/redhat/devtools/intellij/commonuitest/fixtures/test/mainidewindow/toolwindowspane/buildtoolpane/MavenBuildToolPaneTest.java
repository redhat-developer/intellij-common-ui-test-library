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
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.AbstractToolWinPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Maven Build Tool Pane test
 *
 * @author zcervink@redhat.com
 */
class MavenBuildToolPaneTest extends LibraryTestBase {
    private static final String PROJECT_NAME = "maven_build_tool_pane_java_project";
    private static AbstractToolWinPane toolWinPane;
    private static MavenBuildToolPane mavenBuildToolPane;

    @BeforeAll
    public static void prepareProject() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        NewProjectDialogWizard newProjectDialogWizard = flatWelcomeFrame.openNewProjectDialogFromWelcomeDialog(remoteRobot);
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, CreateCloseUtils.NewProjectType.MAVEN, newProjectDialogWizard);
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        } else {
            toolWinPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        }
        toolWinPane.openMavenBuildToolPane();
        mavenBuildToolPane = toolWinPane.find(MavenBuildToolPane.class, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void closeCurrentProject() {
        remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10)).closeProject();
    }

    @Test
    public void buildProjectTest() {
        mavenBuildToolPane.buildProject("install");
        boolean isBuildSuccessful = toolWinPane.find(BuildView.class, Duration.ofSeconds(10)).isBuildSuccessful();
        assertTrue(isBuildSuccessful, "The build should be successful but is not.");
    }

    @Test
    public void reloadAllMavenProjectsTest() {
        mavenBuildToolPane.reloadAllMavenProjects();
    }

    @Test
    public void collapseAllTest() {
        mavenBuildToolPane.mavenTargetTree().expandAll();
        int itemsCountBeforeCollapsing = mavenBuildToolPane.mavenTargetTree().collectRows().size();
        mavenBuildToolPane.collapseAll();
        int itemsCountAfterCollapsing = mavenBuildToolPane.mavenTargetTree().collectRows().size();
        assertTrue(itemsCountAfterCollapsing < itemsCountBeforeCollapsing, "The 'Collapse All' operation was unsuccessful.");
    }
}