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
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.pages.AbstractNewProjectFinalPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.pages.JavaNewProjectFinalPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.pages.JavaNewProjectSecondPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.pages.MavenGradleNewProjectFinalPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.pages.NewProjectFirstPage;
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
    private NewProjectFirstPage newProjectFirstPage;
    private MainIdeWindow mainIdeWindow;

    @BeforeEach
    public void openNewProjectDialog() {
        openNewProjectDialogFromWelcomeDialog();
        newProjectDialogWizard = remoteRobot.find(NewProjectDialogWizard.class, Duration.ofSeconds(10));
        newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, Duration.ofSeconds(10));
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
    public void openMoreSettingsTest() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        makeSureMoreSettingsIsClosed(javaFinalPage);
        javaFinalPage.openMoreSettings();
        assertTrue(isMoreSettingsOpened(javaFinalPage), "The 'More Settings' should be visible.");
        javaFinalPage.openMoreSettings();
        assertTrue(isMoreSettingsOpened(javaFinalPage), "The 'More Settings' should be visible.");
    }

    @Test
    public void getSetModuleNameTest() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openMoreSettings();

        String currentModuleName = javaFinalPage.getModuleName();
        String newModuleName = currentModuleName + "1";
        javaFinalPage.setModuleName(newModuleName);
        currentModuleName = javaFinalPage.getModuleName();
        assertTrue(currentModuleName.equals(newModuleName), "Currently set module name should be '" + newModuleName + "' but is '" + currentModuleName + "'.");
    }

    @Test
    public void getSetContentRootTest() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openMoreSettings();

        String currentContentRoot = javaFinalPage.getContentRoot();
        String newContentRoot = currentContentRoot + "1";
        javaFinalPage.setContentRoot(newContentRoot);
        currentContentRoot = javaFinalPage.getContentRoot();
        assertTrue(currentContentRoot.equals(newContentRoot), "Currently set content root location should be '" + newContentRoot + "' but is '" + currentContentRoot + "'.");
    }

    @Test
    public void getSetModuleFileLocationTest() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openMoreSettings();

        String currentModuleFileLocation = javaFinalPage.getModuleFileLocation();
        String newModuleFileLocation = currentModuleFileLocation + "1";
        javaFinalPage.setModuleFileLocation(newModuleFileLocation);
        currentModuleFileLocation = javaFinalPage.getModuleFileLocation();
        assertTrue(currentModuleFileLocation.equals(newModuleFileLocation), "Currently set module file location should be '" + newModuleFileLocation + "' but is '" + currentModuleFileLocation + "'.");
    }


    @Test
    public void getSetProjectFormat() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openMoreSettings();

        javaFinalPage.setProjectFormat(JavaNewProjectFinalPage.ProjectFormatType.IPR_FILE_BASED);
        JavaNewProjectFinalPage.ProjectFormatType currentlySetProjectFormatType = javaFinalPage.getProjectFormat();
        assertTrue(currentlySetProjectFormatType.equals(JavaNewProjectFinalPage.ProjectFormatType.IPR_FILE_BASED), "Currently set value in the 'Project format' combo box should be '" + JavaNewProjectFinalPage.ProjectFormatType.IPR_FILE_BASED + "' but is '" + currentlySetProjectFormatType + "'.");
        javaFinalPage.setProjectFormat(JavaNewProjectFinalPage.ProjectFormatType.IDEA_DIRECTORY_BASED);
        currentlySetProjectFormatType = javaFinalPage.getProjectFormat();
        assertTrue(currentlySetProjectFormatType.equals(JavaNewProjectFinalPage.ProjectFormatType.IDEA_DIRECTORY_BASED), "Currently set value in the 'Project format' combo box should be '" + JavaNewProjectFinalPage.ProjectFormatType.IDEA_DIRECTORY_BASED + "' but is '" + currentlySetProjectFormatType + "'.");
    }

    @Test
    public void openArtifactCoordinatesMavenTest() {
        testOpenArtifactCoordinatesMavenGradle(NewProjectType.MAVEN);
    }

    @Test
    public void openArtifactCoordinatesGradleTest() {
        testOpenArtifactCoordinatesMavenGradle(NewProjectType.GRADLE);
    }

    @Test
    public void getSetGroupIdMavenTest() {
        testGetSetGroupIdMavenGradle(NewProjectType.MAVEN);
    }

    @Test
    public void getSetGroupIdGradleTest() {
        testGetSetGroupIdMavenGradle(NewProjectType.GRADLE);
    }

    @Test
    public void getSetArtifactIdMavenTest() {
        testGetSetArtifactIdMavenGradle(NewProjectType.MAVEN);
    }

    @Test
    public void getSetArtifactIdGradleTest() {
        testGetSetArtifactIdMavenGradle(NewProjectType.GRADLE);
    }

    @Test
    public void getSetVersionMavenTest() {
        testGetSetVersionMavenGradle(NewProjectType.MAVEN);
    }

    @Test
    public void getSetVersionGradleTest() {
        testGetSetVersionMavenGradle(NewProjectType.GRADLE);
    }

    @Test
    public void toggleFromTemplateTest() {
        newProjectFirstPage.selectNewProjectType("Java");
        newProjectDialogWizard.next();
        JavaNewProjectSecondPage javaNewProjectSecondPage = newProjectDialogWizard.find(JavaNewProjectSecondPage.class, Duration.ofSeconds(10));
        boolean isSelected = javaNewProjectSecondPage.fromTemplateCheckBox().isSelected();
        if (isSelected) {
            javaNewProjectSecondPage.fromTemplateCheckBox().setValue(false);
        }
        javaNewProjectSecondPage.toggleFromTemplate(true);
        assertTrue(javaNewProjectSecondPage.fromTemplateCheckBox().isSelected(), "The 'Create project from template' checkbox should be checked but is not.");
        javaNewProjectSecondPage.fromTemplateCheckBox().setValue(isSelected);
    }

    @Test
    public void previousButtonTest() {
        newProjectFirstPage.selectNewProjectType("Java");
        newProjectFirstPage.setProjectSdkIfAvailable("11");
        assertThrows(UITestException.class, () -> {
            newProjectDialogWizard.previous();
        }, "The 'UITestException' should be thrown because the 'Previous' button is not enabled on the first page of the 'New Project'.");
        newProjectDialogWizard.next();
        boolean isCommandLineAppTextPresent = TextUtils.listOfRemoteTextToString(newProjectFirstPage.findAllText()).contains("Command Line App");
        assertTrue(isCommandLineAppTextPresent, "The 'Command Line App' text should be present on the second page of the 'New Project' wizard for java project.");
        newProjectDialogWizard.previous();
        try {
            newProjectFirstPage.comboBox(byXpath("//div[@accessiblename='Project SDK:' and @class='JPanel']/div[@class='JdkComboBox']"), Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            fail("The 'Project SDK' should be available but is not.");
        }
    }

    @Test
    public void nextButtonTest() {
        newProjectFirstPage.selectNewProjectType("Java");
        newProjectFirstPage.setProjectSdkIfAvailable("11");
        newProjectDialogWizard.next();
        boolean isCommandLineAppTextPresent = TextUtils.listOfRemoteTextToString(newProjectFirstPage.findAllText()).contains("Command Line App");
        assertTrue(isCommandLineAppTextPresent, "The 'Command Line App' text should be present on the second page of the 'New Project' wizard for java project.");
        newProjectDialogWizard.next();
        assertThrows(UITestException.class, () -> {
            newProjectDialogWizard.next();
        }, "The 'UITestException' should be thrown because the 'Next' button is not available on the last page of the 'New Project' wizard.");
    }

    @Test
    public void finishButtonTest() {
        newProjectFirstPage.selectNewProjectType("Java");
        newProjectFirstPage.setProjectSdkIfAvailable("11");
        assertThrows(UITestException.class, () -> {
            newProjectDialogWizard.finish();
        }, "The 'UITestException' should be thrown because the 'Finish' button is not available on the first page of the 'New Project' wizard for java project.");
        newProjectDialogWizard.next();
        newProjectDialogWizard.next();
        assertThrows(UITestException.class, () -> {
            newProjectDialogWizard.next();
        }, "The 'UITestException' should be thrown because the 'Next' button is not available on the last page of the 'New Project' wizard.");
        newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10)).setProjectName(plainJavaProjectName);
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
        newProjectFirstPage.selectNewProjectType("Java");
        newProjectFirstPage.setProjectSdkIfAvailable("8");
        ComboBoxFixture projectJdkComboBox = newProjectFirstPage.find(ComboBoxFixture.class, byXpath("//div[@accessiblename='Project SDK:' and @class='JPanel']/div[@class='JdkComboBox']"), Duration.ofSeconds(10));
        String currentlySelectedProjectSdk = TextUtils.listOfRemoteTextToString(projectJdkComboBox.findAllText());
        assertTrue(currentlySelectedProjectSdk.contains("8"), "Selected project SDK should be Java 8 but is '" + currentlySelectedProjectSdk + "'");
        newProjectFirstPage.setProjectSdkIfAvailable("11");
        currentlySelectedProjectSdk = TextUtils.listOfRemoteTextToString(projectJdkComboBox.findAllText());
        assertTrue(currentlySelectedProjectSdk.contains("11"), "Selected project SDK should be Java 11 but is '" + currentlySelectedProjectSdk + "'");
    }

    @Test
    public void selectNewProjectTypeTest() {
        newProjectFirstPage.selectNewProjectType("Empty Project");
        boolean isEmptyProjectLabelVisible = !newProjectFirstPage.findAll(JListFixture.class, byXpath("//div[@visible_text='Empty Project']")).isEmpty();
        assertTrue(isEmptyProjectLabelVisible, "The 'Empty Project' label should be visible but is not.");

        newProjectFirstPage.selectNewProjectType("Java FX");
        boolean isJavaFXApplicationLabelVisible = !newProjectFirstPage.findAll(JListFixture.class, byXpath("//div[@visible_text='JavaFX Application']")).isEmpty();
        assertTrue(isJavaFXApplicationLabelVisible, "The 'Java FX' label should be visible but is not.");
    }

    private void navigateToSetProjectNamePage(NewProjectType newProjectType) {
        newProjectFirstPage.selectNewProjectType(newProjectType.toString());
        newProjectDialogWizard.next();
        if (newProjectType == NewProjectType.PLAIN_JAVA) {
            newProjectDialogWizard.next();
        }
    }

    private void testProjectNameInputField(NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        AbstractNewProjectFinalPage finalPage;

        switch (newProjectType) {
            case PLAIN_JAVA:
                finalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
                break;
            case MAVEN:
            case GRADLE:
                finalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));
                break;
            default:
                throw new UITestException("Unsupported project type.");
        }

        String currentProjectName = finalPage.getProjectName();
        String newProjectName = currentProjectName + "1";
        finalPage.setProjectName(newProjectName);
        currentProjectName = finalPage.getProjectName();
        assertTrue(currentProjectName.equals(newProjectName), "Currently set project name should be '" + newProjectName + "' but is '" + currentProjectName + "'.");

        String currentProjectLocation = finalPage.getProjectLocation();
        String newProjectLocation = currentProjectLocation + "2";
        finalPage.setProjectLocation(newProjectLocation);
        currentProjectLocation = finalPage.getProjectLocation();
        assertTrue(currentProjectLocation.equals(newProjectLocation), "Currently set project location should be '" + newProjectLocation + "' but is '" + currentProjectLocation + "'.");
    }

    private void testOpenArtifactCoordinatesMavenGradle(NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        MavenGradleNewProjectFinalPage mavenGradleFinalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));
        makeSureArtifactCoordinatesIsClosed(mavenGradleFinalPage);
        mavenGradleFinalPage.openArtifactCoordinates();
        assertTrue(isArtifactCoordinatesOpened(mavenGradleFinalPage), "The 'Artifact Coordinates' settings should be visible.");
        mavenGradleFinalPage.openArtifactCoordinates();
        assertTrue(isArtifactCoordinatesOpened(mavenGradleFinalPage), "The 'Artifact Coordinates' settings should be visible.");
    }

    private void testGetSetGroupIdMavenGradle(NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        MavenGradleNewProjectFinalPage mavenGradleFinalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));
        mavenGradleFinalPage.openArtifactCoordinates();
        String currentGroupId = mavenGradleFinalPage.getGroupId();
        String newGroupId = currentGroupId + "1";
        mavenGradleFinalPage.setGroupId(newGroupId);
        currentGroupId = mavenGradleFinalPage.getGroupId();
        assertTrue(currentGroupId.equals(newGroupId), "Currently set group ID should be '" + newGroupId + "' but is '" + currentGroupId + "'.");
    }

    private void testGetSetArtifactIdMavenGradle(NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        MavenGradleNewProjectFinalPage mavenGradleFinalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));
        mavenGradleFinalPage.openArtifactCoordinates();
        String currentArtifactId = mavenGradleFinalPage.getArtifactId();
        String newArtifactId = currentArtifactId + "1";
        mavenGradleFinalPage.setArtifactId(newArtifactId);
        currentArtifactId = mavenGradleFinalPage.getArtifactId();
        assertTrue(currentArtifactId.equals(newArtifactId), "Currently set artifact ID should be '" + newArtifactId + "' but is '" + currentArtifactId + "'.");
    }

    private void testGetSetVersionMavenGradle(NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        MavenGradleNewProjectFinalPage mavenGradleFinalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));
        mavenGradleFinalPage.openArtifactCoordinates();
        String currentVersion = mavenGradleFinalPage.getVersion();
        String newVersion = currentVersion + "1";
        mavenGradleFinalPage.setVersion(newVersion);
        currentVersion = mavenGradleFinalPage.getVersion();
        assertTrue(currentVersion.equals(newVersion), "Currently set version should be '" + newVersion + "' but is '" + currentVersion + "'.");
    }

    private void makeSureMoreSettingsIsClosed(JavaNewProjectFinalPage javaFinalPage) {
        if (isMoreSettingsOpened(javaFinalPage)) {
            javaFinalPage.jLabel(ButtonLabels.moreSettings).click();
        }
    }

    private boolean isMoreSettingsOpened(JavaNewProjectFinalPage javaFinalPage) {
        return javaFinalPage.findAll(ContainerFixture.class, byXpath("//div[@class='TitledSeparator']/../../*")).size() == 2;
    }

    private void makeSureArtifactCoordinatesIsClosed(MavenGradleNewProjectFinalPage mavenGradleFinalPage) {
        if (isArtifactCoordinatesOpened(mavenGradleFinalPage)) {
            mavenGradleFinalPage.jLabel(ButtonLabels.artifactCoordinates).click();
        }
    }

    private boolean isArtifactCoordinatesOpened(MavenGradleNewProjectFinalPage mavenGradleFinalPage) {
        List<ContainerFixture> cf = mavenGradleFinalPage.findAll(ContainerFixture.class, byXpath("//div[@class='HideableTitledSeparator']/../*"));
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
