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
import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Bottom status bar fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "IdeStatusBarImpl type", xpath = "//div[@class='IdeStatusBarImpl']")
@FixtureName(name = "Ide Status Bar")
public class IdeStatusBar extends CommonContainerFixture {
    private static final Logger LOGGER = Logger.getLogger(IdeStatusBar.class.getName());
    private RemoteRobot remoteRobot;

    public IdeStatusBar(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Create fixture for the InlineProgressPanel
     *
     * @return fixture for the InlineProgressPanel
     */
    public ComponentFixture inlineProgressPanel() {
        return find(ComponentFixture.class, byXpath("//div[@class='InlineProgressPanel']"));
    }

    /**
     * Wait until the project has finished the import
     */
    public void waitUntilProjectImportIsComplete() {
        waitFor(Duration.ofSeconds(300), Duration.ofSeconds(5), "The project import did not finish in 5 minutes.", this::didProjectImportFinish);
    }

    /**
     * Wait until all the background tasks finish
     */
    public void waitUntilAllBgTasksFinish() {
        waitFor(Duration.ofSeconds(300), Duration.ofSeconds(10), "The background tasks did not finish in 5 minutes.", this::didAllBgTasksFinish);
    }

    private boolean didProjectImportFinish() {
        try {
            find(ComponentFixture.class, byXpath("//div[@class='EngravedLabel']"), Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            return true;
        }
        return false;
    }

    private boolean didAllBgTasksFinish() {
        for (int i = 0; i < 5; i++) {
            IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class);
            List<RemoteText> inlineProgressPanelContent = ideStatusBar.inlineProgressPanel().findAllText();
            if (!inlineProgressPanelContent.isEmpty()) {
                return false;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }
}