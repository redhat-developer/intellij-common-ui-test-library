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
import com.intellij.remoterobot.fixtures.ContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.redhat.devtools.intellij.commonuitest.utils.constans.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constans.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static com.intellij.remoterobot.stepsProcessing.StepWorkerKt.step;

/**
 * New Project dialog maven project second page fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "MyDialog type", xpath = XPathDefinitions.DIALOG_ROOT_PANE)
@FixtureName(name = "New Project Dialog")
public class MavenGradleNewProjectFinalPage extends AbstractNewProjectFinalPage {
    public MavenGradleNewProjectFinalPage(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Open the 'Artifact Coordinates' options
     */
    public void openArtifactCoordinates() {
        step("Open the 'Artifact Coordinates' options", () -> {
            if (!isArtifactCoordinatesOpened()) {
                jLabel(ButtonLabels.ARTIFACT_COORDINATES).click();
            }
        });
    }

    /**
     * Close the 'Artifact Coordinates' options
     */
    public void closeArtifactCoordinates() {
        step("Close the 'Artifact Coordinates' options", () -> {
            if (isArtifactCoordinatesOpened()) {
                jLabel(ButtonLabels.ARTIFACT_COORDINATES).click();
            }
        });
    }

    /**
     * Get the group ID currently inserted in the 'GroupId' input field
     *
     * @return group ID currently inserted in the input field
     */
    public String getGroupId() {
        return textField("GroupId:", true).getText();
    }

    /**
     * Insert the group ID into the 'GroupId' input field
     *
     * @param groupId group ID that will be set into the input field
     */
    public void setGroupId(String groupId) {
        textField("GroupId:", true).setText(groupId);
    }

    /**
     * Get the artifact ID currently inserted in the 'ArtifactId' input field
     *
     * @return artifact ID currently inserted in the input field
     */
    public String getArtifactId() {
        return textField("ArtifactId:", true).getText();
    }

    /**
     * Insert the artifact ID into the 'ArtifactId' input field
     *
     * @param artifactId artifact ID that will be set into the input field
     */
    public void setArtifactId(String artifactId) {
        textField("ArtifactId:", true).setText(artifactId);
    }

    /**
     * Get the version currently inserted in the 'Version' input field
     *
     * @return version currently inserted in the input field
     */
    public String getVersion() {
        return textField("Version:", true).getText();
    }

    /**
     * Insert the version into the 'Version' input field
     *
     * @param version version that will be set into the input field
     */
    public void setVersion(String version) {
        textField("Version:", true).setText(version);
    }

    private boolean isArtifactCoordinatesOpened() {
        List<ContainerFixture> cf = findAll(ContainerFixture.class, byXpath(XPathDefinitions.ARTIFACTS_COORDINATES_DIALOG_PANEL));
        return cf.size() > 5;
    }
}