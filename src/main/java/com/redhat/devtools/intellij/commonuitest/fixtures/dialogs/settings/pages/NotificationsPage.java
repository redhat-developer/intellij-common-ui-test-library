/*******************************************************************************
 * Copyright (c) 2022 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JCheckboxFixture;
import org.jetbrains.annotations.NotNull;

/**
 * Notifications page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='DialogRootPane']")
@FixtureName(name = "Notifications Page")
public class NotificationsPage extends CommonContainerFixture {
    public NotificationsPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Disable both balloon and system notifications
     */
    public void disableNotifications() {
        disableBalloonNotifications();
        disableSystemNotifications();
    }

    /**
     * Disable balloon notifications
     */
    public void disableBalloonNotifications() {
        displayBalloonNotificationsCheckBox().setValue(false);
    }

    /**
     * Disable system notifications
     */
    public void disableSystemNotifications() {
        displaySystemNotificationsCheckBox().setValue(false);
    }

    /**
     * Get the 'Display balloon notifications' checkbox fixture
     *
     * @return checkbox fixture
     */
    public JCheckboxFixture displayBalloonNotificationsCheckBox() {
        return checkBox("Display balloon notifications", true);
    }

    /**
     * Get the 'Enable system notifications' checkbox fixture
     *
     * @return checkbox fixture
     */
    public JCheckboxFixture displaySystemNotificationsCheckBox() {
        return checkBox("Enable system notifications", true);
    }
}