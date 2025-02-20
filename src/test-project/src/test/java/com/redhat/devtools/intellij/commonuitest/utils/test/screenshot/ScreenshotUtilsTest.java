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
package com.redhat.devtools.intellij.commonuitest.utils.test.screenshot;

import com.redhat.devtools.intellij.commonuitest.LibraryTestBase;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.utils.screenshot.ScreenshotUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * ScreenshotUtils test
 *
 * @author zcervink@redhat.com
 */
class ScreenshotUtilsTest extends LibraryTestBase {
    @Test
    public void takeScreenshotTest() {
        remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));

        int numberOfScreenshotBefore = getNumberOfSavedScreenshot();
        File screenshotFile = ScreenshotUtils.takeScreenshot(remoteRobot);
        int numberOfScreenshotAfter = getNumberOfSavedScreenshot();
        assertEquals(numberOfScreenshotAfter, numberOfScreenshotBefore + 1, "Screenshot should be already saved but is not.");
        try {
            Files.delete(screenshotFile.toPath());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private int getNumberOfSavedScreenshot() {
        String pathToIdeaProjectsFolder = System.getProperty("user.dir") + File.separator + "build" + File.separator + "screenshots";
        File[] files = new File(pathToIdeaProjectsFolder).listFiles((FileFilter) FileFilterUtils.fileFileFilter());
        return files != null ? files.length : 0;
    }
}