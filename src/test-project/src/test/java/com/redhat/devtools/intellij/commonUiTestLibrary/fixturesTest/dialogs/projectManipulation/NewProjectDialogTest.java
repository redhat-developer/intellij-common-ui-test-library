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
import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.JListFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.exceptions.UITestException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.GradleProjectSecondPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.JavaProjectSecondPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.JavaProjectThirdPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.MavenProjectSecondPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.NewProjectDialogFirstPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.abstractPages.AbstractFinalPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.abstractPages.AbstractMavenGradleFinalPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * NewProjectDialog wizard and pages test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
public class NewProjectDialogTest extends LibraryTestBase {
    private final String plainJavaProjectName = "plain_java_project_name_test";

    private NewProjectDialogWizard newProjectDialogWizard;
    private NewProjectDialogFirstPage newProjectDialogFirstPage;
    private MainIdeWindow mainIdeWindow;

    @BeforeEach
    public void openNewProjectDialog() {
        openNewProjectDialogFromWelcomeDialog();
        newProjectDialogWizard = remoteRobot.find(NewProjectDialogWizard.class, Duration.ofSeconds(10));
        newProjectDialogFirstPage = newProjectDialogWizard.find(NewProjectDialogFirstPage.class, Duration.ofSeconds(10));
    }

    @AfterEach
    public void cleanUp() {
        if (mainIdeWindow != null) {
            // tests ending with opened Main Ide Window needs to close the project and clear workspace
            IdeStatusBar ideStatusBar = mainIdeWindow.find(IdeStatusBar.class, Duration.ofSeconds(10));
            ideStatusBar.waitUntilProjectImportIsComplete();
            TipDialog.closeTipDialogIfItAppears(remoteRobot);
            mainIdeWindow.maximizeIdeWindow();
            ideStatusBar.waitUntilAllBgTasksFinish();
            mainIdeWindow.closeProject();
            remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10)).clearWorkspace();
            mainIdeWindow = null;
        } else {
            try {
                // tests ending with opened New Project Dialog needs to close the dialog
                newProjectDialogWizard.cancel();
            } catch (WaitForConditionTimeoutException e) {
                // tests ending with opened Flat Welcome Frame does not need any assistance
            }
        }
    }

    @Test
    public void setProjectNamePlainJavaProjectTest() {
        testProjectNameInputField(NewProjectType.PLAIN_JAVA);
    }

    @Test
    public void setProjectNameMavenProjectTest() {
        testProjectNameInputField(NewProjectType.MAVEN);
    }

    @Test
    public void setProjectNameGradleProjectTest() {
        testProjectNameInputField(NewProjectType.GRADLE);
    }

    @Test
    public void javaMoreSettingsTest() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaProjectThirdPage javaProjectThirdPage = newProjectDialogWizard.find(JavaProjectThirdPage.class, Duration.ofSeconds(10));
        makeSureMoreSettingsIsClosed(javaProjectThirdPage);
        javaProjectThirdPage.openMoreSettings();
        assertTrue(isMoreSettingsOpened(javaProjectThirdPage), "The 'More Settings' should be visible.");
        javaProjectThirdPage.openMoreSettings();
        assertTrue(isMoreSettingsOpened(javaProjectThirdPage), "The 'More Settings' should be visible.");

        String currentModuleName = javaProjectThirdPage.getModuleName();
        String newModuleName = currentModuleName + "1";
        javaProjectThirdPage.setModuleName(newModuleName);
        currentModuleName = javaProjectThirdPage.getModuleName();
        assertTrue(currentModuleName.equals(newModuleName), "Currently set module name should be '" + newModuleName + "' but is '" + currentModuleName + "'.");

        String currentContentRoot = javaProjectThirdPage.getContentRoot();
        String newContentRoot = currentContentRoot + "2";
        javaProjectThirdPage.setContentRoot(newContentRoot);
        currentContentRoot = javaProjectThirdPage.getContentRoot();
        assertTrue(currentContentRoot.equals(newContentRoot), "Currently set content root location should be '" + newContentRoot + "' but is '" + currentContentRoot + "'.");

        String currentModuleFileLocation = javaProjectThirdPage.getModuleFileLocation();
        String newModuleFileLocation = currentModuleFileLocation + "3";
        javaProjectThirdPage.setModuleFileLocation(newModuleFileLocation);
        currentModuleFileLocation = javaProjectThirdPage.getModuleFileLocation();
        assertTrue(currentModuleFileLocation.equals(newModuleFileLocation), "Currently set module file location should be '" + newModuleFileLocation + "' but is '" + currentModuleFileLocation + "'.");

        javaProjectThirdPage.setProjectFormat(JavaProjectThirdPage.ProjectFormatType.IPR_FILE_BASED);
        JavaProjectThirdPage.ProjectFormatType currentlySetProjectFormatType = javaProjectThirdPage.getProjectFormat();
        assertTrue(currentlySetProjectFormatType.equals(JavaProjectThirdPage.ProjectFormatType.IPR_FILE_BASED), "Currently set value in the 'Project format' combo box should be '" + JavaProjectThirdPage.ProjectFormatType.IPR_FILE_BASED + "' but is '" + currentlySetProjectFormatType + "'.");
        javaProjectThirdPage.setProjectFormat(JavaProjectThirdPage.ProjectFormatType.IDEA_DIRECTORY_BASED);
        currentlySetProjectFormatType = javaProjectThirdPage.getProjectFormat();
        assertTrue(currentlySetProjectFormatType.equals(JavaProjectThirdPage.ProjectFormatType.IDEA_DIRECTORY_BASED), "Currently set value in the 'Project format' combo box should be '" + JavaProjectThirdPage.ProjectFormatType.IDEA_DIRECTORY_BASED + "' but is '" + currentlySetProjectFormatType + "'.");
    }

    @Test
    public void mavenArtifactCoordinatesTest() {
        testArtifactCoordinatesMavenGradle(NewProjectType.MAVEN);
    }

    @Test
    public void gradleArtifactCoordinatesTest() {
        testArtifactCoordinatesMavenGradle(NewProjectType.GRADLE);
    }

    @Test
    public void toggleFromTemplateTest() {
        newProjectDialogFirstPage.selectNewProjectType("Java");
        newProjectDialogWizard.next();
        JavaProjectSecondPage javaProjectSecondPage = newProjectDialogWizard.find(JavaProjectSecondPage.class, Duration.ofSeconds(10));
        boolean isSelected = javaProjectSecondPage.fromTemplateCheckBox().isSelected();
        if (isSelected) {
            javaProjectSecondPage.fromTemplateCheckBox().setValue(false);
        }
        javaProjectSecondPage.toggleFromTemplate(true);
        assertTrue(javaProjectSecondPage.fromTemplateCheckBox().isSelected(), "The 'Create project from template' checkbox should be checked but is not.");
        javaProjectSecondPage.fromTemplateCheckBox().setValue(isSelected);
    }

    @Test
    public void previousButtonTest() {
        newProjectDialogFirstPage.selectNewProjectType("Java");
        newProjectDialogFirstPage.setProjectSdkIfAvailable("11");
        assertThrows(UITestException.class, () -> {
            newProjectDialogWizard.previous();
        }, "The 'UITestException' should be thrown because the 'Previous' button is not enabled on the first page of the 'New Project'.");
        newProjectDialogWizard.next();
        boolean isCommandLineAppTextPresent = TextUtils.listOfRemoteTextToString(newProjectDialogFirstPage.findAllText()).contains("Command Line App");
        assertTrue(isCommandLineAppTextPresent, "The 'Command Line App' text should be present on the second page of the 'New Project' wizard for java project.");
        newProjectDialogWizard.previous();
        try {
            newProjectDialogFirstPage.comboBox(byXpath("//div[@accessiblename='Project SDK:' and @class='JPanel']/div[@class='JdkComboBox']"), Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            fail("The 'Project SDK' should be available but is not.");
        }
    }

    @Test
    public void nextButtonTest() {
        newProjectDialogFirstPage.selectNewProjectType("Java");
        newProjectDialogFirstPage.setProjectSdkIfAvailable("11");
        newProjectDialogWizard.next();
        boolean isCommandLineAppTextPresent = TextUtils.listOfRemoteTextToString(newProjectDialogFirstPage.findAllText()).contains("Command Line App");
        assertTrue(isCommandLineAppTextPresent, "The 'Command Line App' text should be present on the second page of the 'New Project' wizard for java project.");
        newProjectDialogWizard.next();
        assertThrows(UITestException.class, () -> {
            newProjectDialogWizard.next();
        }, "The 'UITestException' should be thrown because the 'Next' button is not available on the last page of the 'New Project' wizard.");
    }

    @Test
    public void finishButtonTest() {
        newProjectDialogFirstPage.selectNewProjectType("Java");
        newProjectDialogFirstPage.setProjectSdkIfAvailable("11");
        assertThrows(UITestException.class, () -> {
            newProjectDialogWizard.finish();
        }, "The 'UITestException' should be thrown because the 'Finish' button is not available on the first page of the 'New Project' wizard for java project.");
        newProjectDialogWizard.next();
        newProjectDialogWizard.next();
        assertThrows(UITestException.class, () -> {
            newProjectDialogWizard.next();
        }, "The 'UITestException' should be thrown because the 'Next' button is not available on the last page of the 'New Project' wizard.");
        newProjectDialogWizard.find(JavaProjectThirdPage.class, Duration.ofSeconds(10)).setProjectName(plainJavaProjectName);
        newProjectDialogWizard.finish();
        mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
    }

    @Test
    public void cancelButtonTest() {
        newProjectDialogWizard.cancel();
        remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
    }

    @Test
    public void setProjectSdkIfAvailableTest() {
        newProjectDialogFirstPage.selectNewProjectType("Java");
        newProjectDialogFirstPage.setProjectSdkIfAvailable("8");
        ComboBoxFixture projectJdkComboBox = newProjectDialogFirstPage.find(ComboBoxFixture.class, byXpath("//div[@accessiblename='Project SDK:' and @class='JPanel']/div[@class='JdkComboBox']"), Duration.ofSeconds(10));
        String currentlySelectedProjectSdk = TextUtils.listOfRemoteTextToString(projectJdkComboBox.findAllText());
        assertTrue(currentlySelectedProjectSdk.contains("8"), "Selected project SDK should be Java 8 but is '" + currentlySelectedProjectSdk + "'");
        newProjectDialogFirstPage.setProjectSdkIfAvailable("11");
        currentlySelectedProjectSdk = TextUtils.listOfRemoteTextToString(projectJdkComboBox.findAllText());
        assertTrue(currentlySelectedProjectSdk.contains("11"), "Selected project SDK should be Java 11 but is '" + currentlySelectedProjectSdk + "'");
    }

    @Test
    public void selectNewProjectTypeTest() {
        newProjectDialogFirstPage.selectNewProjectType("Empty Project");
        boolean isEmptyProjectLabelVisible = !newProjectDialogFirstPage.findAll(JListFixture.class, byXpath("//div[@visible_text='Empty Project']")).isEmpty();
        assertTrue(isEmptyProjectLabelVisible, "The 'Empty Project' label should be visible but is not.");

        newProjectDialogFirstPage.selectNewProjectType("Java FX");
        boolean isJavaFXApplicationLabelVisible = !newProjectDialogFirstPage.findAll(JListFixture.class, byXpath("//div[@visible_text='JavaFX Application']")).isEmpty();
        assertTrue(isJavaFXApplicationLabelVisible, "The 'Java FX' label should be visible but is not.");
    }

    private void navigateToSetProjectNamePage(NewProjectType newProjectType) {
        newProjectDialogFirstPage.selectNewProjectType(newProjectType.toString());
        newProjectDialogWizard.next();
        if (newProjectType == NewProjectType.PLAIN_JAVA) {
            newProjectDialogWizard.next();
        }
    }

    private void testProjectNameInputField(NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        AbstractFinalPage abstractFinalPage = newProjectDialogWizard.find(AbstractFinalPage.class, Duration.ofSeconds(10));

        String currentProjectName = abstractFinalPage.getProjectName();
        String newProjectName = currentProjectName + "1";
        abstractFinalPage.setProjectName(newProjectName);
        currentProjectName = abstractFinalPage.getProjectName();
        assertTrue(currentProjectName.equals(newProjectName), "Currently set project name should be '" + newProjectName + "' but is '" + currentProjectName + "'.");

        String currentProjectLocation = abstractFinalPage.getProjectLocation();
        String newProjectLocation = currentProjectLocation + "2";
        abstractFinalPage.setProjectLocation(newProjectLocation);
        currentProjectLocation = abstractFinalPage.getProjectLocation();
        assertTrue(currentProjectLocation.equals(newProjectLocation), "Currently set project location should be '" + newProjectLocation + "' but is '" + currentProjectLocation + "'.");
    }

    private void testArtifactCoordinatesMavenGradle(NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);

        AbstractMavenGradleFinalPage mavenGradleProjectSecondPage;
        if (newProjectType.equals(NewProjectType.MAVEN)) {
            mavenGradleProjectSecondPage = newProjectDialogWizard.find(MavenProjectSecondPage.class, Duration.ofSeconds(10));
        } else if (newProjectType.equals(NewProjectType.GRADLE)) {
            mavenGradleProjectSecondPage = newProjectDialogWizard.find(GradleProjectSecondPage.class, Duration.ofSeconds(10));
        } else {
            throw new UITestException("Unsupported new project type.");
        }

        makeSureArtifactCoordinatesIsClosed(mavenGradleProjectSecondPage);
        mavenGradleProjectSecondPage.openArtifactCoordinates();
        assertTrue(isArtifactCoordinatesOpened(mavenGradleProjectSecondPage), "The 'Artifact Coordinates' settings should be visible.");
        mavenGradleProjectSecondPage.openArtifactCoordinates();
        assertTrue(isArtifactCoordinatesOpened(mavenGradleProjectSecondPage), "The 'Artifact Coordinates' settings should be visible.");

        String currentGroupId = mavenGradleProjectSecondPage.getGroupId();
        String newGroupId = currentGroupId + "1";
        mavenGradleProjectSecondPage.setGroupId(newGroupId);
        currentGroupId = mavenGradleProjectSecondPage.getGroupId();
        assertTrue(currentGroupId.equals(newGroupId), "Currently set group ID should be '" + newGroupId + "' but is '" + currentGroupId + "'.");

        String currentArtifactId = mavenGradleProjectSecondPage.getArtifactId();
        String newArtifactId = currentArtifactId + "2";
        mavenGradleProjectSecondPage.setArtifactId(newArtifactId);
        currentArtifactId = mavenGradleProjectSecondPage.getArtifactId();
        assertTrue(currentArtifactId.equals(newArtifactId), "Currently set artifact ID should be '" + newArtifactId + "' but is '" + currentArtifactId + "'.");

        String currentVersion = mavenGradleProjectSecondPage.getVersion();
        String newVersion = currentVersion + "3";
        mavenGradleProjectSecondPage.setVersion(newVersion);
        currentVersion = mavenGradleProjectSecondPage.getVersion();
        assertTrue(currentVersion.equals(newVersion), "Currently set version should be '" + newVersion + "' but is '" + currentVersion + "'.");
    }

    private void makeSureMoreSettingsIsClosed(JavaProjectThirdPage javaProjectThirdPage) {
        if (isMoreSettingsOpened(javaProjectThirdPage)) {
            javaProjectThirdPage.jLabel(ButtonLabels.moreSettings).click();
        }
    }

    private boolean isMoreSettingsOpened(JavaProjectThirdPage javaProjectThirdPage) {
        return javaProjectThirdPage.findAll(ContainerFixture.class, byXpath("//div[@class='TitledSeparator']/../../*")).size() == 2;
    }

    private void makeSureArtifactCoordinatesIsClosed(AbstractMavenGradleFinalPage abstractMavenGradleFinalPage) {
        if (isArtifactCoordinatesOpened(abstractMavenGradleFinalPage)) {
            abstractMavenGradleFinalPage.jLabel(ButtonLabels.artifactCoordinates).click();
        }
    }

    private boolean isArtifactCoordinatesOpened(AbstractMavenGradleFinalPage abstractMavenGradleFinalPage) {
        List<ContainerFixture> cf = abstractMavenGradleFinalPage.findAll(ContainerFixture.class, byXpath("//div[@class='HideableTitledSeparator']/../*"));
        return cf.size() > 5;
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
