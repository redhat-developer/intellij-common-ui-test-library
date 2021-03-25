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
package com.redhat.devtools.intellij.commonUiTestLibrary.utils;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.IdeFatalErrorsDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.WelcomeFrameDialog;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Static utilities that assist and simplify manipulation with the IDE and with the project
 *
 * @author zcervink@redhat.com
 */
public class GlobalUtils {
    private static RemoteRobot remoteRobot;

    public static void waitUntilIntelliJStarts(int port) {
        waitFor(Duration.ofSeconds(600), Duration.ofSeconds(3), "The IntelliJ Idea did not start in 10 minutes.", () -> isIntelliJUIVisible(port));
    }

    private static boolean isIntelliJUIVisible(int port) {
        return isHostOnIpAndPortAccessible("127.0.0.1", port);
    }

    private static boolean isHostOnIpAndPortAccessible(String ip, int port) {
        SocketAddress sockaddr = new InetSocketAddress(ip, port);
        Socket socket = new Socket();

        try {
            socket.connect(sockaddr, 10000);
        } catch (IOException IOException) {
            return false;
        }
        return true;
    }

    public static void clearTheWorkspace() {
        step("Delete all the projects in the workspace", () -> {
            // delete all the projects' links from the 'Welcome to IntelliJ IDEA' dialog
            int numberOfLinks = getNumberOfProjectLinks();
            for (int i = 0; i < numberOfLinks; i++) {
                final WelcomeFrameDialog welcomeFrameDialogFixture = remoteRobot.find(WelcomeFrameDialog.class, Duration.ofSeconds(10));
                ComponentFixture cf = welcomeFrameDialogFixture.find(ComponentFixture.class, byXpath("//div[@accessiblename='Recent Projects' and @class='MyList']"));
                cf.runJs("const horizontal_offset = component.getWidth()-22;\n" +
                        "robot.click(component, new Point(horizontal_offset, 22), MouseButton.LEFT_BUTTON, 1);");
            }

            // delete all the files and folders in the IdeaProjects folder
            try {
                String pathToDirToMakeEmpty = System.getProperty("user.home") + File.separator + "IdeaProjects";
                boolean doesTheProjectDirExists = Files.exists(Paths.get(pathToDirToMakeEmpty));
                if (doesTheProjectDirExists) {
                    FileUtils.cleanDirectory(new File(pathToDirToMakeEmpty));
                } else {
                    Files.createDirectory(Paths.get(pathToDirToMakeEmpty));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static RemoteRobot getRemoteRobotConnection(int port) throws InterruptedException {
        remoteRobot = new RemoteRobot("http://127.0.0.1:" + port);
        for (int i = 0; i < 60; i++) {
            try {
                remoteRobot.find(WelcomeFrameDialog.class);
            } catch (Exception ex) {
                Thread.sleep(1000);
            }
        }

        return remoteRobot;
    }

    private static int getNumberOfProjectLinks() {
        final WelcomeFrameDialog welcomeFrameDialogFixture = remoteRobot.find(WelcomeFrameDialog.class, Duration.ofSeconds(10));
        try {
            ComponentFixture cf = welcomeFrameDialogFixture.find(ComponentFixture.class, byXpath("//div[@accessiblename='Recent Projects' and @class='MyList']"));
            int numberOfProjectsLinks = cf.findAllText().size() / 2;    // 2 items per 1 project link (project path and project name)
            return numberOfProjectsLinks;
        } catch (WaitForConditionTimeoutException e) {
            // the list with accessible name 'Recent Projects' is not available -> 0 links in the 'Welcome to IntelliJ IDEA' dialog
            return 0;
        }
    }

    public static void checkForExceptions() {
        step("Check for exceptions and other errors", () -> {
            try {
                final WelcomeFrameDialog welcomeFrameDialogFixture = remoteRobot.find(WelcomeFrameDialog.class, Duration.ofSeconds(10));
                welcomeFrameDialogFixture.ideErrorsIcon().click();
            } catch (WaitForConditionTimeoutException e) {
                e.printStackTrace();
                return;
            }

            final IdeFatalErrorsDialog ideFatalErrorsDialogFixture = remoteRobot.find(IdeFatalErrorsDialog.class, Duration.ofSeconds(10));
            String exceptionNumberLabel = ideFatalErrorsDialogFixture.numberOfExcetionsJBLabel().findAllText().get(0).getText();
            int numberOfExceptions = Integer.parseInt(exceptionNumberLabel.substring(5));

            for (int i = 0; i < numberOfExceptions; i++) {
                String exceptionStackTrace = HelperUtils.listOfRemoteTextToString(ideFatalErrorsDialogFixture.exceptionDescriptionJTextArea().findAllText());

                if (i + 1 < numberOfExceptions) {
                    ideFatalErrorsDialogFixture.nextExceptionButton().click();
                }
            }

            ideFatalErrorsDialogFixture.button("Clear all").click();
        });
    }
}