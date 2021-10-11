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
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.intellij.remoterobot.fixtures.TextEditorFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils;
import org.assertj.swing.core.MouseButton;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Locale;
import java.util.NoSuchElementException;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tool windows pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = "//div[@class='ToolWindowsPane']")
@FixtureName(name = "Tool Windows Pane")
public class ToolWindowsPane extends CommonContainerFixture {
    private RemoteRobot remoteRobot;
    private static String lastBuildStatusTreeText;

    public ToolWindowsPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Create fixture for the Stripe button
     *
     * @param label label text of the stripe button
     * @return fixture for the Stripe button
     */
    public JButtonFixture stripeButton(String label) {
        return button(byXpath("//div[@text='" + label + "']"), Duration.ofSeconds(2));
    }

    /**
     * Build the project
     *
     * @param toolToBuildProject project management tool to manage this project
     */
    public void buildProject(ToolToBuildProject toolToBuildProject) {
        switch (toolToBuildProject) {
            case MAVEN:
                waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The 'Maven' stripe button is not available.", () -> isStripeButtonAvailable("Maven"));
                ToolWindowsPane toolWindowsPaneMaven = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
                toolWindowsPaneMaven.stripeButton(ButtonLabels.mavenStripeButtonLabel).click();
                waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The Maven target tree did not appear in 30 seconds.", () -> isMavenOrGradleTreeVisible(ToolToBuildProject.MAVEN));
                toolWindowsPaneMaven.mavenTabTree().expandAll();
                toolWindowsPaneMaven.mavenTabTree().findAllText("install").get(0).doubleClick();
                break;
            case GRADLE:
                waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The 'Gradle' stripe button is not available.", () -> isStripeButtonAvailable("Gradle"));
                ToolWindowsPane toolWindowsPaneGradle = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
                toolWindowsPaneGradle.stripeButton(ButtonLabels.gradleStripeButtonLabel).click();
                waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The Gradle tasks tree did not appear in 30 seconds.", () -> isMavenOrGradleTreeVisible(ToolToBuildProject.GRADLE));
                actionButton(byXpath("//div[contains(@myvisibleactions, 'IDE')]//div[@myicon='expandall.svg']"), Duration.ofSeconds(10)).click();
                toolWindowsPaneGradle.gradleTabTree().findAllText("build").get(1).doubleClick();
                break;
        }

        waitUntilBuildHasFinished();
    }

    /**
     * Test is a file with given name on given path is available in the project tree
     *
     * @param path path to navigate to
     * @return true if the given file exists on the given path in the project
     */
    public boolean isProjectFilePresent(String... path) {
        try {
            navigateThroughProjectTree(ActionToPerform.HIGHLIGHT, path);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     * Test if build is successful
     */
    public void testIfBuildIsSuccessful() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class);
        String runConsoleOutput = TextUtils.listOfRemoteTextToString(toolWindowsPane.runConsole().findAllText());
        assertTrue(runConsoleOutput.contains("BUILD SUCCESS"), "The build should be successful but is not.");
    }

    /**
     * Get the Project View tree fixture
     *
     * @return Project View tree fixture
     */
    public JTreeFixture projectViewTree() {
        return find(JTreeFixture.class, JTreeFixture.Companion.byType(), Duration.ofSeconds(10));
    }

    /**
     * Get the Maven Tab tree fixture
     *
     * @return Maven Tab tree fixture
     */
    public JTreeFixture mavenTabTree() {
        return find(JTreeFixture.class, byXpath("//div[@class='SimpleTree']"));
    }

    /**
     * Get the Gradle Tab tree fixture
     *
     * @return Gradle Tab tree fixture
     */
    public JTreeFixture gradleTabTree() {
        return find(JTreeFixture.class, byXpath("//div[@class='ExternalProjectTree']"));
    }

    /**
     * Get the Build Status tree fixture
     *
     * @return Build Status tree fixture
     */
    private JTreeFixture buildStatusTree() {
        return find(JTreeFixture.class, byXpath("//div[@class='Tree']"));
    }

    /**
     * Enumeration with building tools
     */
    public enum ToolToBuildProject {
        MAVEN("Maven"),
        GRADLE("Gradle");

        private final String textRepresentation;

        ToolToBuildProject(String textRepresentation) {
            this.textRepresentation = textRepresentation;
        }

        @Override
        public String toString() {
            return textRepresentation;
        }
    }

    private boolean isMavenOrGradleTreeVisible(ToolToBuildProject toolToBuildProject) {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        ComponentFixture tabTree;

        try {
            if (toolToBuildProject == ToolToBuildProject.MAVEN) {
                tabTree = toolWindowsPane.mavenTabTree();
            } else if (toolToBuildProject == ToolToBuildProject.GRADLE) {
                tabTree = toolWindowsPane.gradleTabTree();
            } else {
                return false;
            }
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }

        String treeContent = TextUtils.listOfRemoteTextToString(tabTree.findAllText());
        return !treeContent.toLowerCase(Locale.ROOT).contains("nothing") && !treeContent.equals("");
    }

    private boolean isStripeButtonAvailable(String label) {
        try {
            remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10)).stripeButton(label);
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }

    private void navigateThroughProjectTree(ActionToPerform action, String... pathArray) {
        for (int i = 0; i < pathArray.length; i++) {
            String pathItem = pathArray[i];

            // for last item perform different action
            if (i == pathArray.length - 1) {
                switch (action) {
                    case OPEN:
                        projectViewTree().findText(pathItem).doubleClick();
                        break;
                    case OPEN_CONTEXT_MENU:
                        projectViewTree().findText(pathItem).click(MouseButton.RIGHT_BUTTON);
                        break;
                    case HIGHLIGHT:
                        projectViewTree().findText(pathItem).click();
                        break;
                }
            } else {
                projectViewTree().findText(pathItem).doubleClick();
            }
        }
    }

    private void waitUntilBuildHasFinished() {
        waitFor(Duration.ofSeconds(300), Duration.ofSeconds(3), "The build did not finish in 5 minutes.", () -> didBuildStatusTreeTextStopChanging());
    }

    private boolean didBuildStatusTreeTextStopChanging() {
        String updatedBuildStatusTreeText = getBuildStatusTreeText();

        if (lastBuildStatusTreeText != null && lastBuildStatusTreeText.equals(updatedBuildStatusTreeText)) {
            lastBuildStatusTreeText = null;
            return true;
        } else {
            lastBuildStatusTreeText = updatedBuildStatusTreeText;
            return false;
        }
    }

    private String getBuildStatusTreeText() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class);
        String buildStatusTreeText = TextUtils.listOfRemoteTextToString(toolWindowsPane.buildStatusTree().findAllText());
        return buildStatusTreeText;
    }

    private TextEditorFixture runConsole() {
        return textEditor(byXpath("//div[@accessiblename='Editor']"), Duration.ofSeconds(2));
    }

    private enum ActionToPerform {
        OPEN,
        OPEN_CONTEXT_MENU,
        HIGHLIGHT
    }
}