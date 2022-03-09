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
import org.jetbrains.annotations.NotNull;

/**
 * Settings dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='MyDialog']")
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
            return;
        }

        try {
            settingsTree().expand(path);
            settingsTree().clickPath(path, true);
        } catch (Exception e) {
            if (!(e instanceof JTreeFixture.PathNotFoundException)) {
                throw e;
            }
        }
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
        JButtonFixture button = button("OK");
        button.click();
    }

    /**
     * Cancel the 'Settings' dialog by clicking on the 'Cancel' button
     */
    public void cancel() {
        JButtonFixture button = button("Cancel");
        button.click();
    }
}