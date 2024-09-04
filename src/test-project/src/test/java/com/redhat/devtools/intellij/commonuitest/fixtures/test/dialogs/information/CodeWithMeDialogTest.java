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

import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information.CodeWithMeDialog;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.AbstractNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.NewProjectFirstPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils.getFinalPage;
import static com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils.openNewProjectDialogFromWelcomeDialog;

/**
 * Code With Me dialog test
 *
 * @author zcervink@redhat.com
 */
class CodeWithMeDialogTest extends LibraryTestBase {
    private static final String PROJECT_NAME = "code_with_me_java_project";
    private static final boolean IDEA_VERSION_WITH_CWM_DIALOG_OPENED = ideaVersionInt >= 20212;

    @BeforeAll
    public static void prepareProject() {
        if (IDEA_VERSION_WITH_CWM_DIALOG_OPENED) {
            NewProjectDialogWizard newProjectDialogWizard = openNewProjectDialogFromWelcomeDialog(remoteRobot);
            NewProjectFirstPage newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, Duration.ofSeconds(10));

            if (UITestRunner.getIdeaVersionInt() >= 20221) {
                newProjectFirstPage.selectNewProjectType("New Project");
                newProjectFirstPage.getProjectNameTextField().click(); // Click to gain focus on newProjectFirstPage
                newProjectFirstPage.setProjectName(PROJECT_NAME);
                newProjectFirstPage.setProjectLocation(CreateCloseUtils.PROJECT_LOCATION);
                newProjectFirstPage.setLanguage("Java");
                newProjectFirstPage.setBuildSystem("IntelliJ");
                newProjectFirstPage.setProjectSdkIfAvailable("11");
            } else {
                newProjectFirstPage.selectNewProjectType("Java");
                newProjectFirstPage.setProjectSdkIfAvailable("11");
                newProjectDialogWizard.next();
                newProjectDialogWizard.next();
                AbstractNewProjectFinalPage finalPage = getFinalPage(newProjectDialogWizard, CreateCloseUtils.NewProjectType.PLAIN_JAVA);
                finalPage.setProjectName(PROJECT_NAME);
            }

            newProjectDialogWizard.finish();
            IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
            ideStatusBar.waitUntilProjectImportIsComplete();
            MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(5));
            mainIdeWindow.maximizeIdeWindow();
            ideStatusBar.waitUntilAllBgTasksFinish();
        }
    }

    @AfterAll
    public static void closeCurrentProject() {
        if (IDEA_VERSION_WITH_CWM_DIALOG_OPENED) {
            CreateCloseUtils.closeProject(remoteRobot);
        }
    }

    @Test
    public void closeCodeWithMe() {
        if (IDEA_VERSION_WITH_CWM_DIALOG_OPENED) {
            CodeWithMeDialog.closeCodeWithMePopupIfItAppears(remoteRobot);
        }
    }
}