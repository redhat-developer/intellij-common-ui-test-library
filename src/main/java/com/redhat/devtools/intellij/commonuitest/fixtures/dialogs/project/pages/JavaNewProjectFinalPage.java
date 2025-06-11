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
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

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
     * Get the name of the module currently inserted in the 'Module name' input field
     *
     * @return name of the module currently inserted in the input field
     */
    public String getModuleName() {
        if (ideaVersionInt >= 20242) {
            return find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_NAME_2024_2_AND_NEWER)).getText();
        } else {
            return find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_NAME)).getText();
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
        } else {
            find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_NAME)).setText(moduleName);
        }
    }

    /**
     * Get the location of the content root currently inserted in the 'Content root' input field
     *
     * @return location of the content root currently inserted in the input field
     */
    public String getContentRoot() {
        return find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_CONTENT_ROOT)).getText();
    }

    /**
     * Insert the location of the content root into the 'Content root' input field
     *
     * @param contentRoot location of the content root that will be set into the input field
     */
    public void setContentRoot(String contentRoot) {
        find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_CONTENT_ROOT)).setText(contentRoot);
    }

    /**
     * Get the location of the module file currently inserted in the 'Module file location' input field
     *
     * @return location of the module file currently inserted in the input field
     */
    public String getModuleFileLocation() {
        return find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_FILE_LOCATION)).getText();
    }

    /**
     * Insert the location of the module file into the 'Module file location' input field
     *
     * @param moduleFileLocation location of the module file that will be set into the input field
     */
    public void setModuleFileLocation(String moduleFileLocation) {
        find(JTextFieldFixture.class, byXpath(XPathDefinitions.GET_SET_MODULE_FILE_LOCATION)).setText(moduleFileLocation);
    }

}