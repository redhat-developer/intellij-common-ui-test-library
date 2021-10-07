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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.dialogs;

import com.intellij.remoterobot.fixtures.JListFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.NewProjectDialogFirstPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.FileFilter;
import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FlatWelcomeFrame test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
class FlatWelcomeFrameTest extends LibraryTestBase {
    private final String projectName = "welcome_frame_java_project";
    private FlatWelcomeFrame flatWelcomeFrame;

    @AfterEach
    public void cleanUp() {
        flatWelcomeFrame.clearWorkspace();
    }

    @Test
    public void createNewProjectLinkTest() {
        flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.createNewProject();
        NewProjectDialogWizard newProjectDialogWizard = remoteRobot.find(NewProjectDialogWizard.class, Duration.ofSeconds(10));
        newProjectDialogWizard.find(NewProjectDialogFirstPage.class, Duration.ofSeconds(10)).cancel();
    }

    @Test
    public void clearWorkspaceTest() {
        prepareWorkspace();
        flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        int projectsOnDisk = getNumberOfProjectsOnDisk();
        int projectLinks = getNumberOfProjectLinksInFlatWelcomeFrame();
        assertTrue(projectsOnDisk == 1, "Number of projects in the IntelliJ's project folder should be 1 but is " + projectsOnDisk + ".");
        assertTrue(projectLinks == 1, "Number of projects' links in the IntelliJ's 'Welcome Frame Dialog' should be 1 but is " + projectLinks + ".");
        flatWelcomeFrame.clearWorkspace();
        int projectCount2 = getNumberOfProjectsOnDisk();
        int projectLinks2 = getNumberOfProjectLinksInFlatWelcomeFrame();
        assertTrue(projectCount2 == 0, "Number of projects in the IntelliJ's project folder should be 0 but is " + projectCount2 + ".");
        assertTrue(projectLinks2 == 0, "Number of projects' links in the IntelliJ's 'Welcome Frame Dialog' should be 0 but is " + projectLinks2 + ".");
    }

    @Test
    public void clearExceptionsTest() {
        prepareWorkspace();
        flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
        flatWelcomeFrame.clearExceptions();
    }

    private void prepareWorkspace() {
        createNewProject(projectName, "Java");
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
        mainIdeWindow.closeProject();
    }

    private int getNumberOfProjectsOnDisk() {
        String pathToIdeaProjectsFolder = System.getProperty("user.home") + File.separator + "IdeaProjects";
        File[] files = new File(pathToIdeaProjectsFolder).listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
        return files.length;
    }

    private int getNumberOfProjectLinksInFlatWelcomeFrame() {
        try {
            FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
            JListFixture recentProjectsList = flatWelcomeFrame.find(JListFixture.class, byXpath("//div[@accessiblename='Recent Projects']"), Duration.ofSeconds(10));
            int numberOfProjectsLinks = recentProjectsList.findAllText().size() / 2;    // 2 items per 1 project link (project path and project name)
            return numberOfProjectsLinks;
        } catch (WaitForConditionTimeoutException e) {
            // the list with accessible name 'Recent Projects' is not available -> 0 links in the 'Welcome to IntelliJ IDEA' dialog
            return 0;
        }
    }
}