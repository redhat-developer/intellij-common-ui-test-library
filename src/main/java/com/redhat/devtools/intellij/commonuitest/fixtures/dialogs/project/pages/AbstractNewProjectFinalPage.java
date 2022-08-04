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
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils.listOfRemoteTextToString;

/**
 * New Project dialog abstract terminal page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = XPathDefinitions.DIALOG_ROOT_PANE)
@FixtureName(name = "New Project Dialog")
public abstract class AbstractNewProjectFinalPage extends CommonContainerFixture {
    protected AbstractNewProjectFinalPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Get the project name for new project in the 'New Project' dialog
     *
     * @return currently set new project name
     */
    public String getProjectName() {
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            return textFields(byXpath(XPathDefinitions.JBTEXT_FIELD)).get(0).getText();
        } else {
            return textFields(JTextFieldFixture.Companion.byType()).get(0).getText();
        }
    }

    /**
     * Set the project name for new project in the 'New Project' dialog
     *
     * @param projectName name of the new project
     */
    public void setProjectName(String projectName) {
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            textFields(byXpath(XPathDefinitions.JBTEXT_FIELD)).get(0).setText(projectName);
        } else {
            textFields(JTextFieldFixture.Companion.byType()).get(0).setText(projectName);
        }
    }

    /**
     * Get the project location for new project in the 'New Project' dialog
     *
     * @return currently set new project location
     */
    public String getProjectLocation() {
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            return find(JTextFieldFixture.class, byXpath(XPathDefinitions.EXTENDABLE_TEXT_FIELD)).getText();
        } else {
            return textFields(JTextFieldFixture.Companion.byType()).get(1).getText();
        }
    }

    /**
     * Set the project location for new project in the 'New Project' dialog
     *
     * @param projectLocation project location of the new project
     */
    public void setProjectLocation(String projectLocation) {
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            find(JTextFieldFixture.class, byXpath(XPathDefinitions.EXTENDABLE_TEXT_FIELD )).setText(projectLocation);
        } else {
            textFields(JTextFieldFixture.Companion.byType()).get(1).setText(projectLocation);
        }
    }

    /**
     * Open the 'Advanced Settings' section
     */
    public void openAdvanceSettings() {
        if (!isAdvancedSettingsOpened()) {
            if (UITestRunner.getIdeaVersionInt() >= 20222) {
                find(ComponentFixture.class, byXpath(XPathDefinitions.COLLAPSIBLE_TITLED_SEPARATOR_NEW)).click();
            } else {
                find(ComponentFixture.class, byXpath(XPathDefinitions.COLLAPSIBLE_TITLED_SEPARATOR)).click();
            }
        }
    }

    /**
     * Close the 'Advanced Settings' section
     */
    public void closeAdvanceSettings() {
        if (isAdvancedSettingsOpened()) {
            if (UITestRunner.getIdeaVersionInt() >= 20222) {
                find(ComponentFixture.class, byXpath(XPathDefinitions.COLLAPSIBLE_TITLED_SEPARATOR_NEW)).click();
            } else {
                find(ComponentFixture.class, byXpath(XPathDefinitions.COLLAPSIBLE_TITLED_SEPARATOR)).click();
            }
        }
    }

    private boolean isAdvancedSettingsOpened() {
        List<ComponentFixture> cf;

        if (UITestRunner.getIdeaVersionInt() >= 20222) {
            cf = findAll(ComponentFixture.class, byXpath(XPathDefinitions.COLLAPSIBLE_TITLED_SEPARATOR_NEW_SIBLINGS));
        } else {
            cf = findAll(ComponentFixture.class, byXpath(XPathDefinitions.COLLAPSIBLE_TITLED_SEPARATOR_SIBLINGS));
        }

        for (int i = 0; i < cf.size(); i++) {
            if (listOfRemoteTextToString(cf.get(i).findAllText()).contains("Advanced Settings")) {
                return i != cf.size() - 1;
            }
        }
        throw new UITestException("Wizard does not contain 'Advanced Settings' section.");
    }

    /**
     * Enumeration defining values of the 'Project format' combo box
     */
    public enum ProjectFormatType {
        IDEA_DIRECTORY_BASED(".idea"),
        IPR_FILE_BASED(".ipr");

        private final String textRepresentation;

        ProjectFormatType(String textRepresentation) {
            this.textRepresentation = textRepresentation;
        }

        @Override
        public String toString() {
            return this.textRepresentation;
        }
    }
}