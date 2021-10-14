/*******************************************************************************
 * Copyright (c) 2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.BuildView;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils;
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
@DefaultXpath(by = "ToolWindowsPane type", xpath = "//div[@accessiblename='Gradle Tool Window']")
@FixtureName(name = "Tool Windows Pane")
public class GradleBuildToolPane extends CommonContainerFixture {
    private RemoteRobot remoteRobot;

    public GradleBuildToolPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Reload all Gradle projects
     */
    public void reloadAllGradleProjects() {
        actionButton(byXpath("//div[@myicon='refresh.svg']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Expand all
     */
    public void expandAll() {
        actionButton(byXpath("//div[contains(@myvisibleactions, 'IDE')]//div[@myicon='expandall.svg']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Collapse all
     */
    public void collapseAll() {
        actionButton(byXpath("//div[contains(@myvisibleactions, 'IDE')]//div[@myicon='collapseall.svg']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Build the project
     */
    public void buildProject() {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The Gradle tasks tree did not appear in 30 seconds.", () -> isGradleTreeVisible());
        actionButton(byXpath("//div[contains(@myvisibleactions, 'IDE')]//div[@myicon='expandall.svg']"), Duration.ofSeconds(10)).click();
        gradleTaskTree().findAllText("build").get(1).doubleClick();
        remoteRobot.find(ToolWindowsPane.class).find(BuildView.class).waitUntilBuildHasFinished();
    }

    /**
     * Get the Gradle Tab tree fixture
     *
     * @return Gradle Tab tree fixture
     */
    public JTreeFixture gradleTaskTree() {
        return find(JTreeFixture.class, JTreeFixture.Companion.byType(), Duration.ofSeconds(10));
    }

    private void expandGradleTasksTreeIfNecessary() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        String labels = TextUtils.listOfRemoteTextToString(gradleTaskTree().findAllText());
        // if the Gradle tasks tree is collapsed -> expand it
        if (!labels.contains("Tasks")) {
            gradleTaskTree().findText(labels).doubleClick();
        }
    }

    private boolean isGradleTreeVisible() {
        String treeContent = TextUtils.listOfRemoteTextToString(gradleTaskTree().findAllText());
        return !treeContent.toLowerCase(Locale.ROOT).contains("nothing") && !treeContent.equals("");
    }
}