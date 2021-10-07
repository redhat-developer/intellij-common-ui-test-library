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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.dialogs.projectManipulation;

import com.intellij.remoterobot.fixtures.ComboBoxFixture;
import com.intellij.remoterobot.fixtures.JLabelFixture;
import com.intellij.remoterobot.fixtures.JListFixture;
import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.NewProjectDialog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog.closeTipDialogIfItAppears;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils.listOfRemoteTextToString;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NewProjectDialog test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
public class NewProjectDialogTest extends LibraryTestBase {
    private final String plainJavaProjectName = "plain_java_project_name_test";
    private final String mavenProjectName = "maven_project_name_test";
    private final String gradleProjectName = "gradle_project_name_test";

    private NewProjectDialog newProjectDialog;

    @BeforeEach
    public void penNewProjectDialog() {
        openNewProjectDialogFromWelcomeDialog();
        newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
    }

    @Test
    public void setProjectNamePlainJavaProjectTest() {
        testProjectNameInputField(plainJavaProjectName,NewProjectType.PLAIN_JAVA);
    }

    @Test
    public void setProjectNameMavenProjectTest() {
        testProjectNameInputField(mavenProjectName,NewProjectType.MAVEN);
    }

    @Test
    public void setProjectNameGradleProjectTest() {
        testProjectNameInputField(gradleProjectName,NewProjectType.GRADLE);
    }

    @Test
    public void nextPreviousFinishButtonTest() {
        newProjectDialog.selectNewProjectType("Java");
        newProjectDialog.setProjectSdkIfAvailable("11");
        newProjectDialog.next();
        newProjectDialog.next();
        boolean isProjectNameLabelPresent = newProjectDialog.findAll(JLabelFixture.class, byXpath("//div[@text='Project name:']")).size() == 1;
        assertTrue(isProjectNameLabelPresent, "The 'Project name:' label should be present but is not.");
        newProjectDialog.previous();
        boolean isCommandLineAppTextPresent = listOfRemoteTextToString(newProjectDialog.findAllText()).contains("Command Line App");
        assertTrue(isCommandLineAppTextPresent, "The 'Command Line App' text should be present but is not.");
        newProjectDialog.next();
        newProjectDialog.setProjectName(plainJavaProjectName);
        newProjectDialog.finish();
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilProjectImportIsComplete();
        closeTipDialogIfItAppears(remoteRobot);
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(5));
        mainIdeWindow.maximizeIdeWindow();
        ideStatusBar.waitUntilAllBgTasksFinish();
        closeProject();
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.clearWorkspace();
    }

    @Test
    public void cancelButtonTest() {
        newProjectDialog.cancel();
        remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
    }

    @Test
    public void setProjectSdkIfAvailableTest() {
        newProjectDialog.selectNewProjectType("Java");
        newProjectDialog.setProjectSdkIfAvailable("8");
        ComboBoxFixture projectJdkComboBox = remoteRobot.find(ComboBoxFixture.class, byXpath("//div[@accessiblename='Project SDK:' and @class='JPanel']/div[@class='JdkComboBox']"), Duration.ofSeconds(10));
        String currentlySelectedProjectSdk = listOfRemoteTextToString(projectJdkComboBox.findAllText());
        assertTrue(currentlySelectedProjectSdk.contains("8"), "Selected project SDK should be Java 8 but is '" + currentlySelectedProjectSdk +"'");
        newProjectDialog.setProjectSdkIfAvailable("11");
        currentlySelectedProjectSdk = listOfRemoteTextToString(projectJdkComboBox.findAllText());
        assertTrue(currentlySelectedProjectSdk.contains("11"), "Selected project SDK should be Java 11 but is '" + currentlySelectedProjectSdk +"'");
        newProjectDialog.cancel();
    }

    @Test
    public void selectNewProjectTypeTest() {
        newProjectDialog.selectNewProjectType("Empty Project");
        boolean isEmptyProjectLabelVisible = !newProjectDialog.findAll(JListFixture.class, byXpath("//div[@visible_text='Empty Project']")).isEmpty();
        assertTrue(isEmptyProjectLabelVisible, "The 'Empty Project' label should be visible but is not.");

        newProjectDialog.selectNewProjectType("Java FX");
        boolean isJavaFXApplicationLabelVisible = !newProjectDialog.findAll(JListFixture.class, byXpath("//div[@visible_text='JavaFX Application']")).isEmpty();
        assertTrue(isJavaFXApplicationLabelVisible, "The 'Java FX' label should be visible but is not.");
        newProjectDialog.cancel();
    }

    private void navigateToSetProjectNamePage(NewProjectType newProjectType) {
        newProjectDialog.selectNewProjectType(newProjectType.toString());
        newProjectDialog.next();
        if (newProjectType == NewProjectType.PLAIN_JAVA) {
            newProjectDialog.next();
        }
    }

    private void testProjectNameInputField(String projectName, NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        NewProjectDialog newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
        newProjectDialog.setProjectName(projectName);
        String projectNameFromInputField = remoteRobot.findAll(JTextFieldFixture.class, JTextFieldFixture.Companion.byType()).get(0).getText();
        assertTrue(projectName.equals(projectNameFromInputField), "Project name in the input field (" + projectNameFromInputField + ") is different from the expected project name (" + projectName + ").");
        newProjectDialog.cancel();
    }

    private enum NewProjectType {
        PLAIN_JAVA("Java"),
        MAVEN("Maven"),
        GRADLE("Gradle");

        private final String projectType;

        NewProjectType(String projectType) {
            this.projectType = projectType;
        }

        @Override
        public String toString() {
            return this.projectType;
        }
    }
}
