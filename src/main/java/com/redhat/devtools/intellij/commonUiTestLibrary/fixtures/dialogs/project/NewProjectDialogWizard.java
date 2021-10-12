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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.exceptions.UITestException;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
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
        JButtonFixture previousButton = button(ButtonLabels.previousLabel);
        if (previousButton.isEnabled()) {
            previousButton.click();
        }
        else {
            throw new UITestException("The '" + ButtonLabels.previousLabel + "' button is not enabled.");
        }
    }

    /**
     * Move to the next page of the 'New Project' dialog by clicking on the 'Next' button
     */
    public void next() {
        JButtonFixture nextButton;
        try {
            nextButton = button(ButtonLabels.nextLabel);
        } catch (WaitForConditionTimeoutException e) {
            throw new UITestException("The '" + ButtonLabels.nextLabel + "' button has not been found.");
        }

        if (nextButton.isEnabled()) {
            button(ButtonLabels.nextLabel).click();
        }
        else {
            throw new UITestException("The '" + ButtonLabels.nextLabel + "' button is not enabled.");
        }
    }
    
    /**
     * Finish the 'New Project' dialog by clicking on the 'Finish' button
     */
    public void finish() {
        JButtonFixture finishButton;
        try {
            finishButton = button(ButtonLabels.finishLabel);
        } catch (WaitForConditionTimeoutException e) {
            throw new UITestException("The '" + ButtonLabels.finishLabel + "' button has not been found.");
        }

        if (finishButton.isEnabled()) {
            button(ButtonLabels.finishLabel).click();
        }
        else {
            throw new UITestException("The '" + ButtonLabels.finishLabel + "' button is not enabled.");
        }
    }

    /**
     * Close the 'New Project' dialog by clicking on the 'Cancel' button
     */
    public void cancel() {
        button(ButtonLabels.cancelLabel).click();
    }
}