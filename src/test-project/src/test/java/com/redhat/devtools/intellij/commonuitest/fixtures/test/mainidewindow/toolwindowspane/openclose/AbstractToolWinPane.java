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

import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;

import java.time.Duration;

import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Abstract ToolWindowsPane test
 *
 * @author zcervink@redhat.com
 */
abstract class AbstractToolWinPane extends LibraryTestBase {
    protected static final String MAVEN_PROJECT_NAME = "tool_windows_pane_java_maven_project";
    protected static final String GRADLE_PROJECT_NAME = "tool_windows_pane_java_gradle_project";
    protected static com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.AbstractToolWinPane toolWinPane;

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    protected static boolean isPaneOpened(Class<? extends CommonContainerFixture> fixtureClass) {
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        } else {
            toolWinPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        }

        try {
            toolWinPane.find(fixtureClass, Duration.ofSeconds(5));
            return true;
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
    }

    protected void closePane(String label, Class<? extends CommonContainerFixture> fixtureClass) {
        if (isPaneOpened(fixtureClass)) {
            clickOnStripeButton(label, true);
        }
    }

    protected void clickOnStripeButton(String label, boolean isPaneOpened) {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The '" + label + "' stripe button is not available.", () -> isStripeButtonAvailable(label, isPaneOpened));
        toolWinPane.stripeButton(label, isPaneOpened).click();
    }

    protected boolean isStripeButtonAvailable(String label, boolean isPaneOpened) {
        try {
            toolWinPane.stripeButton(label, isPaneOpened);
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }
}