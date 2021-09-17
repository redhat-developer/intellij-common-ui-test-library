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
package com.redhat.devtools.intellij.commonUiTestLibrary.utilsTest.toolWindowsPaneTest;

import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.toolWindowsPane.ToolWindowsPane;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProjectViewUtils test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
public class ProjectViewUtilsTest extends LibraryTestBase {
    private final String projectName = "project_view_utils_java_project";

    @BeforeEach
    public void prepareProject() {
        createNewProject(projectName, "Java");
    }

    @AfterEach
    public void closeCurrentProject() {
        super.closeProject();
    }

    @Test
    public void projectViewUtilsTest() {
        ToolWindowsPane toolWindowsPane = remoteRobot.find(ToolWindowsPane.class, Duration.ofSeconds(10));
        boolean isImlFilePresent = toolWindowsPane.isAProjectFilePresent(projectName, projectName + ".iml");
        assertTrue(isImlFilePresent, "File '" + projectName + ".iml" + "' should be present in the project view structure.");
    }
}