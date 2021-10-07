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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.projectManipulation.pages.abstractPages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import org.jetbrains.annotations.NotNull;

import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.finishLabel;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.previousLabel;

/**
 * New Project dialog abstract terminal page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='DialogRootPane']")
@FixtureName(name = "New Project Dialog")
public class AbstractTerminalPage extends AbstractPage {
    public AbstractTerminalPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
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
     * Move to the previous page of the 'New Project' dialog by clicking on the 'Previous' button
     */
    public void previous() {
        button(previousLabel).click();
    }

    /**
     * Finish the 'New Project' dialog by clicking on the 'Finish' button
     */
    public void finish() {
        button(finishLabel).click();
    }
}