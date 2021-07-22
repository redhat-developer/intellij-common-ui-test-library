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

import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.NewProjectDialog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NewProjectDialog test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
public class NewProjectDialogTest extends LibraryTestBase {
    @Test
    public void newProjectDialogJavaTest() {
        openNewProjectDialogFromWelcomeDialog();
        NewProjectDialog newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
        newProjectDialog.selectNewProjectType("Java");
        newProjectDialog.next();
        newProjectDialog.next();
        String javaProjectName = "java_project_name_test";
        newProjectDialog.setProjectNameForJavaProject(javaProjectName);
        String javaProjectNameFromInputField = remoteRobot.findAll(JTextFieldFixture.class, byXpath("//div[@accessiblename='Project name:' and @class='JTextField']")).get(0).getText();
        assertTrue(javaProjectName.equals(javaProjectNameFromInputField), "Project name in the input field (" + javaProjectNameFromInputField + ") is different from the expected project name (" + javaProjectName + ").");
        newProjectDialog.previous();
        newProjectDialog.previous();
        newProjectDialog.cancel();
    }

    @Test
    public void newProjectDialogMavenTest() {
        openNewProjectDialogFromWelcomeDialog();
        NewProjectDialog newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
        newProjectDialog.selectNewProjectType("Maven");
        newProjectDialog.next();
        String mavenProjectName = "maven_project_name_test";
        newProjectDialog.setProjectNameForMavenOrGradleProject(mavenProjectName);
        String mavenProjectNameFromInputField = remoteRobot.find(JTextFieldFixture.class, byXpath("//div[@class='JBTextField']")).getText();
        assertTrue(mavenProjectName.equals(mavenProjectNameFromInputField), "Project name in the input field (" + mavenProjectNameFromInputField + ") is different from the expected project name (" + mavenProjectName + ").");
        newProjectDialog.previous();
        newProjectDialog.cancel();
    }

    @Test
    public void newProjectDialogGradleTest() {
        openNewProjectDialogFromWelcomeDialog();
        NewProjectDialog newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
        newProjectDialog.selectNewProjectType("Gradle");
        newProjectDialog.next();
        String gradleProjectName = "gradle_project_name_test";
        newProjectDialog.setProjectNameForMavenOrGradleProject(gradleProjectName);
        String gradleProjectNameFromInputField = remoteRobot.find(JTextFieldFixture.class, byXpath("//div[@class='JBTextField']")).getText();
        assertTrue(gradleProjectName.equals(gradleProjectNameFromInputField), "Project name in the input field (" + gradleProjectNameFromInputField + ") is different from the expected project name (" + gradleProjectName + ").");
        newProjectDialog.previous();
        newProjectDialog.cancel();
    }
}
