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
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;

import java.time.Duration;

import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Abstract ToolWindowsPane test
 *
 * @author zcervink@redhat.com
 */
abstract class AbstractToolWindowsPaneTest extends LibraryTestBase {
    protected static final String mavenProjectName = "tool_windows_pane_java_maven_project";
    protected static final String gradleProjectName = "tool_windows_pane_java_gradle_project";
    protected static ToolWindowsPane toolWindowsPane;

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    protected void closePane(String label, Class fixtureClass) {
        if (isPaneOpened(fixtureClass)) {
            clickOnStripeButton(label, true);
        }
    }

    protected boolean isPaneOpened(Class fixtureClass) {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        try {
            toolWindowsPane.find(fixtureClass, Duration.ofSeconds(5));
            return true;
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
    }

    protected void clickOnStripeButton(String label, boolean isPaneOpened) {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The '" + label + "' stripe button is not available.", () -> isStripeButtonAvailable(label, isPaneOpened));
        toolWindowsPane.stripeButton(label, isPaneOpened).click();
    }

    protected boolean isStripeButtonAvailable(String label, boolean isPaneOpened) {
        try {
            toolWindowsPane.stripeButton(label, isPaneOpened);
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }
}