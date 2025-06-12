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
