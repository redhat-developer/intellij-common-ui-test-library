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
package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Gradle Build Tool Pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = XPathDefinitions.GRADLE_TOOL_WINDOW)
@FixtureName(name = "Tool Windows Pane")
public class GradleBuildToolPane extends AbstractBuildToolPane {

    public GradleBuildToolPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Collapse all
     */
    public void collapseAll() {
        if (ideaVersionInt >= 20242) {
            actionButton(byXpath(XPathDefinitions.MY_ICON_COLLAPSE_ALL_2024_2), Duration.ofSeconds(2)).click();
        } else {
            actionButton(byXpath(XPathDefinitions.MY_ICON_COLLAPSE_ALL_IDE), Duration.ofSeconds(2)).click();
        }
    }

    /**
     * Expand all
     */
    public void expandAll() {
        if (ideaVersionInt >= 20242) {
            actionButton(byXpath(XPathDefinitions.MY_ICON_EXPAND_ALL_2024_2), Duration.ofSeconds(2)).click();
        } else {
            actionButton(byXpath(XPathDefinitions.MY_ICON_EXPAND_ALL_IDE), Duration.ofSeconds(2)).click();
        }
    }

    /**
     * Build the project
     */
    public void buildProject() {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "the Gradle tree to appear in 30 seconds.", this::isTreeVisible);
        expandAll();
        getBuildTree().doubleClickPath(new String[]{"Tasks", "build", "build"}, true);
        remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(2)).find(BuildView.class, Duration.ofSeconds(5)).waitUntilBuildHasFinished();
        remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(30)).waitUntilAllBgTasksFinish();
    }
}