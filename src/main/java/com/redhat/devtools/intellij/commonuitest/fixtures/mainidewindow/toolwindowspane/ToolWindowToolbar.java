/*******************************************************************************
 * Copyright (c) 2025 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

public abstract class ToolWindowToolbar extends CommonContainerFixture {

    protected ToolWindowToolbar(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    public JButtonFixture stripeButton(String label) {
        if (label.equals(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL) || label.equals(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL)) {
            return button(byXpath(XPathDefinitions.toolWindowButton(label)), Duration.ofSeconds(2));
        } else if (label.equals(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL)) {
            return button(byXpath(XPathDefinitions.TOOLTIP_TEXT_PROJECT), Duration.ofSeconds(2));
        }
        return null;
    }

    public void clickStripeButton(String label) {
        stripeButton(label).click();
    }

}
