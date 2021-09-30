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

    @Test
    public void newProjectDialogJavaTest() {
        testProjectDialog(NewProjectType.PLAIN_JAVA, plainJavaProjectName);
    }

    @Test
    public void newProjectDialogMavenTest() {
        testProjectDialog(NewProjectType.MAVEN, mavenProjectName);
    }

    @Test
    public void newProjectDialogGradleTest() {
        testProjectDialog(NewProjectType.GRADLE, gradleProjectName);
    }

    private void testProjectDialog(NewProjectType newProjectType, String projectName) {
        openNewProjectDialogFromWelcomeDialog();
        NewProjectDialog newProjectDialog = remoteRobot.find(NewProjectDialog.class, Duration.ofSeconds(10));
        newProjectDialog.selectNewProjectType(newProjectType.toString());
        newProjectDialog.next();

        String projectNameFromInputField;
        if (newProjectType == NewProjectType.PLAIN_JAVA) {
            newProjectDialog.next();
        }
        newProjectDialog.setProjectName(projectName);
        projectNameFromInputField = remoteRobot.findAll(JTextFieldFixture.class, JTextFieldFixture.Companion.byType()).get(0).getText();
        assertTrue(projectName.equals(projectNameFromInputField), "Project name in the input field (" + projectNameFromInputField + ") is different from the expected project name (" + projectName + ").");

        newProjectDialog.previous();
        if (newProjectType == NewProjectType.PLAIN_JAVA) {
            newProjectDialog.previous();
        }
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
