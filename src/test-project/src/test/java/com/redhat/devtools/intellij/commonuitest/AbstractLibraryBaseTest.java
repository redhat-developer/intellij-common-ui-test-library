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
package com.redhat.devtools.intellij.commonuitest;

import com.intellij.remoterobot.RemoteRobot;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import com.redhat.devtools.intellij.commonuitest.utils.runner.IntelliJVersion;
import com.redhat.devtools.intellij.commonuitest.utils.testextension.ScreenshotAfterTestFailExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * Base class for all JUnit tests in the IntelliJ common UI test library
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
public abstract class AbstractLibraryBaseTest {
    protected static final Logger LOGGER = Logger.getLogger(AbstractLibraryBaseTest.class.getName());
    private static final IntelliJVersion communityIdeaVersion = IntelliJVersion.getFromStringVersion(System.getProperty("communityIdeaVersion"));
    protected static RemoteRobot remoteRobot;
    protected static int ideaVersionInt;
    private static boolean intelliJHasStarted = false;

    @BeforeAll
    static void startIntelliJ() {
        if (!intelliJHasStarted) {
            ideaVersionInt = communityIdeaVersion.toInt();
            remoteRobot = UITestRunner.runIde(communityIdeaVersion);

            intelliJHasStarted = true;
            Runtime.getRuntime().addShutdownHook(new CloseIntelliJBeforeQuit());

            FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, UITestTimeouts.FIXTURE_TIMEOUT);
            flatWelcomeFrame.clearWorkspace();
            flatWelcomeFrame.disableNotifications();
        }
    }

    @AfterAll
    static void finishTestRun() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, UITestTimeouts.FIXTURE_TIMEOUT);
        flatWelcomeFrame.clearExceptions();
        flatWelcomeFrame.clearWorkspace();
    }

    protected void prepareWorkspace(String projectName) {
        CreateCloseUtils.createNewProject(remoteRobot, projectName, NewProjectType.PLAIN_JAVA);
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, UITestTimeouts.FIXTURE_TIMEOUT);
        mainIdeWindow.closeProject();
    }

    private static class CloseIntelliJBeforeQuit extends Thread {
        @Override
        public void run() {
            UITestRunner.closeIde();
        }
    }
}
