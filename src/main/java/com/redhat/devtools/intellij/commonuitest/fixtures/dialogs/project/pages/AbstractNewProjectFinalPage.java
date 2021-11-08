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
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import org.jetbrains.annotations.NotNull;

/**
 * New Project dialog abstract terminal page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='DialogRootPane']")
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
        return textFields(JTextFieldFixture.Companion.byType()).get(0).getText();
    }

    /**
     * Set the project name for new project in the 'New Project' dialog
     *
     * @param projectName name of the new project
     */
    public void setProjectName(String projectName) {
        textFields(JTextFieldFixture.Companion.byType()).get(0).setText(projectName);
    }

    /**
     * Get the project location for new project in the 'New Project' dialog
     *
     * @return currently set new project location
     */
    public String getProjectLocation() {
        return textFields(JTextFieldFixture.Companion.byType()).get(1).getText();
    }

    /**
     * Set the project location for new project in the 'New Project' dialog
     *
     * @param projectLocation project location of the new project
     */
    public void setProjectLocation(String projectLocation) {
        textFields(JTextFieldFixture.Companion.byType()).get(1).setText(projectLocation);
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