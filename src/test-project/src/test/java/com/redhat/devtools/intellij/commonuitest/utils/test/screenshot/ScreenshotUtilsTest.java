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

import com.redhat.devtools.intellij.commonuitest.AbstractLibraryBaseTest;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.utils.screenshot.ScreenshotUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ScreenshotUtils test
 *
 * @author zcervink@redhat.com
 */
class ScreenshotUtilsTest extends AbstractLibraryBaseTest {

    private final String pathToIdeaProjectsFolder = System.getProperty("user.dir") + File.separator + "build" + File.separator + "screenshots";

    @Test
    void takeScreenshotTest() {
        String comment = "to_be_removed";
        remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10));

        int numberOfScreenshotBefore = getNumberOfSavedScreenshot();
        ScreenshotUtils.takeScreenshot(remoteRobot, comment);
        int numberOfScreenshotAfter = getNumberOfSavedScreenshot();
        assertEquals(numberOfScreenshotAfter, numberOfScreenshotBefore + 1, "Screenshot should be already saved but is not.");
        Arrays.stream(Objects.requireNonNull(new File(pathToIdeaProjectsFolder).listFiles())).filter(file -> file.getName().contains(comment)).forEach(File::delete);
    }

    private int getNumberOfSavedScreenshot() {
        File[] files = new File(pathToIdeaProjectsFolder).listFiles();
        return files != null ? files.length : 0;
    }

}