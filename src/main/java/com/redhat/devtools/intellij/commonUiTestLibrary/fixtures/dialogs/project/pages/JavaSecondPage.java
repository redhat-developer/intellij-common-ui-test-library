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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JCheckboxFixture;
import org.jetbrains.annotations.NotNull;

/**
 * New Project dialog java project second page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='DialogRootPane']")
@FixtureName(name = "New Project Dialog")
public class JavaSecondPage extends CommonContainerFixture {
    public JavaSecondPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Toggle the 'Create project from template' checkbox
     *
     * @param toggle value to set to the checkbox
     */
    public void toggleFromTemplate(boolean toggle) {
        fromTemplateCheckBox().setValue(toggle);
    }

    /**
     * Get the 'Create project from template' checkbox fixture
     *
     * @return checkbox fixture
     */
    public JCheckboxFixture fromTemplateCheckBox() {
        return checkBox("Create project from template", true);
    }
}