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
package com.redhat.devtools.intellij.commonuitest.fixtures.test.mainidewindow.toolwindowspane.openclose;

import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane.ToolWindowPane;
import com.redhat.devtools.intellij.commonuitest.utils.project.CreateCloseUtils;
import org.junit.jupiter.api.AfterAll;

/**
 * Abstract ToolWindowsPane test
 *
 * @author zcervink@redhat.com
 */
abstract class AbstractToolWinPaneTest extends AbstractLibraryBaseTest {
    protected static final String MAVEN_PROJECT_NAME = "tool_windows_pane_java_maven_project";
    protected static final String GRADLE_PROJECT_NAME = "tool_windows_pane_java_gradle_project";
    protected static final String PLAIN_PROJECT_NAME = "tool_windows_pane_java_plain_project";
    protected static ToolWindowPane toolWinPane;

    @AfterAll
    public static void closeCurrentProject() {
        CreateCloseUtils.closeProject(remoteRobot);
    }
}