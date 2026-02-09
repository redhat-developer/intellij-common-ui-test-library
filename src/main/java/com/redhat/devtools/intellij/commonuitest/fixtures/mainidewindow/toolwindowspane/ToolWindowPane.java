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
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.Fixture;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Tool Window Pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowPane type", xpath = XPathDefinitions.TOOL_WINDOW_PANE)
@FixtureName(name = "Tool Window Pane")
public class ToolWindowPane extends CommonContainerFixture {

    private final RemoteRobot remoteRobot;
    private final int ideaVersionInt = UITestRunner.getIdeaVersionInt();

    public ToolWindowPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Open project explorer
     */
    public void openProjectExplorer() {
        togglePane(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL, ProjectExplorer.class, true);
    }

    /**
     * Close project explorer
     */
    public void closeProjectExplorer() {
        togglePane(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL, ProjectExplorer.class, false);
    }

    /**
     * Open maven build tool pane
     */
    public void openMavenBuildToolPane() {
        togglePane(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, MavenBuildToolPane.class, true);
    }

    /**
     * Close maven build tool pane
     */
    public void closeMavenBuildToolPane() {
        togglePane(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, MavenBuildToolPane.class, false);
    }

    /**
     * Open gradle build tool pane
     */
    public void openGradleBuildToolPane() {
        togglePane(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL, GradleBuildToolPane.class, true);
    }

    /**
     * Close gradle build tool pane
     */
    public void closeGradleBuildToolPane() {
        togglePane(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL, GradleBuildToolPane.class, false);
    }

    protected void togglePane(String label, Class<? extends Fixture> fixtureClass, boolean openPane) {
        if ((!isPaneOpened(fixtureClass) && openPane) || (isPaneOpened(fixtureClass) && !openPane)) {
            clickOnStripeButton(label, openPane);
        }
    }

    public boolean isPaneOpened(Class<? extends Fixture> fixtureClass) {
        try {
            return find(fixtureClass, UITestTimeouts.FIXTURE_TIMEOUT).isShowing();
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
    }

    private void clickOnStripeButton(String label, boolean openPane) {
        if (ideaVersionInt >= 20242) {
            ToolWindowToolbar toolbar;
            if (isRightToolbarButton(label)) {
                toolbar = remoteRobot.find(ToolWindowRightToolbar.class, UITestTimeouts.FIXTURE_TIMEOUT);
            } else {
                toolbar = remoteRobot.find(ToolWindowLeftToolbar.class, UITestTimeouts.FIXTURE_TIMEOUT);
            }
            toolbar.clickStripeButton(label);
        } else {
            if (openPane) {
                button(byXpath(XPathDefinitions.label(label)), UITestTimeouts.UI_ELEMENT_TIMEOUT).click();
            } else {
                button(byXpath(XPathDefinitions.toolWindowButton(label)), UITestTimeouts.UI_ELEMENT_TIMEOUT).click();
            }
        }
    }

    private boolean isRightToolbarButton(String label) {
        return label.equals(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL) ||
            label.equals(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL);
    }
}
