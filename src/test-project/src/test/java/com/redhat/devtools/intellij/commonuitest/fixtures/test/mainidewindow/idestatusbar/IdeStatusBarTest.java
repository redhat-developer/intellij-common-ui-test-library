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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.mainidewindow.idestatusbar;

import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.NewProjectFirstPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ProjectLocation;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * IdeStatusBar test
 *
 * @author zcervink@redhat.com
 */
class IdeStatusBarTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "ide_status_bar_java_project";

    private static kotlin.Pair<Boolean, IdeStatusBar> isProgressbarWithLabelVisible() {
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, UITestTimeouts.FIXTURE_TIMEOUT);
        List<RemoteText> inlineProgressPanelContent = ideStatusBar.inlineProgressPanel().findAllText();
        String inlineProgressPanelText = TextUtils.listOfRemoteTextToString(inlineProgressPanelContent);
        return new kotlin.Pair<>(!inlineProgressPanelText.isEmpty(), ideStatusBar);
    }

    @BeforeEach
    void prepareProject() {
        NewProjectDialogWizard newProjectDialogWizard = CreateCloseUtils.openNewProjectDialogFromWelcomeDialog(remoteRobot);
        NewProjectFirstPage newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, UITestTimeouts.FIXTURE_TIMEOUT);

        newProjectFirstPage.selectNewProjectType(NewProjectType.NEW_PROJECT);
        newProjectFirstPage.getProjectNameTextField().click(); // Click to gain focus on newProjectFirstPage
        newProjectFirstPage.setProjectName(PROJECT_NAME);
        newProjectFirstPage.setProjectLocation(ProjectLocation.PROJECT_LOCATION);
        newProjectFirstPage.setBuildSystem("Maven");

        newProjectDialogWizard.finish();
    }

    @AfterEach
    void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void progressBarTest() {
        IdeStatusBar ideStatusBar = waitFor(UITestTimeouts.LONG_TIMEOUT, UITestTimeouts.FAST_POLL_INTERVAL, "Wait for the appearance of progress bar in the IDE status bar.", "The progress bar in status bar did not appear in 60 seconds.", IdeStatusBarTest::isProgressbarWithLabelVisible);
        ideStatusBar.waitUntilAllBgTasksFinish();
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, UITestTimeouts.QUICK_TIMEOUT);
        mainIdeWindow.maximizeIdeWindow();
        assertTrue(mainIdeWindow.isShowing(), "The Main IDE Window should be open.");
    }
}