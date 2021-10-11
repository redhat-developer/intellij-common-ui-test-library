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
package com.redhat.devtools.intellij.commonUiTestLibrary.utilsTest.screenshot;

import com.redhat.devtools.intellij.commonUiTestLibrary.LibraryTestBase;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.screenshot.ScreenshotUtils;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.testExtension.ScreenshotAfterTestFailExtension;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.FileFilter;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ScreenshotUtils test
 *
 * @author zcervink@redhat.com
 */
@ExtendWith(ScreenshotAfterTestFailExtension.class)
class ScreenshotUtilsTest extends LibraryTestBase {
    @Test
    public void takeScreenshotTest() {
        remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));

        int numberOfScreenshotBefore = getNumberOfSavedScreenshot();
        File screenshotFile = ScreenshotUtils.takeScreenshot(remoteRobot);
        int numberOfScreenshotAfter = getNumberOfSavedScreenshot();
        assertTrue(numberOfScreenshotAfter == numberOfScreenshotBefore + 1, "Screenshot should be already saved but is not.");
        if (screenshotFile != null) {
            screenshotFile.delete();
        }
    }

    private int getNumberOfSavedScreenshot() {
        String pathToIdeaProjectsFolder = System.getProperty("user.dir") + File.separator + "build" + File.separator + "screenshots";
        File[] files = new File(pathToIdeaProjectsFolder).listFiles((FileFilter) FileFilterUtils.fileFileFilter());
        return files != null ? files.length : 0;
    }
}