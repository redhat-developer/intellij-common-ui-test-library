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

import com.intellij.remoterobot.fixtures.JListFixture;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonuitest.utils.constans.XPathDefinitions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.logging.Level;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tip Dialog test
 *
 * @author zcervink@redhat.com
 */
public class TipDialogTest extends LibraryTestBase {
    private TipDialog tipDialog;

    @AfterAll
    public static void cleanUp() {
        if (ideaVersionInt >= 20213) {
            JTreeFixture jTreeFixture = remoteRobot.find(JTreeFixture.class, byXpath(XPathDefinitions.TREE));
            jTreeFixture.findText("Projects").click();
        } else if (ideaVersionInt >= 20203) {
            JListFixture jListFixture = remoteRobot.find(JListFixture.class, byXpath(XPathDefinitions.JBLIST));
            jListFixture.clickItem("Projects", false);
        }
    }

    @BeforeEach
    public void prepareTipDialog() {
        tipDialog = remoteRobot.find(FlatWelcomeFrame.class).openTipDialog();
    }

    @Test
    public void closeButtonTest() {
        remoteRobot.find(TipDialog.class, Duration.ofSeconds(5));
        tipDialog.close();
        try {
            remoteRobot.find(TipDialog.class, Duration.ofSeconds(5));
            fail("The 'Tif of the Day' dialog should be closed but is not.");
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    @Test
    public void dontShowTipsCheckBoxTest() {
        boolean checkboxStateBefore = tipDialog.dontShowTipsCheckBox().isSelected();
        tipDialog.dontShowTipsCheckBox().setValue(!checkboxStateBefore);
        boolean checkboxStateAfter = tipDialog.dontShowTipsCheckBox().isSelected();
        assertTrue(checkboxStateAfter != checkboxStateBefore,
                "The checkbox value should be '" + !checkboxStateBefore + "' but is '" + checkboxStateAfter + "'.");
        tipDialog.dontShowTipsCheckBox().setValue(checkboxStateBefore);
        tipDialog.close();
    }
}