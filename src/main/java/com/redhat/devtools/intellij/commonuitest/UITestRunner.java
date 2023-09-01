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
package com.redhat.devtools.intellij.commonuitest;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.stepsProcessing.StepLogger;
import com.intellij.remoterobot.stepsProcessing.StepWorker;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.FlatWelcomeFrame;
import com.redhat.devtools.intellij.commonuitest.utils.runner.IntelliJVersion;

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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(UITestRunner.class.getName());
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final String USER_HOME = System.getProperty("user.home");
    private static RemoteRobot remoteRobot = null;
    private static Process ideProcess;
    private static IntelliJVersion ideaVersion;
    private static final String NEW_ITEM_PROPERTY = "New-ItemProperty";
    private static final String NAME_PARAM = "-Name";
    private static final String VALUE_PARAM = "-Value";

    private UITestRunner() {}

    /**
     * Start the given version of IntelliJ Idea listening on the given port
     *
     * @param ideaVersion version of the IntelliJ Idea to start
     * @param port        port number on which will the IntelliJ Idea be listening
     * @return instance of the RemoteRobot
     */
    public static RemoteRobot runIde(IntelliJVersion ideaVersion, int port) {
        StepWorker.registerProcessor(new StepLogger());

        return step("Start IntelliJ Idea ('" + ideaVersion.toString() + "') listening on port " + port, () -> {
            System.setProperty("uitestlib.idea.version", Integer.toString(ideaVersion.toInt()));
            UITestRunner.ideaVersion = ideaVersion;

            acceptAllTermsAndConditions();
            if (ideaVersion.isUltimate()) {
                activateEvaluateForFree();
            }

            String fileExtension = OS_NAME.contains("windows") ? ".bat" : "";
            ProcessBuilder pb = new ProcessBuilder("." + File.separator + "gradlew" + fileExtension, "runIdeForUiTests", "-PideaVersion=" + ideaVersion, "-Drobot-server.port=" + port);
            redirectProcessOutputs(pb);

            try {
                ideProcess = pb.start();
                waitUntilIntelliJStarts(port);
                remoteRobot = getRemoteRobotConnection(port);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
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
    public static RemoteRobot runIde(IntelliJVersion ideaVersion) {
        return runIde(ideaVersion, DEFAULT_PORT);
    }

    /**
     * Close the IntelliJ Idea IDE from the 'Welcome to IntelliJ IDEA' dialog
     */
    public static void closeIde() {
        ideProcess.destroy();
    }

    /**
     * Return the IdeaVersion representation of the currently running IntelliJ Idea version
     *
     * @return version of the currently running IntelliJ Idea
     */
    public static IntelliJVersion getIdeaVersion() {
        return ideaVersion;
    }

    /**
     * Return the integer representation of the currently running IntelliJ Idea version
     *
     * @return version of the currently running IntelliJ Idea
     */
    public static int getIdeaVersionInt() {
        return getIdeaVersion().toInt();
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
                        LOGGER.log(Level.SEVERE, e2.getMessage(), e2);
                        Thread.currentThread().interrupt();
                    }
                }
            }

            return remoteRobot;
        });
    }

    private static void acceptAllTermsAndConditions() {
        String osxPlistSourceLocation;
        if (ideaVersion.isUltimate()) {
            osxPlistSourceLocation = "plist/ultimate_all/com.apple.java.util.prefs.plist";
        } else if (ideaVersion.toInt() <= 20213) {
            osxPlistSourceLocation = "plist/2021_3_and_older/com.apple.java.util.prefs.plist";
        } else {
            osxPlistSourceLocation = "plist/2022_1/com.apple.java.util.prefs.plist";
        }

        String linuxPrefsXmlSourceLocation;
        if (ideaVersion.isUltimate()) {
            linuxPrefsXmlSourceLocation = "prefs_xml/ultimate_all/prefs.xml";
        } else if (ideaVersion.toInt() <= 20213) {
            linuxPrefsXmlSourceLocation = "prefs_xml/2021_3_and_older/prefs.xml";
        } else {
            linuxPrefsXmlSourceLocation = "prefs_xml/2022_1/prefs.xml";
        }

        if (OS_NAME.contains("linux")) {
            step("Copy the 'prefs.xml' file to the appropriate location", () -> {
                String prefsXmlDir = USER_HOME + "/.java/.userPrefs/jetbrains/_!(!!cg\"p!(}!}@\"j!(k!|w\"w!'8!b!\"p!':!e@==";
                createDirectoryHierarchy(prefsXmlDir);
                copyFileFromJarResourceDir(linuxPrefsXmlSourceLocation, prefsXmlDir + "/prefs.xml");
            });

            step(COPY_ACCEPTED_FILE_STEP_DESCRIPTION, () -> {
                String acceptedDir = USER_HOME + "/.local/share/JetBrains/consentOptions";
                createDirectoryHierarchy(acceptedDir);
                copyFileFromJarResourceDir(ACCEPTED_SOURCE_LOCATION, acceptedDir + "/accepted");
            });
        } else if (OS_NAME.contains("os x")) {
            step("Copy the 'com.apple.java.util.prefs.plist' file to the appropriate location", () -> {
                String plistDir = USER_HOME + "/Library/Preferences";
                copyFileFromJarResourceDir(osxPlistSourceLocation, plistDir + "/com.apple.java.util.prefs.plist");
            });

            step(COPY_ACCEPTED_FILE_STEP_DESCRIPTION, () -> {
                String acceptedDir = USER_HOME + "/Library/Application Support/JetBrains/consentOptions";
                createDirectoryHierarchy(acceptedDir);
                copyFileFromJarResourceDir(ACCEPTED_SOURCE_LOCATION, acceptedDir + "/accepted");

                // run the 'killall cfprefsd' cmd to force OS X to reload preferences files
                ProcessBuilder pb = new ProcessBuilder("/usr/bin/killall", "cfprefsd");
                try {
                    Process p = pb.start();
                    p.waitFor();
                } catch (IOException | InterruptedException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            });
        } else if (OS_NAME.contains("windows")) {
            step(COPY_ACCEPTED_FILE_STEP_DESCRIPTION, () -> {
                String acceptedDir = USER_HOME + "\\AppData\\Roaming\\JetBrains\\consentOptions";
                createDirectoryHierarchy(acceptedDir);
                copyFileFromJarResourceDir(ACCEPTED_SOURCE_LOCATION, acceptedDir + "\\accepted");
            });

            step("Create appropriate registry entries", () -> {
                String registryPath = "HKCU:" + "\\Software\\JavaSoft\\Prefs\\jetbrains\\privacy_policy";
                String powershellLocation = "C:\\Windows\\system32\\WindowsPowerShell\\v1.0\\powershell.exe";
                String powershellPathParameter = "-Path";
                ProcessBuilder pb1 = new ProcessBuilder(powershellLocation, "New-Item", powershellPathParameter, registryPath, "-Force");
                ProcessBuilder pb2 = new ProcessBuilder(powershellLocation, NEW_ITEM_PROPERTY, powershellPathParameter, registryPath, NAME_PARAM, "accepted_version", VALUE_PARAM, "'2.1'");
                ProcessBuilder pb3 = new ProcessBuilder(powershellLocation, NEW_ITEM_PROPERTY, powershellPathParameter, registryPath, NAME_PARAM, "euacommunity_accepted_version", VALUE_PARAM, "'1.0'");
                ProcessBuilder pb4 = new ProcessBuilder(powershellLocation, NEW_ITEM_PROPERTY, powershellPathParameter, registryPath, NAME_PARAM, "eua_accepted_version", VALUE_PARAM, "'1.2'");

                try {
                    Process p1 = pb1.start();
                    p1.waitFor();
                    Process p2 = pb2.start();
                    p2.waitFor();
                    Process p3 = pb3.start();
                    p3.waitFor();
                    Process p4 = pb4.start();
                    p4.waitFor();
                } catch (IOException | InterruptedException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    private static void activateEvaluateForFree() {
        String targetEvaluationKeysDir = System.getProperty("user.dir") + "/build/idea-sandbox/config-uiTest/eval/";
        createDirectoryHierarchy(targetEvaluationKeysDir);

        for (String ideaVersionSubstring : new String[]{"202", "203", "211", "212"}) {
            String keyFilename = "idea" + ideaVersionSubstring + ".evaluation.key";
            String sourcePathToKey = "evaluate_for_free_keys/" + keyFilename;
            String targetPathToKey = targetEvaluationKeysDir + keyFilename;
            copyFileFromJarResourceDir(sourcePathToKey, targetPathToKey);
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
        try (Socket socket = new Socket()) {
            connectToHost(socket, sockaddr);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static void connectToHost(Socket socket, SocketAddress sockaddr) throws IOException {
        socket.connect(sockaddr, 10000);
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void createDirectoryHierarchy(String location) {
        Path path = Paths.get(location);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static void copyFileFromJarResourceDir(String sourceFileLocation, String destFileLocation) {
        InputStream resourceStream = UITestRunner.class.getClassLoader().getResourceAsStream(sourceFileLocation);
        try (OutputStream outStream = new FileOutputStream(new File(destFileLocation))) {
            byte[] buffer = new byte[resourceStream.available()];
            int count = resourceStream.read(buffer);
            if (count == 0) {
                throw new UITestException("Reading from buffer was unsuccessful.");
            }
            outStream.write(buffer);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Redirect stdout and stderr of running subprocess to files
     *
     * @param pb    Process builder of running subprocess
     */
    private static void redirectProcessOutputs(ProcessBuilder pb) {
        String outDir = System.getProperty("user.home") + File.separator + "IntelliJ_debug";

        if (!new File(outDir).mkdirs()) {
            LOGGER.log(Level.SEVERE, "Cannot create user.home/debug directory");
        }

        File stdoutLog = new File(outDir + File.separator + "stdout.log");
        File stderrLog = new File(outDir + File.separator + "stderr.log");
        pb.redirectOutput(ProcessBuilder.Redirect.to(stdoutLog));
        pb.redirectError(ProcessBuilder.Redirect.to(stderrLog));
    }
}