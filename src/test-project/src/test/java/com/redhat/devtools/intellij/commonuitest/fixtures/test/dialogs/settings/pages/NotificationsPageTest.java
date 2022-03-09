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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.dialogs.settings.pages;

import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings.SettingsDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings.pages.NotificationsPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NotificationsPage test
 *
 * @author zcervink@redhat.com
 */
class NotificationsPageTest extends LibraryTestBase {
    private static SettingsDialog settingsDialog;
    private static NotificationsPage notificationsPage;

    @BeforeAll
    public static void openSettingsDialog() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.openSettingsDialog();
        settingsDialog = remoteRobot.find(SettingsDialog.class, Duration.ofSeconds(5));
        settingsDialog.navigateTo("Appearance & Behavior", "Notifications");
        notificationsPage = remoteRobot.find(NotificationsPage.class, Duration.ofSeconds(5));
    }

    @AfterAll
    public static void closeSettingsDialog() {
        settingsDialog.cancel();
    }

    @Test
    public void disableNotificationsTest() {
        notificationsPage.displayBalloonNotificationsCheckBox().setValue(true);
        notificationsPage.displaySystemNotificationsCheckBox().setValue(true);
        boolean balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        boolean systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertTrue(balloonNotificationsEnabled && systemNotificationsEnabled, "The 'Balloon Notifications' and the 'System Notifications' checkboxes should be both checked.");

        notificationsPage.disableNotifications();
        balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertTrue(!balloonNotificationsEnabled && !systemNotificationsEnabled, "The 'Balloon Notifications' and the 'System Notifications' checkboxes should be both unchecked.");
    }

    @Test
    public void displayBalloonNotificationsCheckBoxTest() {
        notificationsPage.displayBalloonNotificationsCheckBox().setValue(true);
        boolean balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        assertTrue(balloonNotificationsEnabled, "The 'Balloon Notifications' checkbox should be checked.");

        notificationsPage.displayBalloonNotificationsCheckBox().setValue(false);
        balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        assertTrue(!balloonNotificationsEnabled, "The 'Balloon Notifications' checkbox should be unchecked.");
    }

    @Test
    public void displaySystemNotificationsCheckBoxTest() {
        notificationsPage.displaySystemNotificationsCheckBox().setValue(true);
        boolean systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertTrue(systemNotificationsEnabled, "The 'System Notifications' checkbox should be checked.");

        notificationsPage.displaySystemNotificationsCheckBox().setValue(false);
        systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertTrue(!systemNotificationsEnabled, "The 'System Notifications' checkbox should be unchecked.");
    }
}