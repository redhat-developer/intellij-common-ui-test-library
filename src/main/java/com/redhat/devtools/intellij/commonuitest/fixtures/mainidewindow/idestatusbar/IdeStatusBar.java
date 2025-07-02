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
package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Bottom status bar fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "IdeStatusBarImpl type", xpath = XPathDefinitions.IDE_STATUS_BAR)
@FixtureName(name = "Ide Status Bar")
public class IdeStatusBar extends CommonContainerFixture {

    public IdeStatusBar(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Create fixture for the InlineProgressPanel
     *
     * @return fixture for the InlineProgressPanel
     */
    public ComponentFixture inlineProgressPanel() {
        return find(ComponentFixture.class, byXpath(XPathDefinitions.INLINE_PROGRESS_PANEL), Duration.ofSeconds(5));
    }

    /**
     * Wait for 5 minutes until all the background tasks finish
     */
    public void waitUntilAllBgTasksFinish() {
        waitUntilAllBgTasksFinish(300);
    }

    /**
     * Wait until all the background tasks finish
     */
    public void waitUntilAllBgTasksFinish(int timeout) {
        waitFor(Duration.ofSeconds(timeout), Duration.ofSeconds(10), "the background tasks to finish in " + timeout + " seconds.", this::didAllBgTasksFinish);
    }

    private boolean didAllBgTasksFinish() {
        if (isShowing()) {
            return inlineProgressPanel().findAllText().isEmpty();
        }
        return false;
    }
}