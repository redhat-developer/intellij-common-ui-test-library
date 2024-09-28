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

import com.intellij.remoterobot.fixtures.*;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.AbstractNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.JavaNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.JavaNewProjectSecondPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.MavenGradleNewProjectFinalPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages.NewProjectFirstPage;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.idestatusbar.IdeStatusBar;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.screenshot.ScreenshotUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils.listOfRemoteTextToString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * NewProjectDialog wizard and pages test
 *
 * @author zcervink@redhat.com
 */
public class NewProjectDialogTest extends LibraryTestBase {
    private static final String PLAIN_JAVA_PROJECT_NAME = "plain_java_project_name_test";
    private static final String MORE_SETTINGS_SHOULD_BE_VISIBLE = "The 'More Settings' should be visible.";
    private static final String MORE_SETTINGS_SHOULD_BE_HIDDEN = "The 'More Settings' should be hidden.";
    private static final String ADVANCED_SETTINGS_SHOULD_BE_OPENED_MSG = "The 'Advanced Settings' section should be opened but is not";
    private static final String ADVANCED_SETTINGS_SHOULD_BE_CLOSED_MSG = "The 'Advanced Settings' section should be closed but is not";
    private static final String BUT_IS = "but is";

    private NewProjectDialogWizard newProjectDialogWizard;
    private NewProjectFirstPage newProjectFirstPage;
    private MainIdeWindow mainIdeWindow;

    @BeforeEach
    public void openNewProjectDialog() {
        newProjectDialogWizard = CreateCloseUtils.openNewProjectDialogFromWelcomeDialog(remoteRobot);
        newProjectFirstPage = newProjectDialogWizard.find(NewProjectFirstPage.class, Duration.ofSeconds(10));
    }

    @AfterEach
    public void cleanUp() {
        if (mainIdeWindow != null) {
            // tests ending with opened Main Ide Window needs to close the project and clear workspace
            IdeStatusBar ideStatusBar = mainIdeWindow.find(IdeStatusBar.class, Duration.ofSeconds(10));
            ideStatusBar.waitUntilProjectImportIsComplete();
            mainIdeWindow.maximizeIdeWindow();
            ideStatusBar.waitUntilAllBgTasksFinish();
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
    public void setProjectNamePlainJavaProjectTest() {
        testProjectNameAndLocationInputField(CreateCloseUtils.NewProjectType.PLAIN_JAVA);
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void setProjectNameMavenProjectTest() {
        testProjectNameAndLocationInputField(CreateCloseUtils.NewProjectType.MAVEN);
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void setProjectNameGradleProjectTest() {
        testProjectNameAndLocationInputField(CreateCloseUtils.NewProjectType.GRADLE);
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void openMoreSettingsTest() {
        navigateToSetProjectNamePage(CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.closeMoreSettings();
        assertFalse(isMoreSettingsOpened(javaFinalPage), MORE_SETTINGS_SHOULD_BE_HIDDEN);
        javaFinalPage.openMoreSettings();
        assertTrue(isMoreSettingsOpened(javaFinalPage), MORE_SETTINGS_SHOULD_BE_VISIBLE);
        javaFinalPage.openMoreSettings();
        assertTrue(isMoreSettingsOpened(javaFinalPage), MORE_SETTINGS_SHOULD_BE_VISIBLE);
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void closeMoreSettingsTest() {
        navigateToSetProjectNamePage(CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openMoreSettings();
        assertTrue(isMoreSettingsOpened(javaFinalPage), MORE_SETTINGS_SHOULD_BE_VISIBLE);
        javaFinalPage.closeMoreSettings();
        assertFalse(isMoreSettingsOpened(javaFinalPage), MORE_SETTINGS_SHOULD_BE_HIDDEN);
        javaFinalPage.closeMoreSettings();
        assertFalse(isMoreSettingsOpened(javaFinalPage), MORE_SETTINGS_SHOULD_BE_HIDDEN);
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void openAdvancedSettingsTest() {
        newProjectFirstPage.closeAdvanceSettings();
        assertFalse(isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_CLOSED_MSG);
        newProjectFirstPage.openAdvanceSettings();
        assertTrue(isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_OPENED_MSG);
        newProjectFirstPage.openAdvanceSettings();
        assertTrue(isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_OPENED_MSG);
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void closeAdvancedSettingsTest() {
        newProjectFirstPage.openAdvanceSettings();
        assertTrue(isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_OPENED_MSG);
        newProjectFirstPage.closeAdvanceSettings();
        assertFalse(isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_CLOSED_MSG);
        newProjectFirstPage.closeAdvanceSettings();
        assertFalse(isAdvancedSettingsOpened(), ADVANCED_SETTINGS_SHOULD_BE_CLOSED_MSG);
    }

    @Test
    public void getSetModuleNameTest() {
        navigateToSetProjectNamePage(CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            javaFinalPage.openAdvanceSettings();
        } else {
            javaFinalPage.openMoreSettings();
        }

        String currentModuleName = javaFinalPage.getModuleName();
        String newModuleName = currentModuleName + "1";
        javaFinalPage.setModuleName(newModuleName);
        currentModuleName = javaFinalPage.getModuleName();
        assertTrue(currentModuleName.equals(newModuleName), "Currently set module name should be '" + newModuleName + BUT_IS + currentModuleName + "'.");
    }

    @Test
    public void getSetContentRootTest() {
        navigateToSetProjectNamePage(CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            javaFinalPage.openAdvanceSettings();
        } else {
            javaFinalPage.openMoreSettings();
        }

        String currentContentRoot = javaFinalPage.getContentRoot();
        String newContentRoot = currentContentRoot + "1";
        javaFinalPage.setContentRoot(newContentRoot);
        currentContentRoot = javaFinalPage.getContentRoot();
        assertTrue(currentContentRoot.equals(newContentRoot), "Currently set content root location should be '" + newContentRoot + BUT_IS + currentContentRoot + "'.");
    }

    @Test
    public void getSetModuleFileLocationTest() {
        navigateToSetProjectNamePage(CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            javaFinalPage.openAdvanceSettings();
        } else {
            javaFinalPage.openMoreSettings();
        }

        String currentModuleFileLocation = javaFinalPage.getModuleFileLocation();
        String newModuleFileLocation = currentModuleFileLocation + "1";
        javaFinalPage.setModuleFileLocation(newModuleFileLocation);
        currentModuleFileLocation = javaFinalPage.getModuleFileLocation();
        assertTrue(currentModuleFileLocation.equals(newModuleFileLocation), "Currently set module file location should be '" + newModuleFileLocation + BUT_IS + currentModuleFileLocation + "'.");
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void getSetProjectFormat() {
        navigateToSetProjectNamePage(CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        JavaNewProjectFinalPage javaFinalPage = newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10));
        javaFinalPage.openMoreSettings();

        javaFinalPage.setProjectFormat(AbstractNewProjectFinalPage.ProjectFormatType.IPR_FILE_BASED);
        AbstractNewProjectFinalPage.ProjectFormatType currentlySetProjectFormatType = javaFinalPage.getProjectFormat();
        assertTrue(currentlySetProjectFormatType.equals(AbstractNewProjectFinalPage.ProjectFormatType.IPR_FILE_BASED), "Currently set value in the 'Project format' combo box should be '" + AbstractNewProjectFinalPage.ProjectFormatType.IPR_FILE_BASED + BUT_IS + currentlySetProjectFormatType + "'.");
        javaFinalPage.setProjectFormat(AbstractNewProjectFinalPage.ProjectFormatType.IDEA_DIRECTORY_BASED);
        currentlySetProjectFormatType = javaFinalPage.getProjectFormat();
        assertTrue(currentlySetProjectFormatType.equals(AbstractNewProjectFinalPage.ProjectFormatType.IDEA_DIRECTORY_BASED), "Currently set value in the 'Project format' combo box should be '" + AbstractNewProjectFinalPage.ProjectFormatType.IDEA_DIRECTORY_BASED + BUT_IS + currentlySetProjectFormatType + "'.");
    }

    @Test
    public void openArtifactCoordinatesMavenTest() {
        testOpenArtifactCoordinatesMavenGradle(CreateCloseUtils.NewProjectType.MAVEN);
    }

    @Test
    public void openArtifactCoordinatesGradleTest() {
        testOpenArtifactCoordinatesMavenGradle(CreateCloseUtils.NewProjectType.GRADLE);
    }

    @Test
    public void getSetGroupIdMavenTest() {
        testArtifactCoordinatesAttributes(CreateCloseUtils.NewProjectType.MAVEN, ArtifactCoordinatesAttributes.GROUP_ID);
    }

    @Test
    public void getSetGroupIdGradleTest() {
        testArtifactCoordinatesAttributes(CreateCloseUtils.NewProjectType.GRADLE, ArtifactCoordinatesAttributes.GROUP_ID);
    }

    @Test
    public void getSetArtifactIdMavenTest() {
        testArtifactCoordinatesAttributes(CreateCloseUtils.NewProjectType.MAVEN, ArtifactCoordinatesAttributes.ARTIFACT_ID);
    }

    @Test
    public void getSetArtifactIdGradleTest() {
        testArtifactCoordinatesAttributes(CreateCloseUtils.NewProjectType.GRADLE, ArtifactCoordinatesAttributes.ARTIFACT_ID);
    }

    @Test
    public void getSetVersionMavenTest() {
        testArtifactCoordinatesAttributes(CreateCloseUtils.NewProjectType.MAVEN, ArtifactCoordinatesAttributes.VERSION);
    }

    @Test
    public void getSetVersionGradleTest() {
        testArtifactCoordinatesAttributes(CreateCloseUtils.NewProjectType.GRADLE, ArtifactCoordinatesAttributes.VERSION);
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void toggleFromTemplateTest() {
        newProjectFirstPage.selectNewProjectType(CreateCloseUtils.NewProjectType.PLAIN_JAVA.toString());
        newProjectDialogWizard.next();
        JavaNewProjectSecondPage javaNewProjectSecondPage = newProjectDialogWizard.find(JavaNewProjectSecondPage.class, Duration.ofSeconds(10));
        boolean isSelected = javaNewProjectSecondPage.fromTemplateCheckBox().isSelected();
        if (isSelected) {
            javaNewProjectSecondPage.fromTemplateCheckBox().setValue(false);
        }
        javaNewProjectSecondPage.toggleFromTemplate(true);
        assertTrue(javaNewProjectSecondPage.fromTemplateCheckBox().isSelected(), "The 'Create project from template' checkbox should be checked " + BUT_IS + " not.");
        javaNewProjectSecondPage.fromTemplateCheckBox().setValue(isSelected);
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void previousButtonTest() {
        newProjectFirstPage.selectNewProjectType(CreateCloseUtils.NewProjectType.PLAIN_JAVA.toString());
        newProjectFirstPage.setProjectSdkIfAvailable("11");
        assertThrows(UITestException.class, () ->
                newProjectDialogWizard.previous(), "The 'UITestException' should be thrown because the 'Previous' button is not enabled on the first page of the 'New Project'.");
        newProjectDialogWizard.next();
        boolean isCommandLineAppTextPresent = listOfRemoteTextToString(newProjectFirstPage.findAllText()).contains("Command Line App");
        assertTrue(isCommandLineAppTextPresent, "The 'Command Line App' text should be present on the second page of the 'New Project' wizard for java project.");
        newProjectDialogWizard.previous();
        try {
            newProjectFirstPage.comboBox(byXpath(XPathDefinitions.JDK_COMBOBOX), Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            fail("The 'Project SDK' should be available " + BUT_IS + " not.");
        }
    }

    @Test
    @EnabledIfSystemProperty(named = "uitestlib.idea.version", matches = "2020.|2021.")
    public void nextButtonTest() {
        newProjectFirstPage.selectNewProjectType(CreateCloseUtils.NewProjectType.PLAIN_JAVA.toString());
        newProjectFirstPage.setProjectSdkIfAvailable("11");
        newProjectDialogWizard.next();
        boolean isCommandLineAppTextPresent = listOfRemoteTextToString(newProjectFirstPage.findAllText()).contains("Command Line App");
        assertTrue(isCommandLineAppTextPresent, "The 'Command Line App' text should be present on the second page of the 'New Project' wizard for java project.");
        newProjectDialogWizard.next();
        assertThrows(UITestException.class, () ->
                newProjectDialogWizard.next(), "The 'UITestException' should be thrown because the 'Next' button is not available on the last page of the 'New Project' wizard.");
    }

    @Test
    public void finishButtonTest() {
        if (UITestRunner.getIdeaVersionInt() < 20221) {
            newProjectFirstPage.selectNewProjectType(CreateCloseUtils.NewProjectType.PLAIN_JAVA.toString());
            newProjectFirstPage.setProjectSdkIfAvailable("17");
            assertThrows(UITestException.class, () ->
                    newProjectDialogWizard.finish(), "The 'UITestException' should be thrown because the 'Finish' button is not available on the first page of the 'New Project' wizard for java project.");
            newProjectDialogWizard.next();
            newProjectDialogWizard.next();
        }

        assertThrows(UITestException.class, () ->
                newProjectDialogWizard.next(), "The 'UITestException' should be thrown because the 'Next' button is not available on the last page of the 'New Project' wizard.");
        newProjectDialogWizard.find(JavaNewProjectFinalPage.class, Duration.ofSeconds(10)).setProjectName(PLAIN_JAVA_PROJECT_NAME);
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
        if (ideaVersionInt >= 20242 && remoteRobot.isWin()) {
            newProjectFirstPage.setProjectSdkIfAvailable("Download JDK");
            try {
                ContainerFixture downloadJdkDialog = remoteRobot.find(ContainerFixture.class, byXpath("//div[@title='Download JDK']"), Duration.ofSeconds(10));
                downloadJdkDialog.find(ActionButtonFixture.class, byXpath(XPathDefinitions.label(ButtonLabels.CANCEL_LABEL)), Duration.ofSeconds(5)).click();
            } catch (WaitForConditionTimeoutException e) {
                fail("Download JDK button was not pressed and Download JDK dialog was not found");
            }
        } else {
            newProjectFirstPage.setProjectSdkIfAvailable("11");
            ComboBoxFixture projectJdkComboBox = newProjectFirstPage.getProjectJdkComboBox();
            String currentlySelectedProjectSdk = listOfRemoteTextToString(projectJdkComboBox.findAllText());
            assertTrue(currentlySelectedProjectSdk.contains("11"), "Selected project SDK should be Java 11 but is '" + currentlySelectedProjectSdk + "'");
            newProjectFirstPage.setProjectSdkIfAvailable("17");
            currentlySelectedProjectSdk = listOfRemoteTextToString(projectJdkComboBox.findAllText());
            assertTrue(currentlySelectedProjectSdk.contains("17"), "Selected project SDK should be Java 17 but is '" + currentlySelectedProjectSdk + "'");
        }
    }

    @Test
    public void selectNewProjectTypeTest() {
        newProjectFirstPage.selectNewProjectType("Empty Project");
        boolean isEmptyProjectPageDisplayed;
        if (ideaVersionInt >= 20231) {          // For IntelliJ IDEA version 2023.1 and newer
            isEmptyProjectPageDisplayed = newProjectFirstPage.hasText("A basic project with free structure.");
        } else if (ideaVersionInt >= 20221) {   // For IntelliJ IDEA version 2022.1 and newer
            isEmptyProjectPageDisplayed = newProjectFirstPage.hasText("A basic project that allows working with separate files and compiling Java and Kotlin classes.");
        } else if (ideaVersionInt == 20213) {   // For IntelliJ IDEA version 2021.3
            isEmptyProjectPageDisplayed = newProjectFirstPage.hasText("Simple project with one module");
        } else {                                // For IntelliJ IDEA version 2021.2 and older
            isEmptyProjectPageDisplayed = !newProjectFirstPage.findAll(
                    JListFixture.class, byXpath(XPathDefinitions.EMPTY_PROJECT)
            ).isEmpty();
        }
        assertTrue(isEmptyProjectPageDisplayed, "The 'Empty Project' page should be displayed but is not.");

        selectJavaNewProjectType();

        boolean isProjectSDKLabelVisible;
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            isProjectSDKLabelVisible = !newProjectFirstPage.findAll(JLabelFixture.class, byXpath("//div[@text.key='label.project.wizard.new.project.jdk']")).isEmpty();
        } else {
            isProjectSDKLabelVisible = !newProjectFirstPage.findAll(JLabelFixture.class, byXpath("//div[@text='Project SDK:']")).isEmpty();
        }
        assertTrue(isProjectSDKLabelVisible, "The 'Project SDK:' label should be visible but is not.");
    }

    private void selectJavaNewProjectType() {
        newProjectFirstPage = remoteRobot.find(NewProjectFirstPage.class, Duration.ofSeconds(10));
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            newProjectFirstPage.selectNewProjectType("New Project");
        } else {
            newProjectFirstPage.selectNewProjectType("Java");
        }
    }

    @Test
    public void createEmptyProjectTest() {
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

    private void navigateToSetProjectNamePage(CreateCloseUtils.NewProjectType newProjectType) {
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            newProjectFirstPage.setBuildSystem(newProjectType.toString().equals("Java") ? "IntelliJ" : newProjectType.toString());
            return;
        }
        newProjectFirstPage.selectNewProjectType(newProjectType.toString());
        newProjectDialogWizard.next();
        if (newProjectType == CreateCloseUtils.NewProjectType.PLAIN_JAVA) {
            newProjectDialogWizard.next();
        }
    }

    private void testProjectNameAndLocationInputField(CreateCloseUtils.NewProjectType newProjectType) {
        navigateToSetProjectNamePage(newProjectType);
        AbstractNewProjectFinalPage finalPage = CreateCloseUtils.getFinalPage(newProjectDialogWizard, newProjectType);

        String currentProjectName = finalPage.getProjectName();
        String newProjectName = currentProjectName + "1";
        finalPage.setProjectName(newProjectName);
        currentProjectName = finalPage.getProjectName();
        assertTrue(currentProjectName.equals(newProjectName), "Currently set project name should be '" + newProjectName + BUT_IS + currentProjectName + "'.");

        String currentProjectLocation = finalPage.getProjectLocation();
        String newProjectLocation = currentProjectLocation + "2";
        finalPage.setProjectLocation(newProjectLocation);
        currentProjectLocation = finalPage.getProjectLocation();
        assertTrue(currentProjectLocation.equals(newProjectLocation), "Currently set project location should be '" + newProjectLocation + BUT_IS + currentProjectLocation + "'.");
    }

    private void testOpenArtifactCoordinatesMavenGradle(CreateCloseUtils.NewProjectType newProjectType) {
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            return;
        }
        navigateToSetProjectNamePage(newProjectType);
        MavenGradleNewProjectFinalPage mavenGradleFinalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));
        mavenGradleFinalPage.closeArtifactCoordinates();
        mavenGradleFinalPage.openArtifactCoordinates();
        assertTrue(isArtifactCoordinatesOpened(mavenGradleFinalPage), "The 'Artifact Coordinates' settings should be visible.");
        mavenGradleFinalPage.openArtifactCoordinates();
        assertTrue(isArtifactCoordinatesOpened(mavenGradleFinalPage), "The 'Artifact Coordinates' settings should be visible.");
    }

    private void testArtifactCoordinatesAttributes(CreateCloseUtils.NewProjectType newProjectType, ArtifactCoordinatesAttributes attribute) {
        if (UITestRunner.getIdeaVersionInt() >= 20221 && attribute == ArtifactCoordinatesAttributes.VERSION) {
            return;
        }

        navigateToSetProjectNamePage(newProjectType);
        MavenGradleNewProjectFinalPage mavenGradleFinalPage = newProjectDialogWizard.find(MavenGradleNewProjectFinalPage.class, Duration.ofSeconds(10));

        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            mavenGradleFinalPage.openAdvanceSettings();
        } else {
            mavenGradleFinalPage.openArtifactCoordinates();
        }

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
            case VERSION:
                currentValue = mavenGradleFinalPage.getVersion();
                newValue = currentValue + "1";
                mavenGradleFinalPage.setVersion(newValue);
                currentValue = mavenGradleFinalPage.getVersion();
                break;
        }
        assertTrue(currentValue.equals(newValue), "Currently set '" + attribute + "' should be '" + newValue + BUT_IS + currentValue + "'.");
    }

    private boolean isMoreSettingsOpened(JavaNewProjectFinalPage javaFinalPage) {
        return javaFinalPage.findAll(ContainerFixture.class, byXpath(XPathDefinitions.MORE_SETTINGS_TITLED_SEPARATOR)).size() == 2;
    }

    private boolean isArtifactCoordinatesOpened(MavenGradleNewProjectFinalPage mavenGradleFinalPage) {
        List<ContainerFixture> cf = mavenGradleFinalPage.findAll(ContainerFixture.class, byXpath(XPathDefinitions.ARTIFACTS_COORDINATES_DIALOG_PANEL));
        return cf.size() > 5;
    }

    private boolean isAdvancedSettingsOpened() {
        List<ComponentFixture> ss = newProjectFirstPage.findAll(ComponentFixture.class, byXpath("//div[@class='CollapsibleTitledSeparator']/../*"));
        for (int i = 0; i < ss.size(); i++) {
            if (listOfRemoteTextToString(ss.get(i).findAllText()).contains("Advanced Settings")) {
                return i != ss.size() - 1;
            }
        }
        throw new UITestException("Wizard does not contain 'Advanced Settings' section.");
    }

    private enum ArtifactCoordinatesAttributes {
        GROUP_ID("group ID"),
        ARTIFACT_ID("artifact ID"),
        VERSION("version");

        private final String textReperentation;

        ArtifactCoordinatesAttributes(String textRepresentation) {
            this.textReperentation = textRepresentation;
        }

        @Override
        public String toString() {
            return this.textReperentation;
        }
    }
}
