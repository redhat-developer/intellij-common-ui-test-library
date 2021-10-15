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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.intellij.remoterobot.fixtures.TextEditorFixture;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Build View fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = "//div[@class='BuildView']")
@FixtureName(name = "Tool Windows Pane")
public class BuildView extends CommonContainerFixture {
    private String lastBuildStatusTreeText;

    public BuildView(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        step("Create fixture - Build View", () -> {
        });
    }

    /**
     * Wait until build has finished
     */
    public void waitUntilBuildHasFinished() {
        step("Wait until build has finished", () -> {
            waitFor(Duration.ofSeconds(300), Duration.ofSeconds(3), "The build did not finish in 5 minutes.", () -> didBuildStatusTreeTextStopChanging());
        });
    }

    /**
     * Test if build is successful
     *
     * @return true if the build is successful
     */
    public boolean isBuildSuccessful() {
        return step("Test if build is successful", () -> {
            String runConsoleOutput = TextUtils.listOfRemoteTextToString(buildConsole().findAllText());
            return runConsoleOutput.contains("BUILD SUCCESS");
        });
    }

    /**
     * Get the Build Status tree fixture
     *
     * @return Build Status tree fixture
     */
    public JTreeFixture buildStatusTree() {
        return step("Get the Build Status tree fixture", () -> {
            return find(JTreeFixture.class, byXpath("//div[@class='Tree']"));
        });
    }

    /**
     * Get the build console
     *
     * @return build console fixture
     */
    public TextEditorFixture buildConsole() {
        return step("Get the build console", () -> {
            return textEditor(byXpath("//div[@accessiblename='Editor']"), Duration.ofSeconds(2));
        });
    }

    private boolean didBuildStatusTreeTextStopChanging() {
        return step("Test whether the build status tree stopped changing", () -> {
            String updatedBuildStatusTreeText = getBuildStatusTreeText();

            if (lastBuildStatusTreeText != null && lastBuildStatusTreeText.equals(updatedBuildStatusTreeText)) {
                lastBuildStatusTreeText = null;
                return true;
            } else {
                lastBuildStatusTreeText = updatedBuildStatusTreeText;
                return false;
            }
        });
    }

    private String getBuildStatusTreeText() {
        return step("Get the build status tree text", () -> {
            String buildStatusTreeText = TextUtils.listOfRemoteTextToString(buildStatusTree().findAllText());
            return buildStatusTreeText;
        });
    }
}