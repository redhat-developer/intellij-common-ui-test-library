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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.dialogs;

import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.NewProjectDialogWizard;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ProjectLocation;
import com.redhat.devtools.intellij.commonuitest.utils.constants.UITestTimeouts;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.internalerror.IdeInternalErrorUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileFilter;
import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FlatWelcomeFrame test
 *
 * @author zcervink@redhat.com
 */
class FlatWelcomeFrameTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "welcome_frame_java_project";
    private FlatWelcomeFrame flatWelcomeFrame;

    @AfterEach
    void cleanUp() {
        flatWelcomeFrame.clearWorkspace();
        flatWelcomeFrame.clearExceptions();
    }

    @Test
    void createNewProjectLinkTest() {
        flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, UITestTimeouts.FIXTURE_TIMEOUT);
        flatWelcomeFrame.createNewProject();
        NewProjectDialogWizard newProjectDialogWizard = remoteRobot.find(NewProjectDialogWizard.class, UITestTimeouts.FIXTURE_TIMEOUT);
        newProjectDialogWizard.cancel();
        assertTrue(flatWelcomeFrame.isShowing(), "The Welcome Window should be open.");
    }

    @Test
    void clearWorkspaceTest() {
        prepareWorkspace(PROJECT_NAME);
        flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, UITestTimeouts.FIXTURE_TIMEOUT);
        flatWelcomeFrame.clearExceptions();
        int projectsOnDisk = getNumberOfProjectsOnDisk();
        int projectLinks = getNumberOfProjectLinks();
        assertEquals(1, projectsOnDisk, "Number of projects in the IntelliJ's project folder should be 1 but is " + projectsOnDisk + ".");
        assertEquals(1, projectLinks, "Number of projects' links in the IntelliJ's 'Welcome Frame Dialog' should be 1 but is " + projectLinks + ".");
        IdeInternalErrorUtils.clearWindowsErrorsIfTheyAppear(remoteRobot);
        flatWelcomeFrame.clearWorkspace();
        int projectCount2 = getNumberOfProjectsOnDisk();
        int projectLinks2 = getNumberOfProjectLinks();
        assertEquals(0, projectCount2, "Number of projects in the IntelliJ's project folder should be 0 but is " + projectCount2 + ".");
        assertEquals(0, projectLinks2, "Number of projects' links in the IntelliJ's 'Welcome Frame Dialog' should be 0 but is " + projectLinks2 + ".");
    }

    @Test
    void clearExceptionsTest() {
        prepareWorkspace(PROJECT_NAME);
        flatWelcomeFrame = remoteRobot.find(FlatWelcomeFrame.class, UITestTimeouts.FIXTURE_TIMEOUT);
        flatWelcomeFrame.clearExceptions();
        assertTrue(flatWelcomeFrame.isShowing(), "The Welcome Window should be open.");
    }

    private int getNumberOfProjectsOnDisk() {
        String pathToIdeaProjectsFolder = ProjectLocation.PROJECT_LOCATION;
        File[] files = new File(pathToIdeaProjectsFolder).listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
        if (files != null) {
            return files.length;
        } else {
            return 0;   // files is null (e.g., folder doesn't exist)
        }
    }

    private int getNumberOfProjectLinks() {
        try {
            JTreeFixture projects = remoteRobot.findAll(JTreeFixture.class, byXpath(XPathDefinitions.RECENT_PROJECT_PANEL_NEW_2)).get(0);
            return projects.findAllText().size() / 2;
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }
}