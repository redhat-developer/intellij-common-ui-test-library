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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * Tip of the Day dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@accessiblename='Tip of the Day' and @class='MyDialog']")
@FixtureName(name = "Tip Of The Day Dialog")
public class TipDialog extends CommonContainerFixture {
    public TipDialog(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        step("Create fixture - Tip of the Day dialog", () -> {
        });
    }

    /**
     * Close the 'Tip of the Day' dialog if it appears
     *
     * @param remoteRobot reference to the RemoteRobot instance
     */
    public static void closeTipDialogIfItAppears(RemoteRobot remoteRobot) {
        step("Close the 'Tip of the Day' dialog if it appears", () -> {
            try {
                TipDialog tipDialog = remoteRobot.find(TipDialog.class, Duration.ofSeconds(20));
                step("Click on the '" + ButtonLabels.closeLabel + "' button", () -> {
                    tipDialog.button(ButtonLabels.closeLabel).click();
                });
            } catch (WaitForConditionTimeoutException e) {
                e.printStackTrace();
            }
        });
    }
}