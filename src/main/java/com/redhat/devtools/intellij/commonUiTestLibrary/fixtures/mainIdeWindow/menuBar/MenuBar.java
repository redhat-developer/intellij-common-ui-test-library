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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.menuBar;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.redhat.devtools.intellij.commonUiTestLibrary.UITestRunner;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;


/**
 * Top menu fixture
 *
 * @author zcervink@redhat.com
 */
public class MenuBar {
    private RemoteRobot remoteRobot;
    private UITestRunner.IdeaVersion ideaVersion;

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

        mainMenuItem(path[0]).click();

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
            cf = remoteRobot.find(CommonContainerFixture.class, byXpath("//div[@class='LinuxIdeMenuBar']"), Duration.ofSeconds(10));
        } else if (remoteRobot.isWin() && ideaVersion.toInt() >= 20203) {
            cf = remoteRobot.find(CommonContainerFixture.class, byXpath("//div[@class='MenuFrameHeader']"), Duration.ofSeconds(10));
        } else {
            cf = remoteRobot.find(CommonContainerFixture.class, byXpath("//div[@class='CustomHeaderMenuBar']"), Duration.ofSeconds(10));
        }

        return cf.button(byXpath("//div[@text='" + label + "']"), Duration.ofSeconds(10));
    }
}