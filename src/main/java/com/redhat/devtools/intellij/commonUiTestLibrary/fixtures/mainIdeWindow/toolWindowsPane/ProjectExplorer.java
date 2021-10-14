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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JTreeFixture;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Project Explorer fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "ToolWindowsPane type", xpath = "//div[@accessiblename='Project Tool Window']")
@FixtureName(name = "Tool Windows Pane")
public class ProjectExplorer extends CommonContainerFixture {
    private RemoteRobot remoteRobot;

    public ProjectExplorer(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
    }

    /**
     * Test is a file with given name on given path is available in the project tree
     *
     * @param path path to navigate to
     * @return true if the given file exists on the given path in the project
     */
    public boolean isItemPresent(String... path) {
        try {
            projectViewTree().expand(path);
            projectViewTree().clickPath(path, true);
        } catch (Exception e) {
            if (!(e instanceof JTreeFixture.PathNotFoundException)) { // Kotlin PathNotFoundException could not be used in catch()
                throw e;
            }
            return false;
        }
        return true;
    }

    /**
     * Open file according to given path
     *
     * @param path path to navigate through
     */
    public void openFile(String... path) {
        projectViewTree().expand(path);
        projectViewTree().doubleClickPath(path, true);
    }

    /**
     * Open context menu on item according to given path
     *
     * @param path path to navigate through
     */
    public void openContextMenuOn(String... path) {
        projectViewTree().expand(path);
        projectViewTree().rightClickPath(path, true);
    }

    /**
     * Open the 'Views' popup menu
     */
    public void openViewsPopup() {
        actionButton(byXpath("//div[@class='ContentComboLabel']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Locate and select opened file
     */
    public void selectOpenedFile() {
        actionButton(byXpath("//div[@myicon='locate.svg']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Expand all
     */
    public void expandAll() {
        actionButton(byXpath("//div[contains(@myvisibleactions, 'View),')]//div[@myicon='expandall.svg']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Collapse all
     */
    public void collapseAll() {
        actionButton(byXpath("//div[contains(@myvisibleactions, 'View),')]//div[@myicon='collapseall.svg']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Invoke settings popup
     */
    public void openSettingsPopup() {
        actionButton(byXpath("//div[contains(@myvisibleactions, 'View),')]//div[@myicon='gearPlain.svg']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Hide Project Explorer
     */
    public void hide() {
        actionButton(byXpath("//div[contains(@myvisibleactions, 'View),')]//div[@tooltiptext='Hide']"), Duration.ofSeconds(2)).click();
    }

    /**
     * Get the Project View tree fixture
     *
     * @return Project View tree fixture
     */
    public JTreeFixture projectViewTree() {
        return find(JTreeFixture.class, JTreeFixture.Companion.byType(), Duration.ofSeconds(10));
    }
}