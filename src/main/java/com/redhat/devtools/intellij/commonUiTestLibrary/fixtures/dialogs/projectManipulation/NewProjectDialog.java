/*******************************************************************************
 * Copyright (c) 2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.nextLabel;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.finishLabel;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.cancelLabel;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.previousLabel;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils.listOfRemoteTextToString;

/**
 * New Project dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@accessiblename='New Project' and @class='MyDialog']")
@FixtureName(name = "New Project Dialog")
public class NewProjectDialog extends CommonContainerFixture {
    private int projectSdkItemsCount = -1;

    public NewProjectDialog(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Set the project name for new maven of gradle project in the 'New Project' dialog
     *
     * @param projectName name of the new project
     */
    public void setProjectNameForMavenOrGradleProject(String projectName) {
        find(JTextFieldFixture.class, byXpath("//div[@class='JBTextField']"), Duration.ofSeconds(10)).setText(projectName);
    }

    /**
     * Set the project name for new java project in the 'New Project' dialog
     *
     * @param projectName name of the new project
     */
    public void setProjectNameForJavaProject(String projectName) {
        findAll(JTextFieldFixture.class, byXpath("//div[@accessiblename='Project name:' and @class='JTextField']")).get(0).setText(projectName);
    }

    /**
     * Set the project type to specific type
     *
     * @param projectType name of the project type to which will be changed the current settings
     */
    public void selectNewProjectType(String projectType) {
        ComponentFixture newProjectTypeList = findAll(ComponentFixture.class, byXpath("JBList", "//div[@class='JBList']")).get(0);
        newProjectTypeList.findText(projectType).click();
    }

    /**
     * Set the project SDK to specific option
     *
     * @param targetSdkName name of the SDK to which will be changed the current settings
     */
    public void setProjectSdkIfAvailable(String targetSdkName) {
        ComponentFixture projectJdkComboBox = findAll(ComponentFixture.class, byXpath("//div[@accessiblename='Project SDK:' and @class='JPanel']")).get(0);
        String currentlySelectedProjectSdk = listOfRemoteTextToString(projectJdkComboBox.findAllText());
        if (currentlySelectedProjectSdk.contains(targetSdkName)) {
            return;
        }
        projectJdkComboBox.click();

        waitFor(Duration.ofSeconds(20), Duration.ofSeconds(2), "The project JDK list did not load all items in 20 seconds.", () -> didProjectSdkListLoadAllItems());
        ComponentFixture projectSdkList = find(ComponentFixture.class, byXpath("//div[@class='HeavyWeightWindow']"), Duration.ofSeconds(10));
        List<RemoteText> sdkItems = projectSdkList.findAllText();
        for (RemoteText sdkItem : sdkItems) {
            if (sdkItem.getText().contains(targetSdkName)) {
                try {
                    sdkItem.click();
                } catch (Exception e) {
                    // issue #83 in the Remote-Robot framework - https://github.com/JetBrains/intellij-ui-test-robot/issues/83
                    break;
                }
            }
        }
    }

    /**
     * Move to the next page of the 'New Project' dialog by clicking on the 'Next' button
     */
    public void next() {
        button(nextLabel).click();
    }

    /**
     * Finish the 'New Project' dialog by clicking on the 'Finish' button
     */
    public void finish() {
        button(finishLabel).click();
    }

    /**
     * Close the 'New Project' dialog by clicking on the 'Cancel' button
     */
    public void cancel() {
        button(cancelLabel).click();
    }

    /**
     * Move to the previous page of the 'New Project' dialog by clicking on the 'Previous' button
     */
    public void previous() {
        button(previousLabel).click();
    }

    private boolean didProjectSdkListLoadAllItems() {
        ComponentFixture projectSdkList = find(ComponentFixture.class, byXpath("//div[@class='HeavyWeightWindow']"), Duration.ofSeconds(10));
        List<RemoteText> sdkItems = projectSdkList.findAllText();
        int currentSdkItemsCount = sdkItems.size();

        if (projectSdkItemsCount == -1) {
            projectSdkItemsCount = currentSdkItemsCount;
            return false;
        } else if (projectSdkItemsCount != currentSdkItemsCount) {
            projectSdkItemsCount = currentSdkItemsCount;
            return false;
        }
        return true;
    }
}