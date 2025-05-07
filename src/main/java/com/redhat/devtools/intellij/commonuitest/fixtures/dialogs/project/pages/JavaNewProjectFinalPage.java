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
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.ComboBoxFixture;
import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * New Project dialog java project third page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = XPathDefinitions.DIALOG_ROOT_PANE)
@FixtureName(name = "New Project Dialog")
public class JavaNewProjectFinalPage extends AbstractNewProjectFinalPage {

    private final int ideaVersionInt = UITestRunner.getIdeaVersionInt();

    public JavaNewProjectFinalPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Open the 'More settings' options
     */
    public void openMoreSettings() {
        step("Open the 'More settings' options", () -> {
            boolean isAlreadyOpened = isMoreSettingOpened();
            if (!isAlreadyOpened) {
                jLabel(ButtonLabels.MORE_SETTINGS).click();
            }
        });
    }

    /**
     * Close the 'More settings' options
     */
    public void closeMoreSettings() {
        step("Close the 'More settings' options", () -> {
            boolean isAlreadyOpened = isMoreSettingOpened();
            if (isAlreadyOpened) {
                jLabel(ButtonLabels.MORE_SETTINGS).click();
            }
        });
    }

    /**
     * Get the name of the module currently inserted in the 'Module name' input field
     *
     * @return name of the module currently inserted in the input field
     */
    public String getModuleName() {
        if (ideaVersionInt >= 20242) {
            return find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_NAME_2024_2_AND_NEWER)).getText();
        }
        else if (ideaVersionInt >= 20221) {
            return find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_NAME)).getText();
        } else {
            return textField("Module name:", true).getText();
        }
    }

    /**
     * Insert the name of the module into the 'Module name' input field
     *
     * @param moduleName name of the module that will be set into the input field
     */
    public void setModuleName(String moduleName) {
        if (ideaVersionInt >= 20242) {
            find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_NAME_2024_2_AND_NEWER)).setText(moduleName);
        }
        else if (ideaVersionInt >= 20221) {
            find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_NAME)).setText(moduleName);
        } else {
            textField("Module name:", true).setText(moduleName);
        }
    }

    /**
     * Get the location of the content root currently inserted in the 'Content root' input field
     *
     * @return location of the content root currently inserted in the input field
     */
    public String getContentRoot() {
        if (ideaVersionInt >= 20221) {
            return find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_CONTENT_ROOT)).getText();
        } else {
            return textField("Content root:", true).getText();
        }
    }

    /**
     * Insert the location of the content root into the 'Content root' input field
     *
     * @param contentRoot location of the content root that will be set into the input field
     */
    public void setContentRoot(String contentRoot) {
        if (ideaVersionInt >= 20221) {
            find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_CONTENT_ROOT)).setText(contentRoot);
        } else {
            textField("Content root:", true).setText(contentRoot);
        }
    }

    /**
     * Get the location of the module file currently inserted in the 'Module file location' input field
     *
     * @return location of the module file currently inserted in the input field
     */
    public String getModuleFileLocation() {
        if (ideaVersionInt >= 20221) {
            return find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_FILE_LOCATION)).getText();
        } else {
            return textField("Module file location:", true).getText();
        }
    }

    /**
     * Insert the location of the module file into the 'Module file location' input field
     *
     * @param moduleFileLocation location of the module file that will be set into the input field
     */
    public void setModuleFileLocation(String moduleFileLocation) {
        if (ideaVersionInt >= 20221) {
            find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_FILE_LOCATION)).setText(moduleFileLocation);
        } else {
            textField("Module file location:", true).setText(moduleFileLocation);
        }
    }

    /**
     * Get the project format currently set in the 'Project format' combo box
     *
     * @return project format currently set in the combo box
     * @throws UITestException when there is set another value than defined by the 'ProjectFormatType' enumeration in the combo box
     */
    public ProjectFormatType getProjectFormat() {
        ComboBoxFixture projectFormatComboBox = comboBox(byXpath(XPathDefinitions.JCOMBOBOX), Duration.ofSeconds(10));

        if (projectFormatComboBox.selectedText().contains(ProjectFormatType.IDEA_DIRECTORY_BASED.toString())) {
            return ProjectFormatType.IDEA_DIRECTORY_BASED;
        } else if (projectFormatComboBox.selectedText().contains(ProjectFormatType.IPR_FILE_BASED.toString())) {
            return ProjectFormatType.IPR_FILE_BASED;
        } else {
            throw new UITestException("Currently selected project format is not supported.");
        }
    }

    /**
     * Set the project format into the 'Project format' combo box
     *
     * @param projectFormatType project format that will be set into the combo box
     */
    public void setProjectFormat(ProjectFormatType projectFormatType) {
        ComboBoxFixture projectFormatComboBox = comboBox(byXpath(XPathDefinitions.JCOMBOBOX), Duration.ofSeconds(10));
        projectFormatComboBox.selectItemContains(projectFormatType.toString());
    }

    private boolean isMoreSettingOpened() {
        return findAll(ContainerFixture.class, byXpath(XPathDefinitions.MORE_SETTINGS_TITLED_SEPARATOR)).size() == 2;
    }
}