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
package com.redhat.devtools.intellij.commonuitest.utils.screenshot;

import com.intellij.remoterobot.RemoteRobot;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Static utilities for taking screenshots
 *
 * @author zcervink@redhat.com
 */
public class ScreenshotUtils {
    static final String SCREENSHOT_LOCATION = "." + File.separator + "build" + File.separator + "screenshots" + File.separator;
    static final String SCREENSHOT_FILENAME = getTimeNow("yyyy_MM_dd_HH_mm_ss");
    static final String FILETYPE = "png";
    static final String SCREENSHOT_PATHNAME = SCREENSHOT_LOCATION + SCREENSHOT_FILENAME + "." + FILETYPE;
    private static final Logger LOGGER = Logger.getLogger(ScreenshotUtils.class.getName());

    private ScreenshotUtils() {
        throw new UITestException("Screenshot utility class contains static utilities and cannot be instantiated.");
    }

    /**
     * Take screenshot of the entire screen and save it on disk
     *
     * @param remoteRobot reference to the RemoteRobot instance
     * @return the screenshot as a File object
     */
    public static File takeScreenshot(RemoteRobot remoteRobot) {
        try {
            BufferedImage screenshotBufferedImage = remoteRobot.getScreenshot();
            boolean doesScreenshotDirExists = Files.exists(Paths.get(SCREENSHOT_LOCATION));
            if (!doesScreenshotDirExists) {
                Files.createDirectory(Paths.get(SCREENSHOT_LOCATION));
            }
            File screenshotFile = new File(SCREENSHOT_PATHNAME);
            ImageIO.write(screenshotBufferedImage, FILETYPE, screenshotFile);
            return screenshotFile;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    private static String getTimeNow(String timeFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeFormat);
        LocalDateTime localTimeNow = LocalDateTime.now();
        return dateTimeFormatter.format(localTimeNow);
    }
}
