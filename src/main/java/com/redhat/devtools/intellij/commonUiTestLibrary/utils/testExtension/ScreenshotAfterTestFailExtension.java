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
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.screenshot.ScreenshotUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * ScreenshotAfterTestFailExtension takes screenshot immediately after test has failed
 * and perform a clean up to ensure no dialog or windows is opened<br>
 *
 * <b>USAGE:</b> Add the following annotation before every class with JUnit tests:
 * {@code @ExtendWith(ScreenshotAfterTestFailExtension.class)}<br>
 *
 * @author zcervink@redhat.com
 */
public class ScreenshotAfterTestFailExtension implements AfterTestExecutionCallback {
    private RemoteRobot remoteRobot;

    public ScreenshotAfterTestFailExtension() {
        this.remoteRobot = UITestRunner.getRemoteRobot();
    }

    /**
     * Take screenshot right after a test has failed and perform a clean up to ensure no dialog or window is opened
     *
     * @param extensionContext test run data
     */
    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        boolean testFailed = extensionContext.getExecutionException().isPresent();
        if (testFailed) {
            step("Take a screenshot after a test has failed", () -> {
                ScreenshotUtils.takeScreenshot(remoteRobot);
            });
            step("Return to the 'Welcome Frame' dialog", () -> {
                cleanAfterTestFail();
            });
        }
    }

    private void cleanAfterTestFail() {
        // New Project Dialog is visible -> close it
        try {
            NewProjectDialogWizard newProjectDialogWizard = remoteRobot.find(NewProjectDialogWizard.class, Duration.ofSeconds(10));
            newProjectDialogWizard.cancel();
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
                remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10)).closeProject();
                return;
            } catch (WaitForConditionTimeoutException e2) {
                // Main IDE Window is not visible -> continue
            }
        }
    }
}