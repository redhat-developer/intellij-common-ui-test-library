/*******************************************************************************
 * Copyright (c) 2022 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.settings;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.utils.constans.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constans.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * Settings dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = XPathDefinitions.MY_DIALOG)
@FixtureName(name = "Settings Dialog")
public class SettingsDialog extends CommonContainerFixture {
    public SettingsDialog(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Navigate to specific 'Settings' dialog page according to given path
     *
     * @param path path to navigate to
     */
    public void navigateTo(String... path) {
        if (path.length == 0) {
            throw new UITestException("Path is empty.");
        }
        settingsTree().expand(path);
        settingsTree().clickPath(path, true);
    }

    /**
     * Get the 'Settings' main tree fixture
     *
     * @return Settings tree fixture
     */
    public JTreeFixture settingsTree() {
        return findAll(JTreeFixture.class, JTreeFixture.Companion.byType()).get(0);
    }

    /**
     * Finish the 'Settings' dialog by clicking on the 'OK' button
     */
    public void ok() {
        JButtonFixture button = button(ButtonLabels.OK_LABEL);
        button.click();
    }

    /**
     * Apply performed changes by clicking on the 'Apply' button
     */
    public void apply() {
        JButtonFixture button = button(ButtonLabels.APPLY_LABEL);
        button.click();
    }

    /**
     * Cancel the 'Settings' dialog by clicking on the 'Cancel' button
     */
    public void cancel() {
        JButtonFixture button = button(ButtonLabels.CANCEL_LABEL);
        button.click();
    }
}