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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.mainidewindow.toolwindowspane.openclose;

import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ProjectExplorer;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.utils.constans.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

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
        CreateCloseUtils.createNewProject(remoteRobot, MAVEN_PROJECT_NAME, CreateCloseUtils.NewProjectType.MAVEN);
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
    }

    @BeforeEach
    public void preparePanes() {
        if (isPaneOpened(ProjectExplorer.class)) {
            closePane(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL, ProjectExplorer.class);
        }
    }

    @Test
    public void projectExplorerPaneOpenCloseTest() {
        toolWindowsPane.openProjectExplorer();
        assertTrue(isPaneOpened(ProjectExplorer.class), "The 'Project Explorer' should be opened but is closed.");
        toolWindowsPane.closeProjectExplorer();
        assertFalse(isPaneOpened(ProjectExplorer.class), "The 'Project Explorer' should be closed but is opened.");
    }

    @Test
    public void stripeButtonTest() {
        try {
            toolWindowsPane.stripeButton(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, false);
        } catch (WaitForConditionTimeoutException e) {
            fail(e.getMessage());
        }
    }
}