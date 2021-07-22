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
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.ContainerFixture;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.redhat.devtools.intellij.commonUiTestLibrary.UITestRunner.getIntelliJIdeaVersion;

/**
 * Top menu fixture
 *
 * @author zcervink@redhat.com
 */
public class MenuBar {
    private RemoteRobot remoteRobot;
    private final int ideaVersion = getIntelliJIdeaVersion();

    public MenuBar(RemoteRobot remoteRobot) {
        this.remoteRobot = remoteRobot;
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
            List<ContainerFixture> allContextMenus = remoteRobot.findAll(ContainerFixture.class, byXpath("//div[@class='HeavyWeightWindow']"));
            ContainerFixture lastContextMenu = allContextMenus.get(allContextMenus.size() - 1);
            lastContextMenu.findText((path[i])).moveMouse();
        }

        List<ContainerFixture> allContextMenus = remoteRobot.findAll(ContainerFixture.class, byXpath("//div[@class='HeavyWeightWindow']"));
        ContainerFixture lastContextMenu = allContextMenus.get(allContextMenus.size() - 1);
        lastContextMenu.findText((path[path.length - 1])).click();
    }

    /**
     * Create fixture for main menu item
     *
     * @param label label text
     */
    private ComponentFixture mainMenuItem(String label) {
        if (remoteRobot.isLinux()) {
            ContainerFixture cf = remoteRobot.find(ContainerFixture.class, byXpath("//div[@class='LinuxIdeMenuBar']"), Duration.ofSeconds(10));
            return cf.find(ComponentFixture.class, byXpath("//div[@accessiblename='" + label + "' and @class='ActionMenu' and @text='" + label + "']"), Duration.ofSeconds(10));
        } else if (remoteRobot.isWin()) {
            if (ideaVersion >= 203) {
                ContainerFixture cf = remoteRobot.find(ContainerFixture.class, byXpath("//div[@class='MenuFrameHeader']"), Duration.ofSeconds(10));
                return cf.find(ComponentFixture.class, byXpath("//div[@text='" + label + "']"), Duration.ofSeconds(10));
            } else {
                ContainerFixture cf = remoteRobot.find(ContainerFixture.class, byXpath("//div[@class='CustomHeaderMenuBar']"), Duration.ofSeconds(10));
                return cf.find(ComponentFixture.class, byXpath("//div[@accessiblename='" + label + "' and @class='ActionMenu' and @text='" + label + "']"), Duration.ofSeconds(10));
            }
        }
        return null;
    }
}