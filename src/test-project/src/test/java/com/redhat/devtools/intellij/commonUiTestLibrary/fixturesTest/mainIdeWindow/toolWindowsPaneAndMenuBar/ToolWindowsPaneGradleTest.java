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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane.ToolToBuildProject.GRADLE;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.gradleStripeButtonLabel;

/**
 * ToolWindowsPane Gradle test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
class ToolWindowsPaneGradleTest extends LibraryTestBase {
    private static final String projectName = "tool_windows_pane_java_gradle_project";

    @BeforeAll
    public static void prepareProject() {
        createNewProject(projectName, gradleStripeButtonLabel);
    }

    @AfterAll
    public static void closeCurrentProject() {
        closeProject();
    }

    @Test
    public void gradleBuildTest() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        toolWindowsPane.buildProject(GRADLE);
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilAllBgTasksFinish();
        toolWindowsPane.testIfBuildIsSuccessful();
    }

    @Test
    public void stripeButtonTest() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        toolWindowsPane.stripeButton(gradleStripeButtonLabel);
    }
}