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
import com.intellij.remoterobot.utils.Keyboard;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Maven Build Tool Pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = XPathDefinitions.MAVEN_TOOL_WINDOW)
@FixtureName(name = "Maven Tool Window Pane")
public class MavenBuildToolPane extends AbstractBuildToolPane {

    public MavenBuildToolPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
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
     * Expand all
     */
    public void expandAll() {
        // MavenToolPane has no "Expand All" button (https://github.com/redhat-developer/intellij-common-ui-test-library/issues/405)
        if (!getBuildTree().getHasFocus()) {
            getBuildTree().click();
        }
        Keyboard keyboard = new Keyboard(remoteRobot);
        int current;
        int iteration = 0;

        if (remoteRobot.isMac()) {
            // On macOS, use asterisk key
            do {
                current = getBuildTree().collectRows().size();
                keyboard.key(KeyEvent.VK_MULTIPLY);
                iteration++;
            } while (getBuildTree().collectRows().size() > current && iteration < 10);
        } else {
            // On Linux/Windows, use Ctrl+ADD
            do {
                current = getBuildTree().collectRows().size();
                keyboard.hotKey(KeyEvent.VK_CONTROL, KeyEvent.VK_ADD);
                iteration++;
            } while (getBuildTree().collectRows().size() > current && iteration < 10);
        }
    }

    /**
     * Build the project
     *
     * @param goal name of the Lifecycle goal you want to invoke (clean, validate, compile, test, package, verify, install, site, deploy)
     */
    public void buildProject(String goal, String projectName) {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "the Maven tree to appear.", this::isTreeVisible);
        // below workaround due to https://github.com/JetBrains/intellij-ui-test-robot/issues/459
        expandAll();
        getBuildTree().doubleClickPath(new String[]{projectName, "Lifecycle", goal}, true);
    }

}