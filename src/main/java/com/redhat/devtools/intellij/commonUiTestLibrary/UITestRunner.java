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
package com.redhat.devtools.intellij.commonUiTestLibrary;

import com.intellij.remoterobot.RemoteRobot;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.WelcomeFrameDialog;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.GlobalUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Basic methods for starting and quiting the IntelliJ Idea IDE for UI tests
 *
 * @author zcervink@redhat.com
 */
public class UITestRunner {
    private static final int defaultPort = 8580;
    private static RemoteRobot robot = null;
    private static Process ideProcess;

    public static RemoteRobot runIde(String ideaVersion, int port) {
        makeSureAllTermsAndConditionsAreAccepted();

        String osName = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;
        if (osName.contains("windows")) {
            pb = new ProcessBuilder(".\\gradlew.bat", "runIdeForUiTests", "-PideaVersion=" + ideaVersion, "-Drobot-server.port=" + port);
        } else {
            pb = new ProcessBuilder("./gradlew", "runIdeForUiTests", "-PideaVersion=" + ideaVersion, "-Drobot-server.port=" + port);
        }

        try {
            ideProcess = pb.start();
            GlobalUtils.waitUntilIntelliJStarts(port);
            robot = GlobalUtils.getRemoteRobotConnection(port);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        GlobalUtils.clearTheWorkspace();
        return robot;
    }

    public static RemoteRobot runIde(String ideaVersion) {
        return runIde(ideaVersion, defaultPort);
    }

    public static void closeIde() {
        if (robot.isWin()) {
            robot.find(WelcomeFrameDialog.class, Duration.ofSeconds(10)).windowsCloseButton().click();
        } else {
            ideProcess.destroy();
        }
    }

    public static RemoteRobot getRemoteRobotInstance() {
        return robot;
    }

    private static void makeSureAllTermsAndConditionsAreAccepted() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("linux")) {
            String prefsXmlSourceLocation = "prefs.xml";
            String prefsXmlDir = System.getProperty("user.home") + "/.java/.userPrefs/jetbrains/_!(!!cg\"p!(}!}@\"j!(k!|w\"w!'8!b!\"p!':!e@==";
            createDirectoryHierarchy(prefsXmlDir);
            copyFileFromJarResourceDir(prefsXmlSourceLocation, prefsXmlDir + "/prefs.xml");

            String acceptedSourceLocation = "accepted";
            String acceptedDir = System.getProperty("user.home") + "/.local/share/JetBrains/consentOptions";
            createDirectoryHierarchy(acceptedDir);
            copyFileFromJarResourceDir(acceptedSourceLocation, acceptedDir + "/accepted");
        } else if (osName.contains("os x")) {
            String plistSourceLocation = "com.apple.java.util.prefs.plist";
            String plistDir = System.getProperty("user.home") + "/Library/Preferences";
            copyFileFromJarResourceDir(plistSourceLocation, plistDir + "/com.apple.java.util.prefs.plist");

            String acceptedSourceLocation = "accepted";
            String acceptedDir = System.getProperty("user.home") + "/Library/Application Support/JetBrains/consentOptions";
            createDirectoryHierarchy(acceptedDir);
            copyFileFromJarResourceDir(acceptedSourceLocation, acceptedDir + "/accepted");

            // run the 'killall cfprefsd' cmd to force OS X to reload preferences files
            ProcessBuilder pb = new ProcessBuilder("killall", "cfprefsd");
            try {
                Process p = pb.start();
                p.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (osName.contains("windows")) {
            String acceptedSourceLocation = "accepted";
            String acceptedDir = System.getProperty("user.home") + "\\AppData\\Roaming\\JetBrains\\consentOptions";
            createDirectoryHierarchy(acceptedDir);
            copyFileFromJarResourceDir(acceptedSourceLocation, acceptedDir + "\\accepted");

            String registryPath = "HKCU:\\Software\\JavaSoft\\Prefs\\jetbrains\\privacy_policy";
            ProcessBuilder pb1 = new ProcessBuilder("powershell.exe", "New-Item", "-Path", registryPath, "-Force");
            ProcessBuilder pb2 = new ProcessBuilder("powershell.exe", "New-ItemProperty", "-Path", registryPath, "-Name", "accepted_version", "-Value", "'2.1'");

            try {
                Process p1 = pb1.start();
                p1.waitFor();
                Process p2 = pb2.start();
                p2.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createDirectoryHierarchy(String location) {
        Path path = Paths.get(location);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFileFromJarResourceDir(String sourceFileLocation, String destFileLocation) {
        InputStream resourceStream = UITestRunner.class.getClassLoader().getResourceAsStream(sourceFileLocation);
        try {
            byte[] buffer = new byte[resourceStream.available()];
            resourceStream.read(buffer);
            File targetFile = new File(destFileLocation);
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}