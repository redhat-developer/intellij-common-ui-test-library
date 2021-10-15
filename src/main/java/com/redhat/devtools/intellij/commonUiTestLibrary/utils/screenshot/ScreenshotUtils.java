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
package com.redhat.devtools.intellij.commonUiTestLibrary.utils.screenshot;

import com.intellij.remoterobot.RemoteRobot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * Static utilities for taking screenshots
 *
 * @author zcervink@redhat.com
 */
public class ScreenshotUtils {
    final static String screenshotLocation = "." + File.separator + "build" + File.separator + "screenshots" + File.separator;
    final static String screenshotFilename = getTimeNow("yyyy_MM_dd_HH_mm_ss");
    final static String filetype = "png";
    final static String screenshotPathname = screenshotLocation + screenshotFilename + "." + filetype;

    /**
     * Take screenshot of the entire screen and save it on disk
     *
     * @param remoteRobot reference to the RemoteRobot instance
     * @return the screenshot as a File object
     */
    public static File takeScreenshot(RemoteRobot remoteRobot) {
        return step("Take screenshot of the entire screen and save it on disk", () -> {
            try {
                BufferedImage screenshotBufferedImage = remoteRobot.getScreenshot();
                boolean doesScreenshotDirExists = Files.exists(Paths.get(screenshotLocation));
                if (!doesScreenshotDirExists) {
                    Files.createDirectory(Paths.get(screenshotLocation));
                }
                File screenshotFile = new File(screenshotPathname);
                ImageIO.write(screenshotBufferedImage, filetype, screenshotFile);
                return screenshotFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private static String getTimeNow(String timeFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeFormat);
        LocalDateTime localTimeNow = LocalDateTime.now();
        return dateTimeFormatter.format(localTimeNow);
    }
}
