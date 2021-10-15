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
package com.redhat.devtools.intellij.commonUiTestLibrary;

import com.intellij.remoterobot.RemoteRobot;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * Base class for all JUnit tests in the IntelliJ common UI test library
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
public class LibraryTestBase {
    protected static RemoteRobot remoteRobot;
    private static boolean intelliJHasStarted = false;

    @BeforeAll
    protected static void startIntelliJ() {
        step("Start IntelliJ IDEA for UI tests before executing the first test", () -> {
            if (!intelliJHasStarted) {
                remoteRobot = UITestRunner.runIde(UITestRunner.IdeaVersion.V_2020_3, 8580);
                intelliJHasStarted = true;
                Runtime.getRuntime().addShutdownHook(new CloseIntelliJBeforeQuit());
            }
        });
    }

    @AfterAll
    protected static void finishTestRun() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.clearExceptions();
        flatWelcomeFrame.clearWorkspace();
    }

    private static class CloseIntelliJBeforeQuit extends Thread {
        public void run() {
            UITestRunner.closeIde();
        }
    }
}
