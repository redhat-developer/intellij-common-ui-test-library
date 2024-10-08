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
package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.menubar;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.ActionButtonFixture;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.runner.IntelliJVersion;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Top menu fixture
 *
 * @author zcervink@redhat.com
 */
public class MenuBar {
    private final RemoteRobot remoteRobot;
    private final IntelliJVersion ideaVersion;

    public MenuBar(RemoteRobot remoteRobot) {
        this.remoteRobot = remoteRobot;
        this.ideaVersion = UITestRunner.getIdeaVersion();
    }

    /**
     * Navigate to the location in main IDE menu according to the given path, perform a given mouse action on the last item in the path
     *
     * @param path path to navigate in the main menu
     */
    public void navigateTo(String... path) {
        if (path.length == 0) {
            return;
        }
        if (UITestRunner.getIdeaVersionInt() >= 20242) {
            remoteRobot.find(ActionButtonFixture.class, byXpath(XPathDefinitions.MAIN_MENU)).click();
        }

        JButtonFixture mainMenuFirstItem = mainMenuItem(path[0]);
        if (mainMenuFirstItem != null) {
            if (UITestRunner.getIdeaVersionInt() >= 20242) {
                mainMenuFirstItem.moveMouse();
            } else {
                mainMenuFirstItem.click();
            }

            // Wait for the JPopupMenuFixture to appear
            waitFor(Duration.ofSeconds(5), Duration.ofSeconds(1), "JPopupMenu to appear", () ->
                    !remoteRobot.findAll(JPopupMenuFixture.class, JPopupMenuFixture.Companion.byType()).isEmpty()
            );
        }

        if (path.length == 1) {
            return;
        }

        for (int i = 1; i < path.length - 1; i++) {
            List<JPopupMenuFixture> allContextMenus = remoteRobot.findAll(JPopupMenuFixture.class, JPopupMenuFixture.Companion.byType());
            JPopupMenuFixture lastContextMenu = allContextMenus.get(allContextMenus.size() - 1);
            lastContextMenu.findText((path[i])).moveMouse();
        }

        List<JPopupMenuFixture> allContextMenus = remoteRobot.findAll(JPopupMenuFixture.class, JPopupMenuFixture.Companion.byType());
        JPopupMenuFixture lastContextMenu = allContextMenus.get(allContextMenus.size() - 1);
        lastContextMenu.findText((path[path.length - 1])).click();
    }

    private JButtonFixture mainMenuItem(String label) {
        if (remoteRobot.isMac()) {
            return null;
        }

        CommonContainerFixture cf;
        if (remoteRobot.isLinux()) {
            cf = remoteRobot.find(CommonContainerFixture.class, byXpath(XPathDefinitions.LINUX_MAIN_MENU), Duration.ofSeconds(10));
        } else if (remoteRobot.isWin() && ideaVersion.toInt() >= 20241) {
            cf = remoteRobot.find(CommonContainerFixture.class, byXpath(XPathDefinitions.WINDOWS_MAIN_MENU_2024_1_AND_NEWER), Duration.ofSeconds(10));
        } else if (remoteRobot.isWin() && ideaVersion.toInt() >= 20222) {
            cf = remoteRobot.find(CommonContainerFixture.class, byXpath(XPathDefinitions.WINDOWS_MAIN_MENU_2022_2_TO_2023_2), Duration.ofSeconds(10));
        } else if (remoteRobot.isWin() && ideaVersion.toInt() >= 20203) {
            cf = remoteRobot.find(CommonContainerFixture.class, byXpath(XPathDefinitions.WINDOWS_MAIN_MENU_2020_3_TO_2022_1), Duration.ofSeconds(10));
        } else {
            cf = remoteRobot.find(CommonContainerFixture.class, byXpath(XPathDefinitions.WINDOWS_MAIN_MENU_2020_2_AND_OLDER), Duration.ofSeconds(10));
        }

        return cf.button(byXpath(XPathDefinitions.label(label)), Duration.ofSeconds(10));
    }
}