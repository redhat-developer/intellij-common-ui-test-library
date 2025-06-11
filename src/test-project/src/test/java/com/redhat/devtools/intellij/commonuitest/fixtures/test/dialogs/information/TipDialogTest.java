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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.dialogs.information;

import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.logging.Level;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tip Dialog test
 *
 * @author zcervink@redhat.com
 */
class TipDialogTest extends AbstractLibraryBaseTest {
    private TipDialog tipDialog;

    @AfterAll
    static void cleanUp() {
        JTreeFixture jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(XPathDefinitions.TREE));
        jTreeFixture.findText("Projects").click();
    }

    @BeforeEach
    void prepareTipDialog() {
        tipDialog = remoteRobot.find(FlatWelcomeFrame.class).openTipDialog();
    }

    @Test
    void closeButtonTest() {
        Duration timeout = Duration.ofSeconds(5);
        remoteRobot.find(TipDialog.class, timeout);
        tipDialog.close();
        try {
            remoteRobot.find(TipDialog.class, timeout);
            fail("The 'Tif of the Day' dialog should be closed but is not.");
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    @Test
    void dontShowTipsCheckBoxTest() {
        boolean checkboxStateBefore = tipDialog.dontShowTipsCheckBox().isSelected();
        tipDialog.dontShowTipsCheckBox().setValue(!checkboxStateBefore);
        boolean checkboxStateAfter = tipDialog.dontShowTipsCheckBox().isSelected();
        assertNotEquals(checkboxStateAfter, checkboxStateBefore,
            "The checkbox value should be '" + !checkboxStateBefore + "' but is '" + checkboxStateAfter + "'.");
        tipDialog.dontShowTipsCheckBox().setValue(checkboxStateBefore);
        tipDialog.close();
    }
}