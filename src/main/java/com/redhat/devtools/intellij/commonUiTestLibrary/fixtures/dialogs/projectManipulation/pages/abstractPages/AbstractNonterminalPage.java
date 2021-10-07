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
import org.jetbrains.annotations.NotNull;

import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.nextLabel;
import static com.redhat.devtools.intellij.commonUiTestLibrary.utils.labels.ButtonLabels.previousLabel;

/**
 * New Project dialog abstract non-terminal page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='DialogRootPane']")
@FixtureName(name = "New Project Dialog")
public class AbstractNonterminalPage extends AbstractPage {
    public AbstractNonterminalPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Move to the next page of the 'New Project' dialog by clicking on the 'Next' button
     */
    public void next() {
        button(nextLabel).click();
    }

    /**
     * Move to the previous page of the 'New Project' dialog by clicking on the 'Previous' button
     */
    public void previous() {
        button(previousLabel).click();
    }
}