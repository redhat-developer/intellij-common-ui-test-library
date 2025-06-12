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
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Locale;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Maven Build Tool Pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = XPathDefinitions.MAVEN_TOOL_WINDOW)
@FixtureName(name = "Tool Windows Pane")
public class MavenBuildToolPane extends CommonContainerFixture {
    private final RemoteRobot remoteRobot;
    private final int ideaVersionInt = UITestRunner.getIdeaVersionInt();

    public MavenBuildToolPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Reload all Maven projects
     */
    public void reloadAllMavenProjects() {
        actionButton(byXpath(XPathDefinitions.MY_ICON_REFRESH), Duration.ofSeconds(2)).click();
    }

    /**
     * Collapse all
     */
    public void collapseAll() {
        if (ideaVersionInt >= 20242) {
            actionButton(byXpath(XPathDefinitions.MY_ICON_COLLAPSE_ALL_2024_2), Duration.ofSeconds(2)).click();
        } else {
            actionButton(byXpath(XPathDefinitions.MY_ICON_COLLAPSE_ALL_FOR), Duration.ofSeconds(2)).click();
        }
    }

    /**
     * Build the project
     *
     * @param goal name of the Lifecycle goal you want to invoke (clean, validate, compile, test, package, verify, install, site, deploy)
     */
    public void buildProject(String goal, String projectName) {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The Maven target tree did not appear in 30 seconds.", this::isMavenTreeVisible);
        JTreeFixture tree = mavenTargetTree();
        // below workaround due to https://github.com/JetBrains/intellij-ui-test-robot/issues/459
        tree.doubleClickRowWithText(projectName, true); // expand root
        tree.doubleClickRowWithText("Lifecycle", true); // expand Lifecycle
        tree.doubleClickRowWithText(goal, true);
        remoteRobot.find(ToolWindowPane.class).find(BuildView.class).waitUntilBuildHasFinished();
        remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(30)).waitUntilAllBgTasksFinish();
    }

    /**
     * Get the Maven Tab tree fixture
     *
     * @return Maven Tab tree fixture
     */
    public JTreeFixture mavenTargetTree() {
        return find(JTreeFixture.class, JTreeFixture.Companion.byType(), Duration.ofSeconds(10));
    }

    private boolean isMavenTreeVisible() {
        String treeContent = TextUtils.listOfRemoteTextToString(mavenTargetTree().findAllText());
        return !treeContent.toLowerCase(Locale.ROOT).contains("nothing") && !treeContent.isEmpty();
    }
}