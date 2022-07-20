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
package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.GradleBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.buildtoolpane.MavenBuildToolPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Tool Windows Pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = XPathDefinitions.TOOL_WINDOWS_PANE)
@FixtureName(name = "Tool Windows Pane")
public class ToolWindowsPane extends AbstractToolWinPane {
    public ToolWindowsPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Open project explorer
     *
     * @return the Project Explorer fixture
     */
    @Override
    public ProjectExplorer openProjectExplorer() {
        return togglePane(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL, ProjectExplorer.class, true);
    }

    /**
     * Close project explorer
     */
    @Override
    public void closeProjectExplorer() {
        togglePane(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL, ProjectExplorer.class, false);
    }

    /**
     * Open maven build tool pane
     *
     * @return the Maven Build Tool Pane fixture
     */
    @Override
    public MavenBuildToolPane openMavenBuildToolPane() {
        return togglePane(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, MavenBuildToolPane.class, true);
    }

    /**
     * Close maven build tool pane
     */
    @Override
    public void closeMavenBuildToolPane() {
        togglePane(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL, MavenBuildToolPane.class, false);
    }

    /**
     * Open gradle build tool pane
     *
     * @return the Gradle Build Tool Pane fixture
     */
    @Override
    public GradleBuildToolPane openGradleBuildToolPane() {
        return togglePane(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL, GradleBuildToolPane.class, true);
    }

    /**
     * Close gradle build tool pane
     */
    @Override
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
    @Override
    public JButtonFixture stripeButton(String label, boolean isPaneOpened) {
        if (isPaneOpened) {
            if (label.equals(ButtonLabels.MAVEN_STRIPE_BUTTON_LABEL) || label.equals(ButtonLabels.GRADLE_STRIPE_BUTTON_LABEL)) {
                return button(byXpath(XPathDefinitions.toolWindowSvg(label)), Duration.ofSeconds(2));
            } else if (label.equals(ButtonLabels.PROJECT_STRIPE_BUTTON_LABEL)) {
                return button(byXpath(XPathDefinitions.TOOLTIP_TEXT_PROJECT), Duration.ofSeconds(2));
            }
        }
        return button(byXpath(XPathDefinitions.label(label)), Duration.ofSeconds(2));
    }
}