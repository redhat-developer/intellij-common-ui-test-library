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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.ComboBoxFixture;
import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.redhat.devtools.intellij.commonUiTestLibrary.exceptions.IntelliJCommonUiTestLibException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.abstractPages.AbstractTerminalPage;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * New Project dialog java project third page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='DialogRootPane']")
@FixtureName(name = "New Project Dialog")
public class JavaProjectThirdPage extends AbstractTerminalPage {
    private RemoteRobot remoteRobot;

    public JavaProjectThirdPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Open the 'More settings' options
     */
    public void openMoreSettings() {
        boolean isAlreadyOpened = findAll(ContainerFixture.class, byXpath("//div[@class='TitledSeparator']/../../*")).size() == 2;
        if (!isAlreadyOpened) {
            jLabel(ButtonLabels.moreSettings).click();
        }
    }

    /**
     * Get the name of the module currently inserted in the 'Module name' input field
     *
     * @return name of the module currently inserted in the input field
     */
    public String getModuleName() {
        return textField("Module name:", true).getText();
    }

    /**
     * Insert the name of the module into the 'Module name' input field
     *
     * @param moduleName name of the module that will be set into the input field
     */
    public void setModuleName(String moduleName) {
        textField("Module name:", true).setText(moduleName);
    }

    /**
     * Get the location of the content root currently inserted in the 'Content root' input field
     *
     * @return location of the content root currently inserted in the input field
     */
    public String getContentRoot() {
        return textField("Content root:", true).getText();
    }

    /**
     * Insert the location of the content root into the 'Content root' input field
     *
     * @param contentRoot location of the content root that will be set into the input field
     */
    public void setContentRoot(String contentRoot) {
        textField("Content root:", true).setText(contentRoot);
    }

    /**
     * Get the location of the module file currently inserted in the 'Module file location' input field
     *
     * @return location of the module file currently inserted in the input field
     */
    public String getModuleFileLocation() {
        return textField("Module file location:", true).getText();
    }

    /**
     * Insert the location of the module file into the 'Module file location' input field
     *
     * @param moduleFileLocation location of the module file that will be set into the input field
     */
    public void setModuleFileLocation(String moduleFileLocation) {
        textField("Module file location:", true).setText(moduleFileLocation);
    }

    /**
     * Get the project format currently set in the 'Project format' combo box
     *
     * @return project format currently set in the combo box
     * @throws IntelliJCommonUiTestLibException when there is set another value than defined by the 'ProjectFormatType' enumeration in the combo box
     */
    public ProjectFormatType getProjectFormat() {
        ComboBoxFixture projectFormatComboBox = comboBox(byXpath("//div[@class='JComboBox']"), Duration.ofSeconds(10));

        if (projectFormatComboBox.selectedText().contains(ProjectFormatType.IDEA_DIRECTORY_BASED.toString())) {
            return ProjectFormatType.IDEA_DIRECTORY_BASED;
        } else if (projectFormatComboBox.selectedText().contains(ProjectFormatType.IPR_FILE_BASED.toString())) {
            return ProjectFormatType.IPR_FILE_BASED;
        } else {
            throw new IntelliJCommonUiTestLibException("Currently selected project format is not supported.");
        }
    }

    /**
     * Set the project format into the 'Project format' combo box
     *
     * @param projectFormatType project format that will be set into the combo box
     */
    public void setProjectFormat(ProjectFormatType projectFormatType) {
        ComboBoxFixture projectFormatComboBox = comboBox(byXpath("//div[@class='JComboBox']"), Duration.ofSeconds(10));
        projectFormatComboBox.selectItemContains(projectFormatType.toString());
    }

    /**
     * Enumeration defining values of the 'Project format' combo box
     */
    public enum ProjectFormatType {
        IDEA_DIRECTORY_BASED(".idea"),
        IPR_FILE_BASED(".ipr");

        private String textRepresentation;

        ProjectFormatType(String textRepresentation) {
            this.textRepresentation = textRepresentation;
        }

        @Override
        public String toString() {
            return this.textRepresentation;
        }
    }
}