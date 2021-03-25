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
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.GlobalUtils;

import java.io.IOException;

/**
 * Basic methods for starting and quiting the IntelliJ Idea IDE for UI tests
 *
 * @author zcervink@redhat.com
 */
public class IntegrationTestsUtils {
    private static final int defaultPort = 8580;
    private static RemoteRobot robot = null;
    private static Process ideProcess;

    public static RemoteRobot runIde(String ideaVersion, int port) {
        ProcessBuilder pb = new ProcessBuilder("./gradlew", "runIdeForUiTests", "-PideaVersion=" + ideaVersion, "-Drobot-server.port=" + port);
        try {
            ideProcess = pb.start();
            GlobalUtils.waitUntilIntelliJStarts(port);
            robot = GlobalUtils.getRemoteRobotConnection(port);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        GlobalUtils.clearTheWorkspace();
        return robot;
    }

    public static RemoteRobot runIde(String ideaVersion) {
        return runIde(ideaVersion, defaultPort);
    }

    public static void closeIde() {
        ideProcess.destroy();
    }
}