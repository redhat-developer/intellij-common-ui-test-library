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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.mainIdeWindow.toolWindowsPane;

import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.exceptions.UITestException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ProjectExplorer;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Project Explorer test
 *
 * @author zcervink@redhat.com
 */
class ProjectExplorerTest extends LibraryTestBase {
    private static final String projectName = "project_explorer_java_project";
    private static ToolWindowsPane toolWindowsPane;
    private static ProjectExplorer projectExplorer;

    @BeforeAll
    public static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, projectName, CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        toolWindowsPane.openProjectExplorer();
        projectExplorer = toolWindowsPane.find(ProjectExplorer.class, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }

    @Test
    public void isItemPresentTest() {
        step("@Test - is an item in the Project Explorer tree accessible", () -> {
            boolean isItemPresent = projectExplorer.isItemPresent(projectName, projectName + ".iml");
            assertTrue(isItemPresent, "The file '" + projectName + ".iml' should be present in the project on location '" + projectName + "/" + projectName + ".iml' but is not.");
        });
    }

    @Test
    public void openFileTest() {
        step("@Test - open a file using the Project Explorer", () -> {
            projectExplorer.openFile(projectName, projectName + ".iml");
            ContainerFixture cf = remoteRobot.find(ContainerFixture.class, byXpath("//div[@class='SingleHeightLabel']"), Duration.ofSeconds(10));
            String allText = TextUtils.listOfRemoteTextToString(cf.findAllText());
            boolean isFileOpened = allText.contains(projectName + ".iml");
            assertTrue(isFileOpened, "The '" + projectName + ".iml' file should be opened but is not.");
        });
    }

    @Test
    public void openContextMenuOnTest() {
        step("@Test - open context menu on a file in Project Explorer", () -> {
            try {
                JPopupMenuFixture contextMenu = projectExplorer.openContextMenuOn(projectName, projectName + ".iml");
                assertTrue(contextMenu.hasText("New"), "The context menu on file '" + projectName + ".iml' should be opened but is not.");
            } catch (UITestException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    public void openViewsPopupTest() {
        step("@Test - open the 'Views' popup", () -> {
            try {
                JPopupMenuFixture contextMenu = projectExplorer.openViewsPopup();
                assertTrue(contextMenu.hasText("Packages"), "The View popup menu should be opened but is not.");
            } catch (UITestException e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    public void selectOpenedFileTest() {
        step("@Test - test the 'Select Opened File' button", () -> {
            projectExplorer.openFile(projectName, projectName + ".iml");
            projectExplorer.projectViewTree().clickPath(new String[]{projectName}, true);
            projectExplorer.selectOpenedFile();
            assertTrue(projectExplorer.projectViewTree().isPathSelected(projectName, projectName + ".iml"), "The file '" + projectName + ".iml' should be selected but is not.");
        });
    }

    @Test
    public void expandAllTest() {
        step("@Test - test the 'Expand All' button", () -> {
            projectExplorer.collapseAll();
            int itemsInTreeBeforeExpanding = projectExplorer.projectViewTree().collectRows().size();
            projectExplorer.expandAll();
            int itemsInTreeAfterExpanding = projectExplorer.projectViewTree().collectRows().size();
            assertTrue(itemsInTreeAfterExpanding > itemsInTreeBeforeExpanding, "Expanding of the 'Project View' tree was not successful.");
        });
    }

    @Test
    public void collapseAllTest() {
        step("@Test - test the 'Collapse All' button", () -> {
            projectExplorer.projectViewTree().expand(new String[]{projectName});
            int itemsInTreeBeforeCollapsing = projectExplorer.projectViewTree().collectRows().size();
            projectExplorer.collapseAll();
            int itemsInTreeAfterCollapsing = projectExplorer.projectViewTree().collectRows().size();
            assertTrue(itemsInTreeAfterCollapsing < itemsInTreeBeforeCollapsing, "Collapsing of the 'Project View' tree was not successful.");
        });
    }

    @Test
    public void openSettingsPopupTest() {
        step("@Test - open the settings popup", () -> {
            try {
                JPopupMenuFixture contextMenu = projectExplorer.openSettingsPopup();
                assertTrue(contextMenu.hasText("Flatten Packages"), "The Settings popup menu should be opened but is not.");
            } catch (UITestException e) {
                fail(e.getMessage());
            }
        });
    }
}