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
package com.redhat.devtools.intellij.commonuitest.utils.testextension;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.utils.screenshot.ScreenshotUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * ScreenshotAfterTestFailExtension takes screenshot immediately after test has failed
 * and perform a cleanup to ensure no dialog or windows is opened<br>
 *
 * <b>USAGE:</b> Add the following annotation before every class with JUnit tests:
 * {@code @ExtendWith(ScreenshotAfterTestFailExtension.class)}<br>
 *
 * @author zcervink@redhat.com
 */
public class ScreenshotAfterTestFailExtension implements AfterTestExecutionCallback {
    protected static final Logger LOGGER = Logger.getLogger(ScreenshotAfterTestFailExtension.class.getName());

    /**
     * Take screenshot right after a test has failed and perform a cleanup to ensure no dialog or window is opened
     *
     * @param extensionContext test run data
     */
    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        RemoteRobot remoteRobot = UITestRunner.getRemoteRobot();
        boolean testFailed = extensionContext.getExecutionException().isPresent();
        if (testFailed) {
            if (remoteRobot == null) {
                LOGGER.log(Level.SEVERE, "Can't take a screenshot, remoteRobot is null!");
                return;
            }
            String testClass = extensionContext.getRequiredTestClass().getName();
            String testMethod = extensionContext.getRequiredTestMethod().getName();
            step("Take a screenshot after a test has failed",
                () -> ScreenshotUtils.takeScreenshot(remoteRobot, testClass + "_" + testMethod)
            );
            step("Return to the 'Welcome Frame' dialog",
                () -> cleanAfterTestFail(remoteRobot)
            );
        }
    }

    private void cleanAfterTestFail(RemoteRobot remoteRobot) {
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
        } catch (WaitForConditionTimeoutException e) {
            // Main IDE Window is visible -> close it
            try {
                remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10)).closeProject();
            } catch (WaitForConditionTimeoutException e2) {
                // Main IDE Window is not visible -> continue
            }
        }
    }
}