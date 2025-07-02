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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.dialogs.project_manipulation;

import com.intellij.remoterobot.fixtures.ActionButtonFixture;
import com.intellij.remoterobot.fixtures.ComboBoxFixture;
import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.JLabelFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.AbstractNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.JavaNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.MavenGradleNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.NewProjectFirstPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils.listOfRemoteTextToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * NewProjectDialog wizard and pages test
 *
 * @author zcervink@redhat.com
 */
class NewProjectDialogTest extends AbstractLibraryBaseTest {
    private static final String PLAIN_JAVA_PROJECT_NAME = "plain_java_project_name_test";
    private static final String ADVANCED_SETTINGS_SHOULD_BE_OPENED_MSG = "The 'Advanced Settings' section should be opened but is not";
    private static final String ADVANCED_SETTINGS_SHOULD_BE_CLOSED_MSG = "The 'Advanced Settings' section should be closed but is not";
    private final int ideaVersionInt = UITestRunner.getIdeaVersionInt();

    private NewProjectDialogWizard newProjectDialogWizard;
    private NewProjectFirstPage newProjectFirstPage;
    private MainIdeWindow mainIdeWindow;

    @BeforeEach
    void openNewProjectDialog() {
        newProjectDialogWizard = CreateCloseUtils.openNewProjectDialogFromWelcomeDialog(remoteRobot);
        newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, Duration.ofSeconds(10));
    }

    @AfterEach
    void cleanUp() {
        if (mainIdeWindow != null) {
            // tests ending with opened Main Ide Window needs to close the project and clear workspace
            IdeStatusBar ideStatusBar = mainIdeWindow.find(IdeStatusBar.class, Duration.ofSeconds(10));
            ideStatusBar.waitUntilAllBgTasksFinish();
            mainIdeWindow.maximizeIdeWindow();
            mainIdeWindow.closeProject();
            remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10)).clearWorkspace();
            mainIdeWindow = null;
        } else {
            try {
                // tests ending with opened New Project Dialog needs to close the dialog
                remoteRobot.find(NewProjectDialogWizard.class).cancel();
            } catch (WaitForConditionTimeoutException e) {
                // tests ending with opened Flat Welcome Frame does not need any assistance
            }
        }

    }

    @Test
    void setProjectNamePlainJavaProjectTest() {
        testProjectNameAndLocationInputField(NewProjectType.PLAIN_JAVA);
    }

    @Test
    void setProjectNameMavenProjectTest() {
        testProjectNameAndLocationInputField(NewProjectType.MAVEN);
    }

    @Test
    void setProjectNameGradleProjectTest() {
        testProjectNameAndLocationInputField(NewProjectType.GRADLE);
    }

    @Test
    void openAdvancedSettingsTest() {
        newProjectFirstPage.closeAdvanceSettings();
        assertFalse(newProjectFirstPage.isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_CLOSED_MSG);
        newProjectFirstPage.openAdvanceSettings();
        assertTrue(newProjectFirstPage.isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_OPENED_MSG);
    }

    @Test
    void closeAdvancedSettingsTest() {
        newProjectFirstPage.openAdvanceSettings();
        assertTrue(newProjectFirstPage.isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_OPENED_MSG);
        newProjectFirstPage.closeAdvanceSettings();
        assertFalse(newProjectFirstPage.isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_CLOSED_MSG);
    }

    @Test
    void getSetModuleNameTest() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openAdvanceSettings();

        String currentModuleName = javaFinalPage.getModuleName();
        String newModuleName = currentModuleName + "1";
        javaFinalPage.setModuleName(newModuleName);
        currentModuleName = javaFinalPage.getModuleName();
        assertEquals(currentModuleName, newModuleName, "Currently set module name doesn't match.");
    }

    @Test
    void getSetContentRootTest() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openAdvanceSettings();

        String currentContentRoot = javaFinalPage.getContentRoot();
        String newContentRoot = currentContentRoot + "1";
        javaFinalPage.setContentRoot(newContentRoot);
        currentContentRoot = javaFinalPage.getContentRoot();
        assertEquals(currentContentRoot, newContentRoot, "Currently set content root location doesn't match.");
    }

    @Test
    void getSetModuleFileLocationTest() {
        navigateToSetProjectNamePage(NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openAdvanceSettings();

        String currentModuleFileLocation = javaFinalPage.getModuleFileLocation();
        String newModuleFileLocation = currentModuleFileLocation + "1";
        javaFinalPage.setModuleFileLocation(newModuleFileLocation);
        currentModuleFileLocation = javaFinalPage.getModuleFileLocation();
        assertEquals(currentModuleFileLocation, newModuleFileLocation, "Currently set module file location doesn't match.");
    }

    @Test
    void getSetGroupIdMavenTest() {
        testArtifactCoordinatesAttributes(NewProjectType.MAVEN, ArtifactCoordinatesAttributes.GROUP_ID);
    }

    @Test
    void getSetGroupIdGradleTest() {
        testArtifactCoordinatesAttributes(NewProjectType.GRADLE, ArtifactCoordinatesAttributes.GROUP_ID);
    }

    @Test
    void getSetArtifactIdMavenTest() {
        testArtifactCoordinatesAttributes(NewProjectType.MAVEN, ArtifactCoordinatesAttributes.ARTIFACT_ID);
    }

    @Test
    void getSetArtifactIdGradleTest() {
        testArtifactCoordinatesAttributes(NewProjectType.GRADLE, ArtifactCoordinatesAttributes.ARTIFACT_ID);
    }

    @Test
    void finishButtonTest() {
        newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10)).setProjectName(PLAIN_JAVA_PROJECT_NAME);
        newProjectDialogWizard.finish();
        mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
        assertTrue(mainIdeWindow.isShowing(), "The Main IDE Window should be open.");
    }

    @Test
    void cancelButtonTest() {
        newProjectDialogWizard.cancel();
        FlatWelcomeFrame welcome = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        assertTrue(welcome.isShowing(), "The Welcome Window should be open.");
    }

    @Test
    void setProjectSdkIfAvailableTest() {
        if (ideaVersionInt >= 20242 && remoteRobot.isWin()) {
            newProjectFirstPage.setProjectSdkIfAvailable("Download");
            try {
                ContainerFixture downloadJdkDialog = remoteRobot.find(ContainerFixture.class, byXpath("//div[@title='Download JDK']"), Duration.ofSeconds(10));
                downloadJdkDialog.find(ActionButtonFixture.class, byXpath(XPathDefinitions.label(ButtonLabels.CANCEL_LABEL)), Duration.ofSeconds(5)).click();
            } catch (WaitForConditionTimeoutException e) {
                fail("Download JDK button was not pressed and Download JDK dialog was not found");
            }
        } else {
            setProjectSDKVersion();
        }
    }

    private void setProjectSDKVersion() {
        newProjectFirstPage.setProjectSdkIfAvailable("17");
        ComboBoxFixture projectJdkComboBox = newProjectFirstPage.getProjectJdkComboBox();
        String currentlySelectedProjectSdk = listOfRemoteTextToString(projectJdkComboBox.findAllText());
        Optional<String> optional = Arrays.stream(currentlySelectedProjectSdk.split(" ")).filter(s ->
            s.startsWith("17")).findFirst();
        assertTrue(optional.isPresent(), "Selected project SDK should be Java 17 but is '" + currentlySelectedProjectSdk + "'");
    }

    @Test
    void selectNewProjectTypeTest() {
        newProjectFirstPage.selectNewProjectType(NewProjectType.EMPTY_PROJECT);
        boolean isEmptyProjectPageDisplayed;
        if (ideaVersionInt >= 20231) { // For IntelliJ IDEA version 2023.1 and newer
            isEmptyProjectPageDisplayed = newProjectFirstPage.hasText("A basic project with free structure.");
        } else { // For IntelliJ IDEA version 2022.3 and newer
            isEmptyProjectPageDisplayed = newProjectFirstPage.hasText("A basic project that allows working with separate files and compiling Java and Kotlin classes.");
        }
        assertTrue(isEmptyProjectPageDisplayed, "The 'Empty Project' page should be displayed but is not.");

        selectJavaNewProjectType();

        boolean isProjectSDKLabelVisible = !newProjectFirstPage.findAll(JLabelFixture.class, byXpath("//div[@text.key='label.project.wizard.new.project.jdk']")).isEmpty();
        assertTrue(isProjectSDKLabelVisible, "The 'Project SDK:' label should be visible but is not.");
    }

    private void selectJavaNewProjectType() {
        newProjectFirstPage = remoteRobot.find(NewProjectFirstPage.class, Duration.ofSeconds(10));
        newProjectFirstPage.selectNewProjectType(NewProjectType.NEW_PROJECT);
    }

    @Test
    void createEmptyProjectTest() {
        cleanUp();
        String projectName = "empty-test-project";
        CreateCloseUtils.createEmptyProject(remoteRobot, projectName);
        mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(60));
        assertTrue(mainIdeWindow.isShowing(), "The Main IDE Window should be open after creating an empty project.");

        mainIdeWindow.closeProject();
        mainIdeWindow = null;

        // IntelliJ remembers the last chosen project language, for continuity with other tests select Java project
        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.clearWorkspace();
        flatWelcomeFrame.createNewProject();
        selectJavaNewProjectType();
        remoteRobot.find(NewProjectDialogWizard.class).cancel();
    }

    private void navigateToSetProjectNamePage(NewProjectType newProjectType) {
        if (NewProjectType.PLAIN_JAVA.equals(newProjectType)) {
            newProjectFirstPage.setBuildSystem("IntelliJ");
        } else {
            newProjectFirstPage.setBuildSystem(newProjectType.toString());
        }
    }

    private void testProjectNameAndLocationInputField(NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        AbstractNewProjectFinalPage finalPage = CreateCloseUtils.getFinalPage(newProjectDialogWizard, newProjectType);

        String currentProjectName = finalPage.getProjectName();
        String newProjectName = currentProjectName + "1";
        finalPage.setProjectName(newProjectName);
        currentProjectName = finalPage.getProjectName();
        assertEquals(currentProjectName, newProjectName, "Currently set project name doesn't match.");

        String currentProjectLocation = finalPage.getProjectLocation();
        String newProjectLocation = currentProjectLocation + "2";
        finalPage.setProjectLocation(newProjectLocation);
        currentProjectLocation = finalPage.getProjectLocation();
        assertEquals(currentProjectLocation, newProjectLocation, "Currently set project location doesn't match.");
    }

    private void testArtifactCoordinatesAttributes(NewProjectType newProjectType, ArtifactCoordinatesAttributes attribute) {
        navigateToSetProjectNamePage(newProjectType);
        MavenGradleNewProjectFinalPage mavenGradleFinalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));

        mavenGradleFinalPage.openAdvanceSettings();

        String currentValue = "";
        String newValue = "";
        switch (attribute) {
            case GROUP_ID:
                currentValue = mavenGradleFinalPage.getGroupId();
                newValue = currentValue + "1";
                mavenGradleFinalPage.setGroupId(newValue);
                currentValue = mavenGradleFinalPage.getGroupId();
                break;
            case ARTIFACT_ID:
                currentValue = mavenGradleFinalPage.getArtifactId();
                newValue = currentValue + "1";
                mavenGradleFinalPage.setArtifactId(newValue);
                currentValue = mavenGradleFinalPage.getArtifactId();
                break;
        }
        assertEquals(currentValue, newValue, "Currently set '" + attribute + "' doesn't match.");
    }

    private enum ArtifactCoordinatesAttributes {
        GROUP_ID("group ID"),
        ARTIFACT_ID("artifact ID");

        private final String textRepresentation;

        ArtifactCoordinatesAttributes(String textRepresentation) {
            this.textRepresentation = textRepresentation;
        }

        @Override
        public String toString() {
            return this.textRepresentation;
        }
    }
}
