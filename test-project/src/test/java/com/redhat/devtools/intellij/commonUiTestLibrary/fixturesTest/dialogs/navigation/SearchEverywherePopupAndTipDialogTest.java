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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixturesTest.dialogs.navigation;

import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.MainIdeWindow;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.mainIdeWindow.ideStatusBar.IdeStatusBar;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.information.TipDialog.closeTipDialogIfItAppears;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SearchEverywherePopup and TipDialog test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
public class SearchEverywherePopupAndTipDialogTest extends LibraryTestBase {
    private final String projectName = "tip_dialog_java_project";

    @BeforeEach
    public void prepareTheProject() {
        createNewProject(projectName, "Java");
    }

    @AfterEach
    public void closeTheProject() {
        MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
        mainIdeWindow.closeProject();
    }

    @Test
    public void searchEverywhereAndTipDialogTest() {
        makeSureTipDialogIsVisible();
        assertTrue(isTipDialogVisible(), "The 'Tip of the Day' dialog should be visible but is not.");
        closeTipDialogIfItAppears(remoteRobot);
        assertTrue(!isTipDialogVisible(), "The 'Tip of the Day' dialog should be closed but is not.");
        IdeStatusBar ideStatusBar = remoteRobot.find(IdeStatusBar.class, Duration.ofSeconds(10));
        ideStatusBar.waitUntilAllBgTasksFinish();
    }

    private void makeSureTipDialogIsVisible() {
        try {
            remoteRobot.find(TipDialog.class, Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            MainIdeWindow mainIdeWindow = remoteRobot.find(MainIdeWindow.class, Duration.ofSeconds(10));
            mainIdeWindow.invokeCmdUsingSearchEverywherePopup("Tip of the Day");
        }
    }

    private boolean isTipDialogVisible() {
        try {
            remoteRobot.find(TipDialog.class, Duration.ofSeconds(10));
        } catch (WaitForConditionTimeoutException e) {
            return false;
        }
        return true;
    }
}
