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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.mainIdeWindow.toolWindowsPane.toolWindowsPaneOpenClose;

import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Gradle Tool Windows Pane test
 *
 * @author zcervink@redhat.com
 */
class GradlePaneTest extends AbstractToolWindowsPaneTest {
    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, gradleProjectName, CreateCloseUtils.NewProjectType.GRADLE);
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
    }

    @BeforeEach
    public void preparePanes() {
        if (isPaneOpened(GradleBuildToolPane.class)) {
            closePane(ButtonLabels.gradleStripeButtonLabel, GradleBuildToolPane.class);
        }
    }

    @Test
    public void gradleBuildToolPaneOpenCloseTest() {
        step("@Test - open and close the gradle build tool pane", () -> {
            toolWindowsPane.openGradleBuildToolPane();
            assertTrue(isPaneOpened(GradleBuildToolPane.class), "The 'Gradle Build Tool Pane' should be opened but is closed.");
            toolWindowsPane.closeGradleBuildToolPane();
            assertFalse(isPaneOpened(GradleBuildToolPane.class), "The 'Gradle Build Tool Pane' should be closed but is opened.");
        });
    }
}