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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.dialogs.settings;

import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.JCheckboxFixture;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings.SettingsDialog;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.logging.Level;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * SettingsDialog test
 *
 * @author zcervink@redhat.com
 */
class SettingsDialogTest extends AbstractLibraryBaseTest {
    private static final String APPEARANCE_AND_BEHAVIOR = "Appearance & Behavior";
    private static final String NOTIFICATIONS = "Notifications";
    private static FlatWelcomeFrame flatWelcomeFrame;
    private SettingsDialog settingsDialog = null;

    @BeforeAll
    static void openSettingsDialog() {
        flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.openSettingsDialog();
    }

    @AfterAll
    static void closeSettingsDialog() {
        remoteRobot.find(SettingsDialog.class, Duration.ofSeconds(5)).cancel();
    }

    @BeforeEach
    void prepareSettingsDialogFixture() {
        if (settingsDialog == null) {
            settingsDialog = remoteRobot.find(SettingsDialog.class, Duration.ofSeconds(5));
        }
    }

    @AfterEach
    void reopenSettingsDialogIfNeeded() {
        try {
            remoteRobot.find(SettingsDialog.class, Duration.ofSeconds(5));
        } catch (WaitForConditionTimeoutException e) {
            flatWelcomeFrame.openSettingsDialog();
            settingsDialog = remoteRobot.find(SettingsDialog.class, Duration.ofSeconds(5));
        }
    }

    @Test
    void navigateToTest() {
        settingsDialog.navigateTo(APPEARANCE_AND_BEHAVIOR, NOTIFICATIONS);
        try {
            waitFor(Duration.ofSeconds(10), Duration.ofMillis(250), "The 'Notifications' settings page is not available.", () -> isSettingsPageLoaded(NOTIFICATIONS));
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail("The 'Settings' dialog should display the 'Notifications' page.");
        }

        settingsDialog.navigateTo("Keymap");
        try {
            waitFor(Duration.ofSeconds(10), Duration.ofMillis(250), "The 'Keymap' settings page is not available.", () -> isSettingsPageLoaded("Keymap"));
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            fail("The 'Settings' dialog should display the 'Keymap' page.");
        }
    }

    @Test
    void settingsTreeTest() {
        JTreeFixture settingsTree = settingsDialog.settingsTree();
        assertTrue(settingsTree.hasText(APPEARANCE_AND_BEHAVIOR), "The Settings tree does not contain the 'Appearance & Behavior' item.");
    }

    @Test
    void okTest() {
        settingsDialog.ok();
        Duration timeout = Duration.ofSeconds(5);
        try {
            remoteRobot.find(SettingsDialog.class, timeout);
            fail("The 'Settings' dialog should be closed but is not.");
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    @Test
    void applyTest() {
        settingsDialog = remoteRobot.find(SettingsDialog.class, Duration.ofSeconds(5));
        settingsDialog.navigateTo(APPEARANCE_AND_BEHAVIOR, NOTIFICATIONS);
        JCheckboxFixture balloonNotificationsCheckbox = settingsDialog.checkBox("Display balloon notifications", true);
        balloonNotificationsCheckbox.setValue(!balloonNotificationsCheckbox.isSelected());
        settingsDialog.apply();
        balloonNotificationsCheckbox.setValue(!balloonNotificationsCheckbox.isSelected());
        settingsDialog.apply();
    }

    @Test
    void cancelTest() {
        settingsDialog.cancel();
        Duration timeout = Duration.ofSeconds(5);
        try {
            remoteRobot.find(SettingsDialog.class, timeout);
            fail("The 'Settings' dialog should be closed but is not.");
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    private boolean isSettingsPageLoaded(String label) {
        ComponentFixture cf = remoteRobot.find(ComponentFixture.class, byXpath(XPathDefinitions.BREAD_CRUMBS));
        return cf.hasText(label);
    }
}