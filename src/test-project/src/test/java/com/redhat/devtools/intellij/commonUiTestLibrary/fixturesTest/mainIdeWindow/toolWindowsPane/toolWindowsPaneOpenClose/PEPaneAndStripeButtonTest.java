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

import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ProjectExplorer;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project Explorer Tool Windows Pane test
 *
 * @author zcervink@redhat.com
 */
class PEPaneAndStripeButtonTest extends AbstractToolWindowsPaneTest {
    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, mavenProjectName, CreateCloseUtils.NewProjectType.MAVEN);
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
    }

    @BeforeEach
    public void preparePanes() {
        if (isPaneOpened(ProjectExplorer.class)) {
            closePane(ButtonLabels.projectStripeButtonLabel, ProjectExplorer.class);
        }
    }

    @Test
    public void projectExplorerPaneOpenCloseTest() {
        step("@Test - open and close the Project Explorer", () -> {
            toolWindowsPane.openProjectExplorer();
            assertTrue(isPaneOpened(ProjectExplorer.class), "The 'Project Explorer' should be opened but is closed.");
            toolWindowsPane.closeProjectExplorer();
            assertFalse(isPaneOpened(ProjectExplorer.class), "The 'Project Explorer' should be closed but is opened.");
        });
    }

    @Test
    public void stripeButtonTest() {
        step("@Test - create stripe button fixture", () -> {
            try {
                toolWindowsPane.stripeButton(ButtonLabels.mavenStripeButtonLabel, false);
            } catch (WaitForConditionTimeoutException e) {
                fail(e.getMessage());
            }
        });
    }
}