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
package com.redhat.devtools.intellij.commonuitest.utils.internalerror;


import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Manage IDE internal errors
 *
 * @author zcervink@redhat.com
 */
public class IdeInternalErrorUtils {
    protected static final Logger LOGGER = Logger.getLogger(IdeInternalErrorUtils.class.getName());

    private IdeInternalErrorUtils() {}

    /**
     * Clear internal IDE errors on Windows
     *
     * @param remoteRobot instance of the RemoteRobot
     */
    public static void clearWindowsErrorsIfTheyAppear(RemoteRobot remoteRobot) {
        if (remoteRobot.isWin()) {
            try {
                remoteRobot.find(CommonContainerFixture.class, byXpath(XPathDefinitions.DIALOG_ROOT_PANE), UITestTimeouts.FIXTURE_TIMEOUT).button(ButtonLabels.CLEAR_ALL_LABEL).click();
            } catch (WaitForConditionTimeoutException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
        }
    }
}
