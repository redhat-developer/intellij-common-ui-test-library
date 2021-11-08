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
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.errors;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.redhat.devtools.intellij.commonuitest.utils.labels.ButtonLabels;
import org.jetbrains.annotations.NotNull;

/**
 * IDE Fatal Errors dialog fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@accessiblename='IDE Fatal Errors' and @class='MyDialog']")
@FixtureName(name = "IDE Fatal Errors Dialog")
public class IdeFatalErrorsDialog extends CommonContainerFixture {
    public IdeFatalErrorsDialog(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Click on the 'Clear all' button
     */
    public void clearAll() {
        button(ButtonLabels.CLEAR_ALL_LABEL).click();
    }
}