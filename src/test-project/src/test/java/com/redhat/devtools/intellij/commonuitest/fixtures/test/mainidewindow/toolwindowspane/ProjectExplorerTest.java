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

import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.intellij.remoterobot.utils.Keyboard;
import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ProjectExplorer;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Project Explorer test
 *
 * @author zcervink@redhat.com
 */
class ProjectExplorerTest extends LibraryTestBase {
    private static final String PROJECT_NAME = "project_explorer_java_project";
    private static ProjectExplorer projectExplorer;
    private final Keyboard keyboard = new Keyboard(remoteRobot);

    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        toolWindowsPane.openProjectExplorer();
        projectExplorer = toolWindowsPane.find(ProjectExplorer.class, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @AfterEach
    public void hideAllPopups() {
        keyboard.escape();
    }

    @Test
    public void isItemPresentTest() {
        boolean isItemPresent = projectExplorer.isItemPresent(PROJECT_NAME, PROJECT_NAME + ".iml");
        assertTrue(isItemPresent, "The file '" + PROJECT_NAME + ".iml' should be present in the project on location '" + PROJECT_NAME + "/" + PROJECT_NAME + ".iml' but is not.");
    }

    @Test
    public void openFileTest() {
        projectExplorer.openFile(PROJECT_NAME, PROJECT_NAME + ".iml");
        List<ContainerFixture> cfs = remoteRobot.findAll(ContainerFixture.class, byXpath("//div[@class='SingleHeightLabel']"));
        ContainerFixture cf = cfs.get(cfs.size() - 1);
        String allText = TextUtils.listOfRemoteTextToString(cf.findAllText());
        boolean isFileOpened = allText.contains(PROJECT_NAME + ".iml");
        assertTrue(isFileOpened, "The '" + PROJECT_NAME + ".iml' file should be opened but is not.");
    }

    @Test
    public void openContextMenuOnTest() {
        try {
            JPopupMenuFixture contextMenu = projectExplorer.openContextMenuOn("Scratches and Consoles");
            assertTrue(contextMenu.hasText("New"), "The context menu on 'Scratches and Consoles' item should be opened but is not.");
        } catch (UITestException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void openViewsPopupTest() {
        try {
            JPopupMenuFixture contextMenu = projectExplorer.openViewsPopup();
            assertTrue(contextMenu.hasText("Packages"), "The View popup menu should be opened but is not.");
        } catch (UITestException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void selectOpenedFileTest() {
        projectExplorer.openFile(PROJECT_NAME, ".idea", "modules.xml");
        projectExplorer.projectViewTree().clickPath(new String[]{PROJECT_NAME}, true);
        projectExplorer.selectOpenedFile();
        assertTrue(projectExplorer.projectViewTree().isPathSelected(PROJECT_NAME, ".idea", "modules.xml"), "The file 'modules.xml' should be selected but is not.");
    }

    @Test
    public void expandAllTest() {
        projectExplorer.collapseAll();
        int itemsInTreeBeforeExpanding = projectExplorer.projectViewTree().collectRows().size();
        projectExplorer.expandAll();
        int itemsInTreeAfterExpanding = projectExplorer.projectViewTree().collectRows().size();
        assertTrue(itemsInTreeAfterExpanding > itemsInTreeBeforeExpanding, "Expanding of the 'Project View' tree was not successful.");
    }

    @Test
    public void collapseAllTest() {
        projectExplorer.projectViewTree().expand(PROJECT_NAME);
        int itemsInTreeBeforeCollapsing = projectExplorer.projectViewTree().collectRows().size();
        projectExplorer.collapseAll();
        int itemsInTreeAfterCollapsing = projectExplorer.projectViewTree().collectRows().size();
        assertTrue(itemsInTreeAfterCollapsing < itemsInTreeBeforeCollapsing, "Collapsing of the 'Project View' tree was not successful.");
    }

    @Test
    public void openSettingsPopupTest() {
        try {
            JPopupMenuFixture contextMenu = projectExplorer.openSettingsPopup();
            assertTrue(contextMenu.hasText("Help"), "The Settings popup menu should be opened but is not.");
        } catch (UITestException e) {
            fail(e.getMessage());
        }
    }
}