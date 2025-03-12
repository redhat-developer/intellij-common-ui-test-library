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
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.MavenGradleNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.NewProjectFirstPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ProjectLocation;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * IdeStatusBar test
 *
 * @author zcervink@redhat.com
 */
class IdeStatusBarTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "ide_status_bar_java_project";

    private static kotlin.Pair<Boolean, IdeStatusBar> isProgressbarWithLabelVisible() {
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        List<RemoteText> inlineProgressPanelContent = ideStatusBar.inlineProgressPanel().findAllText();
        String inlineProgressPanelText = TextUtils.listOfRemoteTextToString(inlineProgressPanelContent);
        return new kotlin.Pair<>(!inlineProgressPanelText.isEmpty(), ideStatusBar);
    }

    @BeforeEach
    void prepareProject() {
        NewProjectDialogWizard newProjectDialogWizard = CreateCloseUtils.openNewProjectDialogFromWelcomeDialog(remoteRobot);
        NewProjectFirstPage newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, Duration.ofSeconds(10));

        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            newProjectFirstPage.selectNewProjectType("New Project");
            newProjectFirstPage.getProjectNameTextField().click(); // Click to gain focus on newProjectFirstPage
            newProjectFirstPage.setProjectName(PROJECT_NAME);
            newProjectFirstPage.setProjectLocation(ProjectLocation.PROJECT_LOCATION);
            newProjectFirstPage.selectNewProjectType("New Project");
            newProjectFirstPage.setBuildSystem("Maven");
        } else {
            newProjectFirstPage.selectNewProjectType(CreateCloseUtils.NewProjectType.MAVEN.toString());
            newProjectDialogWizard.next();
            MavenGradleNewProjectFinalPage mavenGradleFinalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));
            mavenGradleFinalPage.setProjectName(PROJECT_NAME);
        }

        newProjectDialogWizard.finish();
    }

    @AfterEach
    void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void progressBarTest() {
        IdeStatusBar ideStatusBar = waitFor(Duration.ofSeconds(60), Duration.ofSeconds(1), "Wait for the appearance of progress bar in the IDE status bar.", "The progress bar in status bar did not appear in 60 seconds.", IdeStatusBarTest::isProgressbarWithLabelVisible);
        ideStatusBar.waitUntilProjectImportIsComplete();
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(5));
        mainIdeWindow.maximizeIdeWindow();
        ideStatusBar.waitUntilAllBgTasksFinish();
    }
}