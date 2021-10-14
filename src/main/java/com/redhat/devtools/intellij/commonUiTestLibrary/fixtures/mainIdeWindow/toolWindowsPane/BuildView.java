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
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.intellij.remoterobot.fixtures.TextEditorFixture;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Build View fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = "//div[@class='BuildView']")
@FixtureName(name = "Tool Windows Pane")
public class BuildView extends CommonContainerFixture {
    private RemoteRobot remoteRobot;
    private static String lastBuildStatusTreeText;

    public BuildView(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Wait until build has finished
     */
    public void waitUntilBuildHasFinished() {
        waitFor(Duration.ofSeconds(300), Duration.ofSeconds(3), "The build did not finish in 5 minutes.", () -> didBuildStatusTreeTextStopChanging());
    }

    /**
     * Test if build is successful
     */
    public void testIfBuildIsSuccessful() {
        String runConsoleOutput = TextUtils.listOfRemoteTextToString(runConsole().findAllText());
        assertTrue(runConsoleOutput.contains("BUILD SUCCESS"), "The build should be successful but is not.");
    }

    /**
     * Get the Build Status tree fixture
     *
     * @return Build Status tree fixture
     */
    public JTreeFixture buildStatusTree() {
        return find(JTreeFixture.class, byXpath("//div[@class='Tree']"));
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
        String buildStatusTreeText = TextUtils.listOfRemoteTextToString(buildStatusTree().findAllText());
        return buildStatusTreeText;
    }

    private TextEditorFixture runConsole() {
        return textEditor(byXpath("//div[@accessiblename='Editor']"), Duration.ofSeconds(2));
    }
}