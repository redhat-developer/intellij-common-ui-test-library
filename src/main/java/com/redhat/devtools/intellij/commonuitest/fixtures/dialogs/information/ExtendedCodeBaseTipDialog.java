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
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.information;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JCheckboxFixture;
import com.redhat.devtools.intellij.commonuitest.utils.constans.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constans.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * Tip of the Day dialog fixture extended to show different code base.
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = XPathDefinitions.TIP_DIALOG)
@FixtureName(name = "Tip Of The Day Dialog")
public class ExtendedCodeBaseTipDialog extends CommonContainerFixture {

    public ExtendedCodeBaseTipDialog(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Get the 'Don't show tips' checkbox fixture
     *
     * @return checkbox fixture
     */
    public JCheckboxFixture dontShowTipsCheckBox2() {
        return checkBox("Don't show tips", true);
    }

    /**
     * Close the 'Tip of the Day' dialog by clicking on the 'Close' button
     */
    public void close3() {
        button(ButtonLabels.CLOSE_LABEL).click();
    }
}
