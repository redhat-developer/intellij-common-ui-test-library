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
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Code With Me dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "CodeWithMe type", xpath = XPathDefinitions.CODE_WITH_ME_JPANEL)
@FixtureName(name = "Code With Me Dialog")
public class CodeWithMeDialog extends CommonContainerFixture {
    private static final Logger LOGGER = Logger.getLogger(CodeWithMeDialog.class.getName());

    public CodeWithMeDialog(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Close the 'Code With Me' dialog if it appears
     *
     * @param remoteRobot reference to the RemoteRobot instance
     */
    public static void closeCodeWithMePopupIfItAppears(RemoteRobot remoteRobot) {
        CodeWithMeDialog codeWithMeDialog;
        try {
            codeWithMeDialog = remoteRobot.find(CodeWithMeDialog.class, Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.INFO, "No 'Code With Me' popup found to close.");
            return;
        }
        codeWithMeDialog.findText(ButtonLabels.GOT_IT_LABEL).click();
    }
}