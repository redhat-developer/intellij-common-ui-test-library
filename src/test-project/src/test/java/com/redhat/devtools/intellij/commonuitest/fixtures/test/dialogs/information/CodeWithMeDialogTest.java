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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.dialogs.information;

import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information.CodeWithMeDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.NewProjectFirstPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ProjectLocation;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils.openNewProjectDialogFromWelcomeDialog;

/**
 * Code With Me dialog test
 *
 * @author zcervink@redhat.com
 */
class CodeWithMeDialogTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "code_with_me_java_project";

    @BeforeAll
    static void prepareProject() {
        NewProjectDialogWizard newProjectDialogWizard = openNewProjectDialogFromWelcomeDialog(remoteRobot);
        NewProjectFirstPage newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, Duration.ofSeconds(10));

        newProjectFirstPage.selectNewProjectType(NewProjectType.NEW_PROJECT);
        newProjectFirstPage.getProjectNameTextField().click(); // Click to gain focus on newProjectFirstPage
        newProjectFirstPage.setProjectName(PROJECT_NAME);
        newProjectFirstPage.setProjectLocation(ProjectLocation.PROJECT_LOCATION);
        newProjectFirstPage.setLanguage("Java");
        newProjectFirstPage.setBuildSystem("IntelliJ");
        newProjectFirstPage.setProjectSdkIfAvailable("17");

        newProjectDialogWizard.finish();
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilAllBgTasksFinish();
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(5));
        mainIdeWindow.maximizeIdeWindow();
    }


    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    void closeCodeWithMe() {
        CodeWithMeDialog.closeCodeWithMePopupIfItAppears(remoteRobot);
    }
}