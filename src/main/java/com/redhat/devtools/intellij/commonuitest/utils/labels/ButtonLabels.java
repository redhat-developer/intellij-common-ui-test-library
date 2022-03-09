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
package com.redhat.devtools.intellij.commonuitest.utils.labels;

import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;

/**
 * Button labels
 *
 * @author zcervink@redhat.com
 */
public class ButtonLabels {
    private ButtonLabels() {
        throw new UITestException("Utility class with static methods.");
    }

    public static final String CLEAR_ALL_LABEL = "Clear all";
    public static final String CLOSE_LABEL = "Close";
    public static final String CANCEL_LABEL = "Cancel";
    public static final String NEXT_LABEL = "Next";
    public static final String PREVIOUS_LABEL = "Previous";
    public static final String FINISH_LABEL = "Finish";
    public static final String PROJECT_STRIPE_BUTTON_LABEL = "Project";
    public static final String MAVEN_STRIPE_BUTTON_LABEL = "Maven";
    public static final String GRADLE_STRIPE_BUTTON_LABEL = "Gradle";
    public static final String GOT_IT_LABEL = "Got It";
    public static final String MORE_SETTINGS = "More Settings";
    public static final String ARTIFACT_COORDINATES = "Artifact Coordinates";
}
