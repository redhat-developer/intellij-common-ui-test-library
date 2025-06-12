/*******************************************************************************
 * Copyright (c) 2025 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.utils.project;

/**
 * Enumeration for new project type
 */
public enum NewProjectType {
    PLAIN_JAVA("Java"),
    MAVEN("Maven"),
    GRADLE("Gradle"),
    NEW_PROJECT("New Project"),
    EMPTY_PROJECT("Empty Project");

    private final String projectType;

    NewProjectType(String projectType) {
        this.projectType = projectType;
    }

    @Override
    public String toString() {
        return this.projectType;
    }
}
