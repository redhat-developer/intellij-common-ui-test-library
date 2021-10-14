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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Tool windows pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = "//div[@class='ToolWindowsPane']")
@FixtureName(name = "Tool Windows Pane")
public class ToolWindowsPane extends CommonContainerFixture {
    private RemoteRobot remoteRobot;

    public ToolWindowsPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Open project explorer
     */
    public void openProjectExplorer() {
        openPane(ButtonLabels.projectStripeButtonLabel, ProjectExplorer.class);
    }

    /**
     * Close project explorer
     */
    public void closeProjectExplorer() {
        closePane(ButtonLabels.projectStripeButtonLabel, ProjectExplorer.class);
    }

    /**
     * Open maven build tool pane
     */
    public void openMavenBuildToolPane() {
        openPane(ButtonLabels.mavenStripeButtonLabel, MavenBuildToolPane.class);
    }

    /**
     * Close maven build tool pane
     */
    public void closeMavenBuildToolPane() {
        closePane(ButtonLabels.mavenStripeButtonLabel, MavenBuildToolPane.class);
    }

    /**
     * Open gradle build tool pane
     */
    public void openGradleBuildToolPane() {
        openPane(ButtonLabels.gradleStripeButtonLabel, GradleBuildToolPane.class);
    }

    /**
     * Close gradle build tool pane
     */
    public void closeGradleBuildToolPane() {
        closePane(ButtonLabels.gradleStripeButtonLabel, GradleBuildToolPane.class);
    }

    /**
     * Create fixture for the Stripe button
     *
     * @param label label text of the stripe button
     * @return fixture for the Stripe button
     */
    public JButtonFixture stripeButton(String label) {
        return stripeButton(label, false);
    }

    /**
     * Create fixture for the Stripe button
     *
     * @param label        label text of the stripe button
     * @param isPaneOpened true if the pane is already opened
     * @return fixture for the Stripe button
     */
    public JButtonFixture stripeButton(String label, boolean isPaneOpened) {
        if (isPaneOpened) {
            if (label.equals(ButtonLabels.mavenStripeButtonLabel) || label.equals(ButtonLabels.gradleStripeButtonLabel)) {
                return button(byXpath("//div[@disabledicon='toolWindow" + label + ".svg']"), Duration.ofSeconds(2));
            } else if (label.equals(ButtonLabels.projectStripeButtonLabel)) {
                return button(byXpath("//div[@tooltiptext='Project']"), Duration.ofSeconds(2));
            }
        }
        return button(byXpath("//div[@text='" + label + "']"), Duration.ofSeconds(2));
    }

    private void openPane(String label, Class fixtureClass) {
        if (!isPaneOpened(fixtureClass)) {
            clickOnStripeButton(label);
        }
    }

    private void closePane(String label, Class fixtureClass) {
        if (isPaneOpened(fixtureClass)) {
            clickOnStripeButton(label, true);
        }
    }

    private boolean isPaneOpened(Class fixtureClass) {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        try {
            toolWindowsPane.find(fixtureClass, Duration.ofSeconds(10));
            return true;
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
    }

    private void clickOnStripeButton(String label) {
        clickOnStripeButton(label, false);
    }

    private void clickOnStripeButton(String label, boolean isPaneOpened) {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The '" + label + "' stripe button is not available.", () -> isStripeButtonAvailable(label, isPaneOpened));
        remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10)).stripeButton(label, isPaneOpened).click();
    }

    private boolean isStripeButtonAvailable(String label, boolean isPaneOpened) {
        try {
            remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10)).stripeButton(label, isPaneOpened);
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }
}