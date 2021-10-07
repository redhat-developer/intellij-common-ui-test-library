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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.mainIdeWindow.ideStatusBar;

import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.NewProjectDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils.listOfRemoteTextToString;
import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog.closeTipDialogIfItAppears;

/**
 * IdeStatusBar test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
class IdeStatusBarTest extends LibraryTestBase {
    private final String projectName = "ide_status_bar_java_project";

    @BeforeEach
    public void prepareProject() {
        openNewProjectDialogFromWelcomeDialog();
        NewProjectDialog newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
        newProjectDialog.selectNewProjectType("Maven");
        newProjectDialog.next();
        newProjectDialog.setProjectName(projectName);
        newProjectDialog.finish();
    }

    @AfterEach
    public void closeCurrentProject() {
        super.closeProject();
    }

    @Test
    public void progressBarTest() {
        IdeStatusBar ideStatusBar = waitFor(Duration.ofSeconds(60), Duration.ofSeconds(1), "The progress bar in status bar did not appear in 60 seconds.", () -> isProgressbarWithLabelVisible());
        ideStatusBar.waitUntilProjectImportIsComplete();
        closeTipDialogIfItAppears(remoteRobot);
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(5));
        mainIdeWindow.maximizeIdeWindow();
        ideStatusBar.waitUntilAllBgTasksFinish();
    }

    private static kotlin.Pair<Boolean, IdeStatusBar> isProgressbarWithLabelVisible() {
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        List<RemoteText> inlineProgressPanelContent = ideStatusBar.inlineProgressPanel().findAllText();
        String inlineProgressPanelText = listOfRemoteTextToString(inlineProgressPanelContent);
        return new kotlin.Pair(!inlineProgressPanelText.equals(""), ideStatusBar);
    }
}