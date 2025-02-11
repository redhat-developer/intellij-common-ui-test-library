package com.redhat.devtools.intellij.commonuitest.utils.constants;

import java.io.File;
import java.util.Optional;

public final class ProjectLocation {
    // For more info on testProjectLocation please check README
    public static final String PROJECT_LOCATION = Optional.ofNullable(System.getProperty("testProjectLocation"))
        .filter(s -> !s.isEmpty())
        .orElseGet(() -> System.getProperty("user.home") + File.separator + "IdeaProjects" + File.separator + "intellij-ui-test-projects");

    private ProjectLocation() {}

}
