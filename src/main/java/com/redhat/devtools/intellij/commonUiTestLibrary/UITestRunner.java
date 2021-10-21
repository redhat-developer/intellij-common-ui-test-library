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
import com.intellij.remoterobot.stepsProcessing.StepLogger;
import com.intellij.remoterobot.stepsProcessing.StepWorker;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.FlatWelcomeFrame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * Basic methods for starting and quiting the IntelliJ Idea IDE for UI tests
 *
 * @author zcervink@redhat.com
 */
public class UITestRunner {
    private static final int DEFAULT_PORT = 8580;
    private static final String ACCEPTED_SOURCE_LOCATION = "accepted";
    private static final String COPY_ACCEPTED_FILE_STEP_DESCRIPTION = "Copy the 'accepted' file to the appropriate location";
    private static RemoteRobot remoteRobot = null;
    private static Process ideProcess;
    private static IdeaVersion ideaVersion;

    /**
     * Start the given version of IntelliJ Idea listening on the given port
     *
     * @param ideaVersion version of the IntelliJ Idea to start
     * @param port        port number on which will the IntelliJ Idea be listening
     * @return instance of the RemoteRobot
     */
    public static RemoteRobot runIde(IdeaVersion ideaVersion, int port) {
        StepWorker.registerProcessor(new StepLogger());

        return step("Start IntelliJ Idea ('" + ideaVersion.toString() + "') listening on port " + port, () -> {
            UITestRunner.ideaVersion = ideaVersion;
            makeSureAllTermsAndConditionsAreAccepted();

            String osName = System.getProperty("os.name").toLowerCase();
            String fileExtension = osName.contains("windows") ? ".bat" : "";
            ProcessBuilder pb = new ProcessBuilder("." + File.separator + "gradlew" + fileExtension, "runIdeForUiTests", "-PideaVersion=" + ideaVersion.toString(), "-Drobot-server.port=" + port);

            try {
                ideProcess = pb.start();
                waitUntilIntelliJStarts(port);
                remoteRobot = getRemoteRobotConnection(port);
            } catch (IOException e) {
                e.printStackTrace();
            }

            remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10)).clearWorkspace();
            return remoteRobot;
        });
    }

    /**
     * Start the given version of IntelliJ Idea listening on the default port
     *
     * @param ideaVersion version of the IntelliJ Idea to start
     * @return instance of the RemoteRobot
     */
    public static RemoteRobot runIde(IdeaVersion ideaVersion) {
        return runIde(ideaVersion, DEFAULT_PORT);
    }

    /**
     * Close the IntelliJ Idea IDE from the 'Welcome to IntelliJ IDEA' dialog
     */
    public static void closeIde() {
        step("Close IntelliJ Idea", () -> {
            if (remoteRobot.isWin()) {
                remoteRobot.find(FlatWelcomeFrame.class, Duration.ofSeconds(10)).runJs("const horizontal_offset = component.getWidth() - 24;\n" +
                        "robot.click(component, new Point(horizontal_offset, 14), MouseButton.LEFT_BUTTON, 2);");
            } else {
                ideProcess.destroy();
            }
        });
    }

    /**
     * Return the integer representation of the currently running IntelliJ Idea version
     *
     * @return version of the currently running IntelliJ Idea
     */
    public static IdeaVersion getIdeaVersion() {
        return ideaVersion;
    }

    /**
     * Return the reference to the Remote Robot instance
     *
     * @return reference to the Remote Robot instance
     */
    public static RemoteRobot getRemoteRobot() {
        return remoteRobot;
    }

    /**
     * Create an instance of the RemoteRobot listening on the given port
     *
     * @param port port number
     * @return instance of the RemoteRobot
     */
    public static RemoteRobot getRemoteRobotConnection(int port) {
        return step("Create an instance of the RemoteRobot listening on port " + port, () -> {
            RemoteRobot remoteRobot = new RemoteRobot("http://127.0.0.1:" + port);
            for (int i = 0; i < 60; i++) {
                try {
                    remoteRobot.find(FlatWelcomeFrame.class);
                } catch (WaitForConditionTimeoutException e) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }

            return remoteRobot;
        });
    }

    /**
     * Enumeration for supported versions of the IntelliJ Idea
     */
    public enum IdeaVersion {
        V_2020_2("IC-2020.2"),
        V_2020_3("IC-2020.3");

        private final String ideaVersionStringRepresentation;

        IdeaVersion(String ideaVersionStringRepresentation) {
            this.ideaVersionStringRepresentation = ideaVersionStringRepresentation;
        }

        @Override
        public String toString() {
            return ideaVersionStringRepresentation;
        }

        public int toInt() {
            String ideaVersion = this.ideaVersionStringRepresentation.substring(3).replace(".", "");
            return Integer.parseInt(ideaVersion);
        }
    }

    private static void makeSureAllTermsAndConditionsAreAccepted() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("linux")) {
            step("Copy the 'prefs.xml' file to the appropriate location", () -> {
                String prefsXmlSourceLocation = "prefs.xml";
                String prefsXmlDir = System.getProperty("user.home") + "/.java/.userPrefs/jetbrains/_!(!!cg\"p!(}!}@\"j!(k!|w\"w!'8!b!\"p!':!e@==";
                createDirectoryHierarchy(prefsXmlDir);
                copyFileFromJarResourceDir(prefsXmlSourceLocation, prefsXmlDir + "/prefs.xml");
            });

            step(COPY_ACCEPTED_FILE_STEP_DESCRIPTION, () -> {
                String acceptedDir = System.getProperty("user.home") + "/.local/share/JetBrains/consentOptions";
                createDirectoryHierarchy(acceptedDir);
                copyFileFromJarResourceDir(ACCEPTED_SOURCE_LOCATION, acceptedDir + "/accepted");
            });
        } else if (osName.contains("os x")) {
            step("Copy the 'com.apple.java.util.prefs.plist' file to the appropriate location", () -> {
                String plistSourceLocation = "com.apple.java.util.prefs.plist";
                String plistDir = System.getProperty("user.home") + "/Library/Preferences";
                copyFileFromJarResourceDir(plistSourceLocation, plistDir + "/com.apple.java.util.prefs.plist");
            });

            step(COPY_ACCEPTED_FILE_STEP_DESCRIPTION, () -> {
                String acceptedDir = System.getProperty("user.home") + "/Library/Application Support/JetBrains/consentOptions";
                createDirectoryHierarchy(acceptedDir);
                copyFileFromJarResourceDir(ACCEPTED_SOURCE_LOCATION, acceptedDir + "/accepted");

                // run the 'killall cfprefsd' cmd to force OS X to reload preferences files
                ProcessBuilder pb = new ProcessBuilder("killall", "cfprefsd");
                try {
                    Process p = pb.start();
                    p.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } else if (osName.contains("windows")) {
            step(COPY_ACCEPTED_FILE_STEP_DESCRIPTION, () -> {
                String acceptedDir = System.getProperty("user.home") + "\\AppData\\Roaming\\JetBrains\\consentOptions";
                createDirectoryHierarchy(acceptedDir);
                copyFileFromJarResourceDir(ACCEPTED_SOURCE_LOCATION, acceptedDir + "\\accepted");
            });

            step("Create appropriate registry entries", () -> {
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
            });
        }
    }

    private static void waitUntilIntelliJStarts(int port) {
        waitFor(Duration.ofSeconds(600), Duration.ofSeconds(3), "The IntelliJ Idea did not start in 10 minutes.", () -> isIntelliJUIVisible(port));
    }

    private static boolean isIntelliJUIVisible(int port) {
        return isHostOnIpAndPortAccessible("127.0.0.1", port);
    }

    private static boolean isHostOnIpAndPortAccessible(String ip, int port) {
        SocketAddress sockaddr = new InetSocketAddress(ip, port);
        Socket socket = new Socket();

        try {
            socket.connect(sockaddr, 10000);
        } catch (IOException e) {
            return false;
        }
        return true;
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