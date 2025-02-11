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
package com.redhat.devtools.intellij.commonuitest.fixtures.dialogs.project.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.ComboBoxFixture;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.HeavyWeightWindowFixture;
import com.intellij.remoterobot.fixtures.JLabelFixture;
import com.intellij.remoterobot.fixtures.JListFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.intellij.remoterobot.fixtures.JTextFieldFixture;
import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.intellij.remoterobot.utils.WaitForConditionTimeoutException;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import com.redhat.devtools.intellij.commonuitest.utils.screenshot.ScreenshotUtils;
import com.redhat.devtools.intellij.commonuitest.utils.texttranformation.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * New Project dialog first page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = XPathDefinitions.DIALOG_ROOT_PANE)
@FixtureName(name = "New Project Dialog")
public class NewProjectFirstPage extends AbstractNewProjectFinalPage {
    private final RemoteRobot remoteRobot;
    private int projectSdkItemsCount = -1;
    private final int ideaVersion;

    public NewProjectFirstPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        this.remoteRobot = remoteRobot;
        this.ideaVersion = UITestRunner.getIdeaVersion().toInt();
    }

    /**
     * Set the project type to specific type
     *
     * @param projectType name of the project type to which will be changed the current settings
     */
    public void selectNewProjectType(String projectType) {
        jLists(JListFixture.Companion.byType()).get(0).findText(projectType).click();
    }

    /**
     * Set the project name
     *
     * @param projectName project name
     */
    @Override
    public void setProjectName(String projectName) {
        find(JTextFieldFixture.class, byXpath(XPathDefinitions.JBTEXT_FIELD)).setText(projectName);
    }

    public JTextFieldFixture getProjectNameTextField() {
        return find(JTextFieldFixture.class, byXpath(XPathDefinitions.JBTEXT_FIELD), Duration.ofSeconds(10));
    }

    /**
     * Set the project language
     *
     * @param language project language
     */
    public void setLanguage(String language) {
        if (ideaVersion >= 20241) {
            JListFixture jListFixture = remoteRobot.find(JListFixture.class, byXpath(XPathDefinitions.JBLIST));
            jListFixture.clickItem(language, false);
        } else {
            findAll(JLabelFixture.class, byXpath(XPathDefinitions.SET_LANGUAGE)).get(0).findText(language).click();
        }
    }

    /**
     * Set the build system
     *
     * @param buildSystem build system type
     */
    public void setBuildSystem(String buildSystem) {
        if (ideaVersion >= 20241) {
            find(JLabelFixture.class, byXpath(XPathDefinitions.SET_BUILD_SYSTEM_2024_2_AND_NEWER)).findText(buildSystem).click();
        } else {
            find(JLabelFixture.class, byXpath(XPathDefinitions.SET_BUILD_SYSTEM)).findText(buildSystem).click();
        }
    }

    /**
     * Get the project SDK JdkComboBox
     *
     * @return JdkComboBox fixture
     */
    public ComboBoxFixture getProjectJdkComboBox() {
        if (ideaVersion >= 20241) {
            return comboBox(byXpath(XPathDefinitions.JDK_COMBOBOX_PROJECT_WIZARD), Duration.ofSeconds(10));
        }
        return comboBox(byXpath(XPathDefinitions.JDK_COMBOBOX), Duration.ofSeconds(10));
    }

    /**
     * Set the project SDK to specific option
     *
     * @param targetSdkName name of the SDK to which will be changed the current settings
     */
    public void setProjectSdkIfAvailable(String targetSdkName) {
        step("Select the '" + targetSdkName + "' as new project SDK", () -> {

            waitFor(
                Duration.ofSeconds(20),
                Duration.ofSeconds(5),
                "Waiting for 'resolving jdk' dialog to disappear.",
                () -> "Expected exactly one dialog but found " + remoteRobot.findAll(CommonContainerFixture.class, byXpath(XPathDefinitions.MY_DIALOG)).size(),
                () -> remoteRobot.findAll(CommonContainerFixture.class, byXpath(XPathDefinitions.MY_DIALOG)).size() == 1
            );

            ComboBoxFixture projectJdkComboBox = getProjectJdkComboBox();
            String currentlySelectedProjectSdk = TextUtils.listOfRemoteTextToString(projectJdkComboBox.findAllText());
            if (currentlySelectedProjectSdk.startsWith(targetSdkName)) {
                return;
            }

            if (UITestRunner.getIdeaVersionInt() >= 20221) {
                projectJdkComboBox.click();
                boolean popupOpenedPermanently = false;
                try {
                    waitFor(Duration.ofSeconds(10), Duration.ofMillis(250), "HeavyWeightWindow still visible.", this::noHeavyWeightWindowVisible);
                } catch (WaitForConditionTimeoutException e) {
                    popupOpenedPermanently = true;
                }
                if (!popupOpenedPermanently) {
                    projectJdkComboBox.click();
                }
            }

            CommonContainerFixture parentFixture = waitFor(Duration.ofSeconds(20), Duration.ofSeconds(2), "Wait for the 'Project SDK' list to finish loading all items.", "The project JDK list did not load all items in 20 seconds.", this::didProjectSdkListLoadAllItems);
            JPopupMenuFixture projectSdkList = parentFixture.jPopupMenus(byXpath(XPathDefinitions.HEAVY_WEIGHT_WINDOW)).get(0); // issue https://github.com/JetBrains/intellij-ui-test-robot/issues/104
            List<String> sdkItems = projectSdkList.jList().collectItems();
            Map<String, String> foundItems = new HashMap<>();
            sdkItems.forEach(item ->
                Arrays.stream(item.split(" ")).filter(s ->
                    s.startsWith(targetSdkName)).findFirst().ifPresent(s -> foundItems.put(s, item))
            );
            if (!foundItems.isEmpty()) {
                String label = foundItems.values().stream().findFirst().get();
                projectSdkList.jList().clickItem(label, true);
                ScreenshotUtils.takeScreenshot(remoteRobot);
                try {
                    waitFor(Duration.ofSeconds(10), Duration.ofMillis(250), "HeavyWeightWindow still visible.", this::noHeavyWeightWindowVisible);
                } catch (WaitForConditionTimeoutException e) {
                    // wait for "Resolving JDK..." dialog to disappear
                }
                ScreenshotUtils.takeScreenshot(remoteRobot);
            } else {
                ScreenshotUtils.takeScreenshot(remoteRobot, "No SDK found starting with " + targetSdkName);
            }

        });
    }

    private kotlin.Pair<Boolean, CommonContainerFixture> didProjectSdkListLoadAllItems() {
        return step("Test whether the 'Project SDK' list has loaded all items", () -> {
            CommonContainerFixture parentFixture = remoteRobot.find(CommonContainerFixture.class, byXpath(XPathDefinitions.MY_DIALOG));
            JPopupMenuFixture projectSdkList = parentFixture.jPopupMenus(byXpath(XPathDefinitions.HEAVY_WEIGHT_WINDOW)).get(0); // issue https://github.com/JetBrains/intellij-ui-test-robot/issues/104
            List<RemoteText> sdkItems = projectSdkList.findAllText();
            int currentSdkItemsCount = sdkItems.size();

            if ((projectSdkItemsCount == -1) || (projectSdkItemsCount != currentSdkItemsCount)) {
                projectSdkItemsCount = currentSdkItemsCount;
                return new kotlin.Pair<>(false, parentFixture);
            }
            return new kotlin.Pair<>(true, parentFixture);
        });
    }

    private boolean noHeavyWeightWindowVisible() {
        return remoteRobot.findAll(HeavyWeightWindowFixture.class).isEmpty();
    }
}