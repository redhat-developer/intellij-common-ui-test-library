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
import com.intellij.remoterobot.fixtures.Fixture;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.buildToolPane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Tool Windows Pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = "//div[@class='ToolWindowsPane']")
@FixtureName(name = "Tool Windows Pane")
public class ToolWindowsPane extends CommonContainerFixture {
    private RemoteRobot remoteRobot;

    public ToolWindowsPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        step("Create fixture - Tool Windows Pane", () -> {
            this.remoteRobot = remoteRobot;
        });
    }

    /**
     * Open project explorer
     *
     * @return the Project Explorer fixture
     */
    public ProjectExplorer openProjectExplorer() {
        return step("Open project explorer", () -> {
            return togglePane(ButtonLabels.projectStripeButtonLabel, ProjectExplorer.class, true);
        });
    }

    /**
     * Close project explorer
     */
    public void closeProjectExplorer() {
        step("Close project explorer", () -> {
            togglePane(ButtonLabels.projectStripeButtonLabel, ProjectExplorer.class, false);
        });
    }

    /**
     * Open maven build tool pane
     *
     * @return the Maven Build Tool Pane fixture
     */
    public MavenBuildToolPane openMavenBuildToolPane() {
        return step("Open maven build tool pane", () -> {
            return togglePane(ButtonLabels.mavenStripeButtonLabel, MavenBuildToolPane.class, true);
        });
    }

    /**
     * Close maven build tool pane
     */
    public void closeMavenBuildToolPane() {
        step("Close maven build tool pane", () -> {
            togglePane(ButtonLabels.mavenStripeButtonLabel, MavenBuildToolPane.class, false);
        });
    }

    /**
     * Open gradle build tool pane
     *
     * @return the Gradle Build Tool Pane fixture
     */
    public GradleBuildToolPane openGradleBuildToolPane() {
        return step("Open gradle build tool pane", () -> {
            return togglePane(ButtonLabels.gradleStripeButtonLabel, GradleBuildToolPane.class, true);
        });
    }

    /**
     * Close gradle build tool pane
     */
    public void closeGradleBuildToolPane() {
        step("Close gradle build tool pane", () -> {
            togglePane(ButtonLabels.gradleStripeButtonLabel, GradleBuildToolPane.class, false);
        });
    }

    /**
     * Create fixture for the Stripe button
     *
     * @param label        label text of the stripe button
     * @param isPaneOpened true if the pane is already opened
     * @return fixture for the Stripe button
     */
    public JButtonFixture stripeButton(String label, boolean isPaneOpened) {
        return step("Create fixture for the '" + label + "' Stripe button", () -> {
            if (isPaneOpened) {
                if (label.equals(ButtonLabels.mavenStripeButtonLabel) || label.equals(ButtonLabels.gradleStripeButtonLabel)) {
                    return button(byXpath("//div[@disabledicon='toolWindow" + label + ".svg']"), Duration.ofSeconds(2));
                } else if (label.equals(ButtonLabels.projectStripeButtonLabel)) {
                    return button(byXpath("//div[@tooltiptext='Project']"), Duration.ofSeconds(2));
                }
            }
            return button(byXpath("//div[@text='" + label + "']"), Duration.ofSeconds(2));
        });
    }

    private <T extends Fixture> T togglePane(String label, Class<T> fixtureClass, boolean openPane) {
        return step("Toggle the '" + label + "' pane", () -> {
            if ((!isPaneOpened(fixtureClass) && openPane)) {
                clickOnStripeButton(label, false);
                return find(fixtureClass, Duration.ofSeconds(10));
            } else if (isPaneOpened(fixtureClass) && !openPane) {
                clickOnStripeButton(label, true);
            }
            return null;
        });
    }

    private boolean isPaneOpened(Class fixtureClass) {
        return step("Test whether the pane is opened", () -> {
            ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
            try {
                toolWindowsPane.find(fixtureClass, Duration.ofSeconds(10));
                return true;
            } catch (WaitForConditionTimeoutException e) {
                return false;
            }
        });
    }

    private void clickOnStripeButton(String label, boolean isPaneOpened) {
        step("Click on the '" + label + "' stripe button", () -> {
            waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The '" + label + "' stripe button is not available.", () -> isStripeButtonAvailable(label, isPaneOpened));
            remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10)).stripeButton(label, isPaneOpened).click();
        });
    }

    private boolean isStripeButtonAvailable(String label, boolean isPaneOpened) {
        return step("Test whether the '" + label + "' stripe button is available", () -> {
            try {
                remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10)).stripeButton(label, isPaneOpened);
            } catch (WaitForConditionTimeoutException e) {
                return false;
            }
            return true;
        });
    }
}