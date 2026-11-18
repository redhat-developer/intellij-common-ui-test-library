/*******************************************************************************
 * Copyright (c) 2025 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.utils.build;

import com.intellij.remoterobot.RemoteRobot;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;

import java.time.Duration;

/**
 * Build project utility class
 */
public final class BuildUtils {

    private BuildUtils() {throw new UITestException("Utility class with static methods.");}

    /**
     * Build a Maven project and wait for the result to be successful
     *
     * @param remoteRobot reference to the RemoteRobot instance
     * @param projectName the name of the project
     * @param goal        the maven goal
     */
    public static void buildMavenProjectAndWaitForFinish(RemoteRobot remoteRobot, String projectName, String goal) {
        ToolWindowPane toolWinPane = remoteRobot.find(ToolWindowPane.class, UITestTimeouts.FIXTURE_TIMEOUT);
        toolWinPane.openMavenBuildToolPane();
        MavenBuildToolPane mavenBuildToolPane = toolWinPane.find(MavenBuildToolPane.class, UITestTimeouts.FIXTURE_TIMEOUT);
        mavenBuildToolPane.buildProject(goal, projectName);
        toolWinPane.find(BuildView.class, UITestTimeouts.SHORT_TIMEOUT).waitUntilBuildHasFinished();
        remoteRobot.find(IdeStatusBar.class, UITestTimeouts.LONG_TIMEOUT).waitUntilAllBgTasksFinish();
    }
}
