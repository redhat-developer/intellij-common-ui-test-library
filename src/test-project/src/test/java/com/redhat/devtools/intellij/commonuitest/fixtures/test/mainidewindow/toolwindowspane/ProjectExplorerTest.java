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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.mainidewindow.toolwindowspane;

import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.intellij.remoterobot.utils.Keyboard;
import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ProjectExplorer;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.project.NewProjectType;
import com.redhat.devtools.intellij.commonuitest.utils.steps.SharedSteps;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project Explorer test
 *
 * @author zcervink@redhat.com
 */
class ProjectExplorerTest extends AbstractLibraryBaseTest {
    private static final String PROJECT_NAME = "pe_java_project";
    private static ProjectExplorer projectExplorer;
    private final Keyboard keyboard = new Keyboard(remoteRobot);

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, NewProjectType.PLAIN_JAVA);
        ToolWindowPane toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        toolWinPane.openProjectExplorer();
        projectExplorer = toolWinPane.find(ProjectExplorer.class, Duration.ofSeconds(10));
    }

    @AfterAll
    static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @AfterEach
    void hideAllPopups() {
        keyboard.escape();
    }

    @Test
    void isItemPresentTest() {
        boolean isItemPresent = projectExplorer.isItemPresent(PROJECT_NAME, "src", "Main");
        assertTrue(isItemPresent, "The file 'Main' should be present in the project on location 'src/Main' but is not.");
    }

    @Test
    void openFileTest() {
        if (ideaVersionInt >= 20231) {
            //ensure no editor is opened
            MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
            mainIdeWindow.invokeCmdUsingSearchEverywherePopup("Close All Tabs");
            projectExplorer.openFile(PROJECT_NAME, "src", "Main");
            assertTrue(remoteRobot.find(ComponentFixture.class, byXpath(XPathDefinitions.PROJECT_LABEL)).isShowing());
        }
    }

    @Test
    void openContextMenuOnTest() {
        JPopupMenuFixture contextMenu = projectExplorer.openContextMenuOn("Scratches and Consoles");
        assertTrue(contextMenu.hasText("New"), "The context menu on 'Scratches and Consoles' item should be opened but is not.");
    }

    @Test
    void openViewsPopupTest() {
        JPopupMenuFixture contextMenu = projectExplorer.openViewsPopup();
        assertTrue(contextMenu.hasText("Packages"), "The View popup menu should be opened but is not.");
    }

    @Test
    void selectOpenedFileTest() {
        if (ideaVersionInt >= 20231) {
            projectExplorer.expandAll();
            projectExplorer.openFile(PROJECT_NAME, "src", "Main");
            projectExplorer.projectViewTree().clickRow(0);
            SharedSteps.waitForComponentByXpath(remoteRobot, 3, 200, byXpath(XPathDefinitions.MY_ICON_LOCATE_SVG));
            projectExplorer.selectOpenedFile();
            SharedSteps.waitForComponentByXpath(remoteRobot, 3, 200, byXpath(XPathDefinitions.MY_ICON_LOCATE_SVG));
            assertTrue(projectExplorer.projectViewTree().isPathSelected(
                    projectExplorer.projectViewTree().getValueAtRow(0), "src", "Main"),
                "The file 'Main' should be selected but is not."
            );
        }
    }

    @Test
    void expandAllTest() {
        projectExplorer.collapseAll();
        int itemsInTreeBeforeExpanding = projectExplorer.projectViewTree().collectRows().size();
        projectExplorer.projectViewTree().clickRow(0); // Newer versions expands selected subtree (not all rows)
        projectExplorer.expandAll();
        int itemsInTreeAfterExpanding = projectExplorer.projectViewTree().collectRows().size();
        assertTrue(itemsInTreeAfterExpanding > itemsInTreeBeforeExpanding, "Expanding of the 'Project View' tree was not successful.");
    }

    @Test
    void collapseAllTest() {
        projectExplorer.projectViewTree().expand(PROJECT_NAME);
        int itemsInTreeBeforeCollapsing = projectExplorer.projectViewTree().collectRows().size();
        projectExplorer.collapseAll();
        int itemsInTreeAfterCollapsing = projectExplorer.projectViewTree().collectRows().size();
        assertTrue(itemsInTreeAfterCollapsing < itemsInTreeBeforeCollapsing, "Collapsing of the 'Project View' tree was not successful.");
    }

    @Test
    void openSettingsPopupTest() {
        JPopupMenuFixture contextMenu = projectExplorer.openSettingsPopup();
        assertTrue(contextMenu.hasText("Help"), "The Settings popup menu should be opened but is not.");
    }
}
