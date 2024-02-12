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
package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.Fixture;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Abstract Tool Window/Windows Pane fixture
 *
 * @author zcervink@redhat.com
 */
public abstract class AbstractToolWinPane extends CommonContainerFixture {
    private final RemoteRobot remoteRobot;

    protected AbstractToolWinPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Open project explorer
     *
     * @return the Project Explorer fixture
     */
    public ProjectExplorer openProjectExplorer() {
        return togglePane(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL, ProjectExplorer.class, true);
    }

    /**
     * Close project explorer
     */
    public void closeProjectExplorer() {
        togglePane(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL, ProjectExplorer.class, false);
    }

    /**
     * Open maven build tool pane
     *
     * @return the Maven Build Tool Pane fixture
     */
    public MavenBuildToolPane openMavenBuildToolPane() {
        return togglePane(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, MavenBuildToolPane.class, true);
    }

    /**
     * Close maven build tool pane
     */
    public void closeMavenBuildToolPane() {
        togglePane(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, MavenBuildToolPane.class, false);
    }

    /**
     * Open gradle build tool pane
     *
     * @return the Gradle Build Tool Pane fixture
     */
    public GradleBuildToolPane openGradleBuildToolPane() {
        return togglePane(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL, GradleBuildToolPane.class, true);
    }

    /**
     * Close gradle build tool pane
     */
    public void closeGradleBuildToolPane() {
        togglePane(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL, GradleBuildToolPane.class, false);
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
            if (label.equals(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL) || label.equals(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL)) {
                if (UITestRunner.getIdeaVersionInt() >= 20232) {    // Code for IntelliJ version 2023.2 and newer
                    return button(byXpath(XPathDefinitions.toolWindowButton(label)), Duration.ofSeconds(2));
                } else {                                            // Code for IntelliJ versions 2023.1 and older
                    return button(byXpath(XPathDefinitions.toolWindowSvg(label)), Duration.ofSeconds(2));
                }
            } else if (label.equals(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL)) {
                return button(byXpath(XPathDefinitions.TOOLTIP_TEXT_PROJECT), Duration.ofSeconds(2));
            }
        }
        return button(byXpath(XPathDefinitions.label(label)), Duration.ofSeconds(2));
    }

    protected <T extends Fixture> T togglePane(String label, Class<T> fixtureClass, boolean openPane) {
        if ((!isPaneOpened(fixtureClass) && openPane)) {
            clickOnStripeButton(label, false);
            return find(fixtureClass, Duration.ofSeconds(10));
        } else if (isPaneOpened(fixtureClass) && !openPane) {
            clickOnStripeButton(label, true);
        }
        return null;
    }

    private boolean isPaneOpened(Class<? extends Fixture> fixtureClass) {
        AbstractToolWinPane toolWinPane;
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        } else {
            toolWinPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        }

        try {
            toolWinPane.find(fixtureClass, Duration.ofSeconds(10));
            return true;
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
    }

    private void clickOnStripeButton(String label, boolean isPaneOpened) {
        waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The '" + label + "' stripe button is not available.", () -> isStripeButtonAvailable(label, isPaneOpened));
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10)).stripeButton(label, isPaneOpened).click();
        } else {
            remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10)).stripeButton(label, isPaneOpened).click();
        }
    }

    private boolean isStripeButtonAvailable(String label, boolean isPaneOpened) {
        try {
            if (UITestRunner.getIdeaVersionInt() >= 20221) {
                remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(2)).stripeButton(label, isPaneOpened);
            } else {
                remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(2)).stripeButton(label, isPaneOpened);
            }
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }
}