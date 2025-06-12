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
