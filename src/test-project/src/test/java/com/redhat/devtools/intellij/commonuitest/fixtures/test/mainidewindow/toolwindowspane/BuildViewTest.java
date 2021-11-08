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

import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Build View test
 *
 * @author zcervink@redhat.com
 */
class BuildViewTest extends LibraryTestBase {
    private static final String PROJECT_NAME = "build_view_java_project";
    private static ToolWindowsPane toolWindowsPane;

    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, CreateCloseUtils.NewProjectType.MAVEN);
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        toolWindowsPane.openMavenBuildToolPane();
        toolWindowsPane.find(MavenBuildToolPane.class, Duration.ofSeconds(10)).buildProject("install");
    }

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    public void waitForSuccessfulBuildTest() {
        BuildView buildView = toolWindowsPane.find(BuildView.class, Duration.ofSeconds(10));
        buildView.waitUntilBuildHasFinished();
        assertTrue(buildView.isBuildSuccessful(), "The build should be successful but is not.");
    }
}