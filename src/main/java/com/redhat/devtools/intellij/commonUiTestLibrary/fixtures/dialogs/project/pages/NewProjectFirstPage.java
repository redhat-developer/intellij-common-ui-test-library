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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.project.pages;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.ComboBoxFixture;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JListFixture;
import com.intellij.remoterobot.fixtures.JPopupMenuFixture;
import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * New Project dialog first page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = "//div[@class='DialogRootPane']")
@FixtureName(name = "New Project Dialog")
public class NewProjectFirstPage extends CommonContainerFixture {
    private static RemoteRobot remoteRobot;
    private int projectSdkItemsCount = -1;

    public NewProjectFirstPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
        step("Create fixture - New Project dialog first page", () -> {
            this.remoteRobot = remoteRobot;
        });
    }

    /**
     * Set the project type to specific type
     *
     * @param projectType name of the project type to which will be changed the current settings
     */
    public void selectNewProjectType(String projectType) {
        step("Set the project type to specific type", () -> {
            jLists(JListFixture.Companion.byType()).get(0).findText(projectType).click();
        });
    }

    /**
     * Set the project SDK to specific option
     *
     * @param targetSdkName name of the SDK to which will be changed the current settings
     */
    public void setProjectSdkIfAvailable(String targetSdkName) {
        step("Select the '" + targetSdkName + "' as new project SDK", () -> {
            ComboBoxFixture projectJdkComboBox = comboBox(byXpath("//div[@accessiblename='Project SDK:' and @class='JPanel']/div[@class='JdkComboBox']"), Duration.ofSeconds(10));
            String currentlySelectedProjectSdk = TextUtils.listOfRemoteTextToString(projectJdkComboBox.findAllText());
            if (currentlySelectedProjectSdk.contains(targetSdkName)) {
                return;
            }
            projectJdkComboBox.click();

            CommonContainerFixture parentFixture = waitFor(Duration.ofSeconds(20), Duration.ofSeconds(2), "The project JDK list did not load all items in 20 seconds.", () -> didProjectSdkListLoadAllItems());
            JPopupMenuFixture projectSdkList = parentFixture.jPopupMenus(byXpath("//div[@class='HeavyWeightWindow']")).get(0); // issue https://github.com/JetBrains/intellij-ui-test-robot/issues/104
            List<RemoteText> sdkItems = projectSdkList.findAllText();
            for (RemoteText sdkItem : sdkItems) {
                if (sdkItem.getText().contains(targetSdkName)) {
                    try {
                        sdkItem.click();
                    } catch (Exception e) {
                        // issue #83 in the Remote-Robot framework - https://github.com/JetBrains/intellij-ui-test-robot/issues/83
                        break;
                    }
                }
            }
        });
    }

    private kotlin.Pair<Boolean, CommonContainerFixture> didProjectSdkListLoadAllItems() {
        return step("Test whether the 'Project SDK' list has loaded all items", () -> {
            CommonContainerFixture parentFixture = remoteRobot.find(CommonContainerFixture.class, byXpath("//div[@class='MyDialog']"));
            JPopupMenuFixture projectSdkList = parentFixture.jPopupMenus(byXpath("//div[@class='HeavyWeightWindow']")).get(0); // issue https://github.com/JetBrains/intellij-ui-test-robot/issues/104
            List<RemoteText> sdkItems = projectSdkList.findAllText();
            int currentSdkItemsCount = sdkItems.size();

            if (projectSdkItemsCount == -1) {
                projectSdkItemsCount = currentSdkItemsCount;
                return new kotlin.Pair(false, parentFixture);
            } else if (projectSdkItemsCount != currentSdkItemsCount) {
                projectSdkItemsCount = currentSdkItemsCount;
                return new kotlin.Pair(false, parentFixture);
            }
            return new kotlin.Pair(true, parentFixture);
        });
    }
}