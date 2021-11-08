/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.utils.labels.ButtonLabels;
import org.jetbrains.annotations.NotNull;

/**
 * New Project dialog wizard fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@accessiblename='New Project' and @class='MyDialog']")
@FixtureName(name = "New Project Dialog")
public class NewProjectDialogWizard extends CommonContainerFixture {
    public NewProjectDialogWizard(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Move to the previous page of the 'New Project' dialog by clicking on the 'Previous' button
     */
    public void previous() {
        clickOnButton(ButtonLabels.PREVIOUS_LABEL);
    }

    /**
     * Move to the next page of the 'New Project' dialog by clicking on the 'Next' button
     */
    public void next() {
        clickOnButton(ButtonLabels.NEXT_LABEL);
    }

    /**
     * Finish the 'New Project' dialog by clicking on the 'Finish' button
     */
    public void finish() {
        clickOnButton(ButtonLabels.FINISH_LABEL);
    }

    /**
     * Close the 'New Project' dialog by clicking on the 'Cancel' button
     */
    public void cancel() {
        button(ButtonLabels.CANCEL_LABEL).click();
    }

    private void clickOnButton(String label) {
        JButtonFixture button;
        try {
            button = button(label);
        } catch (WaitForConditionTimeoutException e) {
            throw new UITestException("The '" + label + "' button has not been found.");
        }

        if (button.isEnabled()) {
            button.click();
        } else {
            throw new UITestException("The '" + label + "' button is not enabled.");
        }
    }
}