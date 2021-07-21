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
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.other.ActionLink;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.TextHelper;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * Welcome to IntelliJ IDEA dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "FlatWelcomeFrame type", xpath = "//div[@class='FlatWelcomeFrame']")
@FixtureName(name = "Welcome To IntelliJ IDEA Dialog")
public class WelcomeFrameDialog extends ContainerFixture {
    public WelcomeFrameDialog(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    public ActionLink createNewProjectLink() {
        return find(ActionLink.class, byXpath("//div[@text='New Project' and @class='ActionLink']"));
    }

    public ComponentFixture importProjectLink() {
        return find(ComponentFixture.class, byXpath("//div[@text='Import Project' and @class='ActionLink']"));
    }

    public ComponentFixture ideErrorsIcon() {
        return find(ComponentFixture.class, byXpath("//div[@class='IdeErrorsIcon']"));
    }

    public ComponentFixture windowsCloseButton() {
        return find(ComponentFixture.class, byXpath("//div[@accessiblename='Close' and @class='JButton']"));
    }

    public void clearTheWorkspace() {
        step("Delete all the projects in the workspace", () -> {
            // delete all the projects' links from the 'Welcome to IntelliJ IDEA' dialog
            int numberOfLinks = getNumberOfProjectLinks();
            for (int i = 0; i < numberOfLinks; i++) {
                ComponentFixture cf = this.find(ComponentFixture.class, byXpath("//div[@accessiblename='Recent Projects' and @class='MyList']"));
                cf.runJs("const horizontal_offset = component.getWidth()-22;\n" +
                        "robot.click(component, new Point(horizontal_offset, 22), MouseButton.LEFT_BUTTON, 1);");
            }

            // delete all the files and folders in the IdeaProjects folder
            try {
                String pathToDirToMakeEmpty = System.getProperty("user.home") + File.separator + "IdeaProjects";
                boolean doesTheProjectDirExists = Files.exists(Paths.get(pathToDirToMakeEmpty));
                if (doesTheProjectDirExists) {
                    FileUtils.cleanDirectory(new File(pathToDirToMakeEmpty));
                } else {
                    Files.createDirectory(Paths.get(pathToDirToMakeEmpty));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private int getNumberOfProjectLinks() {
        try {
            ComponentFixture cf = this.find(ComponentFixture.class, byXpath("//div[@accessiblename='Recent Projects' and @class='MyList']"));
            int numberOfProjectsLinks = cf.findAllText().size() / 2;    // 2 items per 1 project link (project path and project name)
            return numberOfProjectsLinks;
        } catch (WaitForConditionTimeoutException e) {
            // the list with accessible name 'Recent Projects' is not available -> 0 links in the 'Welcome to IntelliJ IDEA' dialog
            return 0;
        }
    }

    public void checkForExceptions(RemoteRobot robot) {
        step("Check for exceptions and other errors", () -> {
            try {
                this.ideErrorsIcon().click();
            } catch (WaitForConditionTimeoutException e) {
                e.printStackTrace();
                return;
            }

            final IdeFatalErrorsDialog ideFatalErrorsDialogFixture = robot.find(IdeFatalErrorsDialog.class, Duration.ofSeconds(10));
            String exceptionNumberLabel = ideFatalErrorsDialogFixture.numberOfExcetionsJBLabel().findAllText().get(0).getText();
            int numberOfExceptions = Integer.parseInt(exceptionNumberLabel.substring(5));

            for (int i = 0; i < numberOfExceptions; i++) {
                String exceptionStackTrace = TextHelper.listOfRemoteTextToString(ideFatalErrorsDialogFixture.exceptionDescriptionJTextArea().findAllText());

                if (i + 1 < numberOfExceptions) {
                    ideFatalErrorsDialogFixture.nextExceptionButton().click();
                }
            }

            ideFatalErrorsDialogFixture.button("Clear all").click();
        });
    }
}