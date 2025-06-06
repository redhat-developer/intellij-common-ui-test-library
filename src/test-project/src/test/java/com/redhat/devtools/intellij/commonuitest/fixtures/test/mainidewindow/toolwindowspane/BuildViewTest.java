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
 * Build View test
 *
 * @author zcervink@redhat.com
 */
class BuildViewTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "build_view_java_project";
    private static AbstractToolWinPane toolWinPane;

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, CreateCloseUtils.NewProjectType.MAVEN);
        if (ideaVersionInt >= 20221) {
            toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        } else {
            toolWinPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        }
    }

    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void waitForSuccessfulBuildTest() {
        toolWinPane.openMavenBuildToolPane();
        toolWinPane.find(MavenBuildToolPane.class, Duration.ofSeconds(10)).buildProject("verify", PROJECT_NAME);
        BuildView buildView = toolWinPane.find(BuildView.class, Duration.ofSeconds(10));
        buildView.waitUntilBuildHasFinished();
        assertTrue(buildView.isBuildSuccessful(), "The build should be successful but is not.");
    }
}