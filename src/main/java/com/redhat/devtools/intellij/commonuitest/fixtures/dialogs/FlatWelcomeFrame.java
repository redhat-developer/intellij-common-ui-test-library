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
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.fixtures.JListFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.intellij.remoterobot.utils.UtilsKt;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.errors.IdeFatalErrorsDialog;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Welcome to IntelliJ IDEA dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "FlatWelcomeFrame type", xpath = "//div[@class='FlatWelcomeFrame']")
@FixtureName(name = "Welcome To IntelliJ IDEA Dialog")
public class FlatWelcomeFrame extends CommonContainerFixture {
    private static final Logger LOGGER = Logger.getLogger(FlatWelcomeFrame.class.getName());
    private UITestRunner.IdeaVersion intelliJVersion;

    public FlatWelcomeFrame(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.intelliJVersion = UITestRunner.getIdeaVersion();
    }

    /**
     * Click on the 'New Project' link
     */
    public void createNewProject() {
        clickOnLink("New Project");
    }

    /**
     * Click on the link according to given label
     *
     * @param label label of the link to click on
     */
    public void clickOnLink(String label) {
        // Code for IntelliJ IDEA 2020.3 or newer
        if (intelliJVersion.toInt() >= 20203) {
            welcomeFrameLink(label).click();
        }
        // Code for IntelliJ IDEA 2020.2 or earlier
        else {
            actionLink(label).click();
        }
    }

    /**
     * Clear the workspace by deleting the content of the IdeaProjects folder and clearing all the projects' links in the 'Welcome to IntelliJ IDEA' dialog
     */
    public void clearWorkspace() {
        List<JListFixture> jListFixtures = jLists(byXpath("//div[@accessiblename='Recent Projects']"));
        while (!jListFixtures.isEmpty() && jListFixtures.get(0).findAllText().size() != 1) {
            JListFixture recentProjectsList = jListFixtures.get(0);
            recentProjectsList.runJs("const horizontal_offset = component.getWidth()-22;\n" +
                    "robot.click(component, new Point(horizontal_offset, 22), MouseButton.LEFT_BUTTON, 1);");
            // Code for IntelliJ Idea 2020.3 or newer
            if (intelliJVersion.toInt() >= 20203) {
                JPopupMenuFixture contextMenu = jPopupMenus(JPopupMenuFixture.Companion.byType()).get(0);
                contextMenu.select("Remove from Recent Projects");
            }
        }

        try {
            String pathToDirToMakeEmpty = System.getProperty("user.home") + File.separator + "IdeaProjects";
            boolean doesProjectDirExists = Files.exists(Paths.get(pathToDirToMakeEmpty));
            if (doesProjectDirExists) {
                FileUtils.cleanDirectory(new File(pathToDirToMakeEmpty));
            } else {
                Files.createDirectory(Paths.get(pathToDirToMakeEmpty));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Clear all exceptions
     */
    public void clearExceptions() {
        try {
            ideErrorsIcon().click();
        } catch (WaitForConditionTimeoutException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        find(IdeFatalErrorsDialog.class, Duration.ofSeconds(10)).clearAll();
    }

    // Works for IntelliJ Idea 2020.3+
    private JButtonFixture welcomeFrameLink(String label) {
        if (UtilsKt.hasAnyComponent(this, byXpath("//div[@class='NewRecentProjectPanel']"))) {
            return button(byXpath("//div[@class='JBOptionButton' and @text='" + label + "']"), Duration.ofSeconds(2));
        }
        return button(byXpath("//div[@class='NonOpaquePanel'][./div[@text='" + label + "']]"), Duration.ofSeconds(2));
    }

    private ComponentFixture ideErrorsIcon() {
        return find(ComponentFixture.class, byXpath("//div[@class='IdeErrorsIcon']"), Duration.ofSeconds(10));
    }
}