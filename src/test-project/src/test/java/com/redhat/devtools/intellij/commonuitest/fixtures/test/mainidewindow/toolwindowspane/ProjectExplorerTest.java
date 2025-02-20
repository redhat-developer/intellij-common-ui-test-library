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
import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.intellij.remoterobot.utils.Keyboard;
import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.AbstractToolWinPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ProjectExplorer;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import com.redhat.devtools.intellij.commonuitest.utils.steps.SharedSteps;
import com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils;
import org.intellij.lang.annotations.Language;
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
    private static final String PROJECT_NAME = "pe_java_project";
    private static ProjectExplorer projectExplorer;
    private final Keyboard keyboard = new Keyboard(remoteRobot);

    @BeforeAll
    static void prepareProject() {
        CreateCloseUtils.createNewProject(remoteRobot, PROJECT_NAME, CreateCloseUtils.NewProjectType.PLAIN_JAVA);
        AbstractToolWinPane toolWinPane;
        if (UITestRunner.getIdeaVersionInt() >= 20221) {
            toolWinPane = remoteRobot.find(ToolWindowPane.class, Duration.ofSeconds(10));
        } else {
            toolWinPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        }
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
        projectExplorer.openFile(PROJECT_NAME, ".gitignore");
        if (ideaVersionInt >= 20231) {       // Code for IJ 2023.1+
            @Language("XPath") String projectLabelXpath = "//div[@accessiblename='.gitignore' and @class='EditorTabLabel']//div[@class='ActionPanel']";
            try {       // Verify file is opened by finding its tab in the editor
                remoteRobot.find(ComponentFixture.class, byXpath(projectLabelXpath));
            } catch (Exception e) {
                fail("The '.gitignore' file should be opened but is not.");
            }
        } else {
            List<ContainerFixture> cfs = remoteRobot.findAll(ContainerFixture.class, byXpath(XPathDefinitions.SINGLE_HEIGHT_LABEL));
            ContainerFixture cf = cfs.get(cfs.size() - 1);
            String allText = TextUtils.listOfRemoteTextToString(cf.findAllText());
            boolean isFileOpened = allText.contains(".gitignore");
            assertTrue(isFileOpened, "The '.gitignore' file should be opened but is not.");
        }
    }

    @Test
    void openContextMenuOnTest() {
        try {
            JPopupMenuFixture contextMenu = projectExplorer.openContextMenuOn("Scratches and Consoles");
            assertTrue(contextMenu.hasText("New"), "The context menu on 'Scratches and Consoles' item should be opened but is not.");
        } catch (UITestException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void openViewsPopupTest() {
        try {
            JPopupMenuFixture contextMenu = projectExplorer.openViewsPopup();
            assertTrue(contextMenu.hasText("Packages"), "The View popup menu should be opened but is not.");
        } catch (UITestException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void selectOpenedFileTest() {
        projectExplorer.expandAll();
        projectExplorer.openFile(PROJECT_NAME, "src", "Main");
        projectExplorer.projectViewTree().clickRow(0);
        SharedSteps.waitForComponentByXpath(remoteRobot, 3, 1, byXpath(XPathDefinitions.MY_ICON_LOCATE_SVG));
        projectExplorer.selectOpenedFile();
        SharedSteps.waitForComponentByXpath(remoteRobot, 3, 1, byXpath(XPathDefinitions.MY_ICON_LOCATE_SVG));
        assertTrue(projectExplorer.projectViewTree().isPathSelected(
                        projectExplorer.projectViewTree().getValueAtRow(0), "src", "Main"),
                "The file 'Main' should be selected but is not."
        );
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
        try {
            JPopupMenuFixture contextMenu = projectExplorer.openSettingsPopup();
            assertTrue(contextMenu.hasText("Help"), "The Settings popup menu should be opened but is not.");
        } catch (UITestException e) {
            fail(e.getMessage());
        }
    }
}
