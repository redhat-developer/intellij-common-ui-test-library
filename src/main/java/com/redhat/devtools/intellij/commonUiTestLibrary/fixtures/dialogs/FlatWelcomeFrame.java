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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.*;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.errors.IdeFatalErrorsDialog;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.UtilsKt.hasAnyComponent;
import static com.redhat.devtools.intellij.commonUiTestLibrary.UITestRunner.getIntelliJIdeaVersion;

/**
 * Welcome to IntelliJ IDEA dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "FlatWelcomeFrame type", xpath = "//div[@class='FlatWelcomeFrame']")
@FixtureName(name = "Welcome To IntelliJ IDEA Dialog")
public class FlatWelcomeFrame extends CommonContainerFixture {
    private int intelliJVersion;

    public FlatWelcomeFrame(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.intelliJVersion = getIntelliJIdeaVersion();
    }

    /**
     * Click on the 'New Project' link
     */
    public void createNewProject() {
        // Code for IntelliJ Idea 2020.3 or newer
        if (intelliJVersion >= 203) {
            welcomeFrameLink("New Project").click();
        }
        // Code for IntelliJ Idea 2020.2 or earlier
        else {
            actionLink("New Project").click();
        }
    }

    /**
     * Clear the workspace by deleting the content of the IdeaProjects folder and clearing all the projects' links in the 'Welcome to IntelliJ IDEA' dialog
     */
    public void clearWorkspace() {
        // delete all the projects' links from the 'Welcome to IntelliJ IDEA' dialog
        int numberOfLinks = getNumberOfProjectLinks();
        for (int i = 0; i < numberOfLinks; i++) {
            ComponentFixture recentProjectsList = find(ComponentFixture.class, byXpath("//div[@accessiblename='Recent Projects' and @class='MyList']"), Duration.ofSeconds(10));
            recentProjectsList.runJs("const horizontal_offset = component.getWidth()-22;\n" +
                    "robot.click(component, new Point(horizontal_offset, 22), MouseButton.LEFT_BUTTON, 1);");
            // Code for IntelliJ Idea 2020.3 or newer
            if (intelliJVersion >= 203) {
                JPopupMenuFixture contextMenu = find(JPopupMenuFixture.class, JPopupMenuFixture.Companion.byType(), Duration.ofSeconds(10));
                contextMenu.select("Remove from Recent Projects");
            }
        }

        // delete all the files and folders in the IdeaProjects folder
        try {
            String pathToDirToMakeEmpty = System.getProperty("user.home") + File.separator + "IdeaProjects";
            boolean doesProjectDirExists = Files.exists(Paths.get(pathToDirToMakeEmpty));
            if (doesProjectDirExists) {
                FileUtils.cleanDirectory(new File(pathToDirToMakeEmpty));
            } else {
                Files.createDirectory(Paths.get(pathToDirToMakeEmpty));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear all exceptions
     */
    public void clearExceptions() {
        try {
            ideErrorsIcon().click();
        } catch (WaitForConditionTimeoutException e) {
            e.printStackTrace();
            return;
        }

        IdeFatalErrorsDialog ideFatalErrorsDialog = find(IdeFatalErrorsDialog.class, Duration.ofSeconds(10));
        ideFatalErrorsDialog.clearAll();
    }

    // Works for IntelliJ Idea 2020.3+
    private ComponentFixture welcomeFrameLink(String text) {
        if (hasAnyComponent(this, byXpath("//div[@class='NewRecentProjectPanel']"))) {
            return find(ComponentFixture.class, byXpath("//div[@class='JBOptionButton' and @text='" + text + "']"));
        }
        return find(ComponentFixture.class, byXpath("//div[@class='NonOpaquePanel'][./div[@text='" + text + "']]//div[@class='JButton']"));
    }

    private int getNumberOfProjectLinks() {
        try {
            ComponentFixture recentProjectsList = find(ComponentFixture.class, byXpath("//div[@accessiblename='Recent Projects' and @class='MyList']"), Duration.ofSeconds(10));
            int numberOfProjectsLinks = recentProjectsList.findAllText().size() / 2;    // 2 items per 1 project link (project path and project name)
            return numberOfProjectsLinks;
        } catch (WaitForConditionTimeoutException e) {
            // the list with accessible name 'Recent Projects' is not available -> 0 links in the 'Welcome to IntelliJ IDEA' dialog
            return 0;
        }
    }

    private ComponentFixture ideErrorsIcon() {
        return find(ComponentFixture.class, byXpath("//div[@class='IdeErrorsIcon']"), Duration.ofSeconds(10));
    }
}