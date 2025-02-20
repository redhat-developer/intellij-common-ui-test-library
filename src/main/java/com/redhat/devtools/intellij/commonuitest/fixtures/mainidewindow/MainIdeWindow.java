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
package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.utils.Keyboard;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.navigation.SearchEverywherePopup;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.menubar.MenuBar;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.time.Duration;

import com.redhat.devtools.intellij.commonuitest.utils.internalerror.IdeInternalErrorUtils;

/**
 * Main IDE window fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "IdeFrameImpl type", xpath = XPathDefinitions.MAIN_IDE_WINDOW)
@FixtureName(name = "Main IDE Window")
public class MainIdeWindow extends CommonContainerFixture {
    private final RemoteRobot remoteRobot;

    public MainIdeWindow(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Maximize the main IDE window
     */
    public void maximizeIdeWindow() {
        if (remoteRobot.isWin()) {
            runJs("""
                    const width = component.getWidth();
                    const height = component.getHeight();
                    const horizontal_offset = width-72;
                    robot.click(component, new Point(horizontal_offset, 14), MouseButton.LEFT_BUTTON, 1);
                    const width_after = component.getWidth();
                    const height_after = component.getHeight();
                    const horizontal_offset_after = width_after-72;
                    if (width > width_after || height > height_after) { robot.click(component, new Point(horizontal_offset_after, 14), MouseButton.LEFT_BUTTON, 1); }""");

        } else {
            runJs("""
                const width = component.getWidth();
                const height = component.getHeight();
                const horizontal_offset = width/2;
                robot.click(component, new Point(horizontal_offset, 10), MouseButton.LEFT_BUTTON, 2);
                const width_after = component.getWidth();
                const height_after = component.getHeight();
                const horizontal_offset_after = width_after/2;
                if (width > width_after || height > height_after) { robot.click(component, new Point(horizontal_offset_after, 10), MouseButton.LEFT_BUTTON, 2); }""");
        }
    }

    /**
     * Close the currently opened project
     */
    public void closeProject() {
        if (remoteRobot.isWin()) {
            new MenuBar(remoteRobot).navigateTo("File", "Close Project");
        } else {
            invokeCmdUsingSearchEverywherePopup("Close Project");
        }
        IdeInternalErrorUtils.clearWindowsErrorsIfTheyAppear(remoteRobot);
        remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10)).runJs("const horizontal_offset = component.getWidth()/2;\n" +
                "robot.click(component, new Point(horizontal_offset, 10), MouseButton.LEFT_BUTTON, 1);");
    }

    /**
     * Invoke a command using the Search Everywhere popup
     *
     * @param cmdToInvoke String representation of command which will be executed using the Search Everywhere popup
     */
    public void invokeCmdUsingSearchEverywherePopup(String cmdToInvoke) {
        SearchEverywherePopup searchEverywherePopup = openSearchEverywherePopup();
        searchEverywherePopup.invokeCmd(cmdToInvoke);
    }

    private SearchEverywherePopup openSearchEverywherePopup() {
        try {
            SearchEverywherePopup searchEverywherePopup = find(SearchEverywherePopup.class, Duration.ofSeconds(10));
            searchEverywherePopup.activateTab("All");
            return searchEverywherePopup;
        } catch (WaitForConditionTimeoutException e) {
            Keyboard keyboard = new Keyboard(remoteRobot);
            if (remoteRobot.isMac()) {
                keyboard.hotKey(KeyEvent.VK_META, KeyEvent.VK_O);
            } else {
                keyboard.hotKey(KeyEvent.VK_CONTROL, KeyEvent.VK_N);
            }
            SearchEverywherePopup searchEverywherePopup = find(SearchEverywherePopup.class, Duration.ofSeconds(10));
            searchEverywherePopup.activateTab("All");
            return searchEverywherePopup;
        }
    }
}