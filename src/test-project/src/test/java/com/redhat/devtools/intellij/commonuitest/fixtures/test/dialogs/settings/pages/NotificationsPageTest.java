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

import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings.SettingsDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings.pages.NotificationsPage;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NotificationsPage test
 *
 * @author zcervink@redhat.com
 */
class NotificationsPageTest extends AbstractLibraryBaseTest {
    private static SettingsDialog settingsDialog;
    private static NotificationsPage notificationsPage;
    private boolean balloonNotificationsCheckBox;
    private boolean systemNotificationsCheckBox;

    @BeforeAll
    static void openSettingsDialog() {
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, UITestTimeouts.FIXTURE_TIMEOUT);
        flatWelcomeFrame.openSettingsDialog();
        settingsDialog = remoteRobot.find(SettingsDialog.class, UITestTimeouts.SHORT_TIMEOUT);
        settingsDialog.navigateTo("Appearance & Behavior", "Notifications");
        notificationsPage = remoteRobot.find(NotificationsPage.class, UITestTimeouts.SHORT_TIMEOUT);
    }

    @AfterAll
    static void closeSettingsDialog() {
        settingsDialog.cancel();
    }

    @BeforeEach
    void backupSettings() {
        balloonNotificationsCheckBox = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        systemNotificationsCheckBox = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
    }

    @AfterEach
    void restoreSettings() {
        notificationsPage.displayBalloonNotificationsCheckBox().setValue(balloonNotificationsCheckBox);
        notificationsPage.displaySystemNotificationsCheckBox().setValue(systemNotificationsCheckBox);
    }

    @Test
    void toggleNotificationsTest() {
        notificationsPage.displayBalloonNotificationsCheckBox().setValue(true);
        notificationsPage.displaySystemNotificationsCheckBox().setValue(true);
        boolean balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        boolean systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertTrue(balloonNotificationsEnabled && systemNotificationsEnabled, "The 'Balloon Notifications' and the 'System Notifications' checkboxes should be both checked.");

        notificationsPage.toggleNotifications(false);
        balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertTrue(!balloonNotificationsEnabled && !systemNotificationsEnabled, "The 'Balloon Notifications' and the 'System Notifications' checkboxes should be both unchecked.");
    }

    @Test
    void toggleBalloonNotificationsTest() {
        notificationsPage.displayBalloonNotificationsCheckBox().setValue(true);
        notificationsPage.toggleBalloonNotifications(false);
        boolean balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        assertFalse(balloonNotificationsEnabled, "The 'Balloon Notifications' checkbox should be unchecked.");
    }

    @Test
    void toggleSystemNotificationsTest() {
        notificationsPage.displaySystemNotificationsCheckBox().setValue(true);
        notificationsPage.toggleSystemNotifications(false);
        boolean systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertFalse(systemNotificationsEnabled, "The 'System Notifications' checkbox should be unchecked.");
    }

    @Test
    void displayBalloonNotificationsCheckBoxTest() {
        notificationsPage.displayBalloonNotificationsCheckBox().setValue(true);
        boolean balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        assertTrue(balloonNotificationsEnabled, "The 'Balloon Notifications' checkbox should be checked.");

        notificationsPage.displayBalloonNotificationsCheckBox().setValue(false);
        balloonNotificationsEnabled = notificationsPage.displayBalloonNotificationsCheckBox().isSelected();
        assertFalse(balloonNotificationsEnabled, "The 'Balloon Notifications' checkbox should be unchecked.");
    }

    @Test
    void displaySystemNotificationsCheckBoxTest() {
        notificationsPage.displaySystemNotificationsCheckBox().setValue(true);
        boolean systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertTrue(systemNotificationsEnabled, "The 'System Notifications' checkbox should be checked.");

        notificationsPage.displaySystemNotificationsCheckBox().setValue(false);
        systemNotificationsEnabled = notificationsPage.displaySystemNotificationsCheckBox().isSelected();
        assertFalse(systemNotificationsEnabled, "The 'System Notifications' checkbox should be unchecked.");
    }
}