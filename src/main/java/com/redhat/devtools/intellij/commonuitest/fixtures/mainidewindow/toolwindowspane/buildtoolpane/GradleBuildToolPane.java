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
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.BuildView;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Locale;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Gradle Build Tool Pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = XPathDefinitions.GRADLE_TOOL_WINDOW)
@FixtureName(name = "Tool Windows Pane")
public class GradleBuildToolPane extends CommonContainerFixture {
    private final RemoteRobot remoteRobot;

    public GradleBuildToolPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Reload all Gradle projects
     */
    public void reloadAllGradleProjects() {
        actionButton(byXpath(XPathDefinitions.MY_ICON_REFRESH), Duration.ofSeconds(2)).click();
        remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10)).waitUntilAllBgTasksFinish();
    }

    /**
     * Expand all
     */
    public void expandAll() {
        actionButton(byXpath(XPathDefinitions.MY_ICON_EXPAND_ALL_IDE), Duration.ofSeconds(2)).click();
    }

    /**
     * Collapse all
     */
    public void collapseAll() {
        actionButton(byXpath(XPathDefinitions.MY_ICON_COLLAPSE_ALL_IDE), Duration.ofSeconds(2)).click();
    }

    /**
     * Build the project
     */
    public void buildProject() {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The Gradle tasks tree did not appear in 30 seconds.", this::isGradleTreeVisible);

        // ISSUE #199 - https://github.com/JetBrains/intellij-ui-test-robot/issues/199
        if (remoteRobot.isMac()) {
            expandAll();
        } else {
            gradleTaskTree().expandAll();
        }

        gradleTaskTree().findAllText("build").get(1).click(); // select lifecycle
        gradleTaskTree().findAllText("build").get(1).doubleClick(); // activate lifecycle
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            remoteRobot.find(ToolWindowPane.class).find(BuildView.class).waitUntilBuildHasFinished();
        } else {
            remoteRobot.find(ToolWindowsPane.class).find(BuildView.class).waitUntilBuildHasFinished();
        }
        remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10)).waitUntilAllBgTasksFinish();
    }

    /**
     * Get the Gradle Tab tree fixture
     *
     * @return Gradle Tab tree fixture
     */
    public JTreeFixture gradleTaskTree() {
        return find(JTreeFixture.class, JTreeFixture.Companion.byType(), Duration.ofSeconds(10));
    }

    private boolean isGradleTreeVisible() {
        String treeContent = TextUtils.listOfRemoteTextToString(gradleTaskTree().findAllText());
        return !treeContent.toLowerCase(Locale.ROOT).contains("nothing") && !treeContent.equals("");
    }
}