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
package com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.UITestRunner;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.NewProjectDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.screenshot.ScreenshotUtils.takeScreenshot;

/**
 * ScreenshotAfterTestFailExtension takes screenshot immediately after test has failed
 * and perform a clean up to ensure no dialog or windows is opened
 *
 * USAGE: Add the following annotation before every class with JUnit tests
 *        @ExtendWith(ScreenshotAfterTestFailExtension.class)
 *
 * @author zcervink@redhat.com
 *
 */
public class ScreenshotAfterTestFailExtension implements AfterTestExecutionCallback {
    private RemoteRobot remoteRobot;

    public ScreenshotAfterTestFailExtension() {
        this.remoteRobot = UITestRunner.getRemoteRobot();
    }

    /**
     * Take screenshot right after a test fail and perform a clean up to ensure no dialog or windows is opened
     *
     * @param extensionContext test run data
     */
    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        boolean testFailed = extensionContext.getExecutionException().isPresent();
        if (testFailed) {
            takeScreenshot(remoteRobot);
            cleanAfterTestFail();
        }
    }

    private void cleanAfterTestFail() {
        // New Project Dialog is visible -> close it
        try {
            NewProjectDialog newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
            newProjectDialog.cancel();
            return;
        } catch (WaitForConditionTimeoutException e2) {
            // New Project Dialog is not visible -> continue
        }

        // Flat Welcome Frame dialog is visible -> return
        try {
            remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
            return;
        } catch (WaitForConditionTimeoutException e) {
            // Main IDE Window is visible -> close it
            try {
                remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
                MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
                mainIdeWindow.closeProject();
                return;
            } catch (WaitForConditionTimeoutException e2) {
                // Main IDE Window is not visible -> continue
            }
        }
    }
}