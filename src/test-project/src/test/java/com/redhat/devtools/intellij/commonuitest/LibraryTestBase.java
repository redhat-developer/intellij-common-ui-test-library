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
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.utils.constans.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constans.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.testextension.ScreenshotAfterTestFailExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Base class for all JUnit tests in the IntelliJ common UI test library
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
public class LibraryTestBase {
    protected static final Logger LOGGER = Logger.getLogger(LibraryTestBase.class.getName());
    private static final UITestRunner.IdeaVersion ideaVersion = UITestRunner.IdeaVersion.COMMUNITY_V_2022_1;
    protected static RemoteRobot remoteRobot;
    protected static int ideaVersionInt = ideaVersion.toInt();
    private static boolean intelliJHasStarted = false;

    @BeforeAll
    protected static void startIntelliJ() {
        if (!intelliJHasStarted) {
            remoteRobot = UITestRunner.runIde(ideaVersion, 8580);
            intelliJHasStarted = true;
            Runtime.getRuntime().addShutdownHook(new CloseIntelliJBeforeQuit());

            FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
            flatWelcomeFrame.disableNotifications();
            flatWelcomeFrame.preventTipDialogFromOpening();
        }
    }

    @AfterAll
    protected static void finishTestRun() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.clearExceptions();
        flatWelcomeFrame.clearWorkspace();
    }

    protected void prepareWorkspace(String projectName) {
        CreateCloseUtils.createNewProject(remoteRobot, projectName, CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
        mainIdeWindow.closeProject();
    }

    private static class CloseIntelliJBeforeQuit extends Thread {
        @Override
        public void run() {
            UITestRunner.closeIde();
        }
    }
}
