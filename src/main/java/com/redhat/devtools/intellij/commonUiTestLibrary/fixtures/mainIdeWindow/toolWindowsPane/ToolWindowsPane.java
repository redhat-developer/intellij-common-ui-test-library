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
import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import org.assertj.swing.core.MouseButton;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane.ToolToBuildProject.GRADLE;
import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane.ToolToBuildProject.MAVEN;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils.listOfRemoteTextToString;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tool windows pane fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = "//div[@class='ToolWindowsPane']")
@FixtureName(name = "Tool Windows Pane")
public class ToolWindowsPane extends CommonContainerFixture {
    private static String lastBuildStatusTreeText;
    private RemoteRobot remoteRobot;

    public ToolWindowsPane(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Create fixture for the Stripe button
     *
     * @return fixture for the Stripe button
     */
    public ComponentFixture stripeButton(String label) {
        return find(ComponentFixture.class, byXpath("//div[@accessiblename='" + label + "' and @class='StripeButton' and @text='" + label + "']"));
    }

    /**
     * Create fixture for the options tree in the 'Maven' right side panel
     *
     * @return fixture for the options tree in the 'Maven' right side panel
     */
    public ComponentFixture mavenTabTree() {
        return find(ComponentFixture.class, byXpath("//div[@class='SimpleTree']"));
    }

    /**
     * Create fixture for the options tree in the 'Gradle' right side panel
     *
     * @return fixture for the options tree in the 'Gradle' right side panel
     */
    public ComponentFixture gradleTabTree() {
        return find(ComponentFixture.class, byXpath("//div[@class='ExternalProjectTree']"));
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
                toolWindowsPaneMaven.stripeButton("Maven").click();
                waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The Maven target tree did not appear in 30 seconds.", () -> isMavenOrGradleTreeVisible(MAVEN));
                expandMavenTargetTreeIfNecessary();
                toolWindowsPaneMaven.mavenTabTree().findText("Lifecycle").doubleClick();
                toolWindowsPaneMaven.mavenTabTree().findText("install").doubleClick();
                break;
            case GRADLE:
                waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The 'Gradle' stripe button is not available.", () -> isStripeButtonAvailable("Gradle"));
                ToolWindowsPane toolWindowsPaneGradle = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
                toolWindowsPaneGradle.stripeButton("Gradle").click();
                waitFor(Duration.ofSeconds(30), Duration.ofSeconds(2), "The Gradle tasks tree did not appear in 30 seconds.", () -> isMavenOrGradleTreeVisible(GRADLE));
                expandGradleTasksTreeIfNecessary();
                toolWindowsPaneGradle.gradleTabTree().findText("Tasks").doubleClick();
                toolWindowsPaneGradle.gradleTabTree().findText("build").doubleClick();
                toolWindowsPaneGradle.gradleTabTree().findAllText("build").get(1).doubleClick();
                break;
        }

        waitUntilBuildHasFinished();
    }

    /**
     * Test is a file with given name on given path is available in the project tree
     *
     * @param path        path to navigate
     */
    public boolean isAProjectFilePresent(String... path) {
        try {
            navigateThroughProjectTree(ActionToPerform.HIGHLIGHT, path);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     * Test if build is successful
     *
     */
    public void testIfBuildIsSuccessful() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class);
        String runConsoleOutput = listOfRemoteTextToString(toolWindowsPane.runConsole().findAllText());
        assertTrue(runConsoleOutput.contains("BUILD SUCCESS"), "The build should be successful but is not.");
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

    /**
     * Create fixture for the ProjectViewTree
     *
     * @return fixture for the ProjectViewTree
     */
    public ComponentFixture projectViewTree() {
        return find(ComponentFixture.class, byXpath("//div[@class='ProjectViewTree']"), Duration.ofSeconds(10));
    }

    private void expandMavenTargetTreeIfNecessary() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        try {
            toolWindowsPane.mavenTabTree().findText("Lifecycle");
        } catch (NoSuchElementException e) {
            List<RemoteText> mavenBuildLabels = toolWindowsPane.mavenTabTree().findAllText();
            Collections.reverse(mavenBuildLabels);
            for (RemoteText label : mavenBuildLabels) {
                label.doubleClick();
            }
        }
    }

    private void expandGradleTasksTreeIfNecessary() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        String labels = listOfRemoteTextToString(toolWindowsPane.gradleTabTree().findAllText());
        // if the Gradle tasks tree is collapsed -> expand it
        if (!labels.contains("Tasks")) {
            toolWindowsPane.gradleTabTree().findText(labels).doubleClick();
        }
    }

    private boolean isMavenOrGradleTreeVisible(ToolToBuildProject toolToBuildProject) {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        ComponentFixture tabTree;

        try {
            if (toolToBuildProject == MAVEN) {
                tabTree = toolWindowsPane.mavenTabTree();
            } else if (toolToBuildProject == GRADLE) {
                tabTree = toolWindowsPane.gradleTabTree();
            } else {
                return false;
            }
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }

        String treeContent = listOfRemoteTextToString(tabTree.findAllText());
        return !treeContent.toLowerCase(Locale.ROOT).contains("nothing") && !treeContent.equals("");
    }


    private boolean isStripeButtonAvailable(String label) {
        try {
            ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
            toolWindowsPane.stripeButton(label);
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
        String buildStatusTreeText = listOfRemoteTextToString(toolWindowsPane.buildStatusTree().findAllText());
        return buildStatusTreeText;
    }

    private ComponentFixture buildStatusTree() {
        return find(ComponentFixture.class, byXpath("//div[@class='Tree']"));
    }

    private ComponentFixture runConsole() {
        return find(ComponentFixture.class, byXpath("//div[@accessiblename='Editor' and @class='EditorComponentImpl']"));
    }

    private enum ActionToPerform {
        OPEN,
        OPEN_CONTEXT_MENU,
        HIGHLIGHT
    }
}