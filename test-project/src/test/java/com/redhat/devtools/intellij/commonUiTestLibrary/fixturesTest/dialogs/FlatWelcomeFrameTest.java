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

import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void prepareProject() {
        createNewProject(projectName, "Java");
    }

    @Test
    public void flatWelcomeFrameTest() {
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
        mainIdeWindow.closeProject();

        FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
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

    private int getNumberOfProjectsOnDisk() {
        String pathToIdeaProjectsFolder = System.getProperty("user.home") + File.separator + "IdeaProjects";
        File[] files = new File(pathToIdeaProjectsFolder).listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
        return files.length;
    }

    private int getNumberOfProjectLinksInFlatWelcomeFrame() {
        try {
            FlatWelcomeFrame flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));
            ComponentFixture recentProjectsList = flatWelcomeFrame.find(ComponentFixture.class, byXpath("//div[@accessiblename='Recent Projects' and @class='MyList']"));
            int numberOfProjectsLinks = recentProjectsList.findAllText().size() / 2;    // 2 items per 1 project link (project path and project name)
            return numberOfProjectsLinks;
        } catch (WaitForConditionTimeoutException e) {
            // the list with accessible name 'Recent Projects' is not available -> 0 links in the 'Welcome to IntelliJ IDEA' dialog
            return 0;
        }
    }
}