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
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.utils.build.BuildUtils;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
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
    private static ToolWindowPane toolWinPane;

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, NewProjectType.MAVEN);
        toolWinPane = remoteRobot.find(ToolWindowPane.class, UITestTimeouts.FIXTURE_TIMEOUT);
    }

    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void waitForSuccessfulMavenBuildTest() {
        BuildUtils.buildMavenProjectAndWaitForFinish(remoteRobot, PROJECT_NAME, "verify");
        BuildView buildView = toolWinPane.find(BuildView.class, UITestTimeouts.FIXTURE_TIMEOUT);
        assertTrue(buildView.isBuildSuccessful(), "The build should be successful but is not.");
    }
}