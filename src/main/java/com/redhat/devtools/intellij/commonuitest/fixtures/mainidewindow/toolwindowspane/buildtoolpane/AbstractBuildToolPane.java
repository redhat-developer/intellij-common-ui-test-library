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
package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Locale;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

public abstract class AbstractBuildToolPane extends CommonContainerFixture {

    protected final RemoteRobot remoteRobot;
    protected final int ideaVersionInt = UITestRunner.getIdeaVersionInt();

    protected AbstractBuildToolPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Reload all projects
     */
    public void reloadAllProjects() {
        actionButton(byXpath(XPathDefinitions.MY_ICON_REFRESH), UITestTimeouts.QUICK_TIMEOUT).click();
        remoteRobot.find(IdeStatusBar.class, UITestTimeouts.FIXTURE_TIMEOUT).waitUntilAllBgTasksFinish();
    }

    /**
     * Get the build tree fixture
     *
     * @return build tree fixture
     */
    public JTreeFixture getBuildTree() {
        return find(JTreeFixture.class, JTreeFixture.Companion.byType(), UITestTimeouts.FIXTURE_TIMEOUT);
    }

    protected boolean isTreeVisible() {
        String treeContent = TextUtils.listOfRemoteTextToString(getBuildTree().findAllText());
        return !treeContent.toLowerCase(Locale.ROOT).contains("nothing") && !treeContent.isEmpty();
    }
}
