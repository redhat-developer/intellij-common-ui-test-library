/*******************************************************************************
 * Copyright (c) 2022 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.utils.runner;

/**
 * Enumeration for supported versions of the IntelliJ Idea
 * <p>
 * * @author zcervink@redhat.com
 */
public enum IntelliJVersion {
    COMMUNITY_V_2020_2("IC-2020.2"),
    COMMUNITY_V_2020_3("IC-2020.3"),
    COMMUNITY_V_2021_1("IC-2021.1"),
    COMMUNITY_V_2021_2("IC-2021.2"),
    COMMUNITY_V_2021_3("IC-2021.3"),
    COMMUNITY_V_2022_1("IC-2022.1"),
    COMMUNITY_V_2022_2("IC-2022.2"),
    COMMUNITY_V_2022_3("IC-2022.3"),
    COMMUNITY_V_2023_1("IC-2023.1"),
    COMMUNITY_V_2023_2("IC-2023.2"),
    COMMUNITY_V_2023_3("IC-2023.2"),
    COMMUNITY_V_2024_1("IC-2024.1"),
    COMMUNITY_V_2024_2("IC-2024.2"),
    COMMUNITY_V_2024_3("IC-2024.3");

    private final String ideaVersionStringRepresentation;
    private final int ideaVersionIntRepresentation;

    IntelliJVersion(String ideaVersionStringRepresentation) {
        this.ideaVersionStringRepresentation = ideaVersionStringRepresentation;

        String ideaVersionString = this.ideaVersionStringRepresentation.substring(3).replace(".", "");
        this.ideaVersionIntRepresentation = Integer.parseInt(ideaVersionString);
    }

    public static IntelliJVersion getFromStringVersion(String aVersion) {
        for (IntelliJVersion v : values()) {
            if (v.ideaVersionStringRepresentation.endsWith(aVersion)) return v;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return this.ideaVersionStringRepresentation;
    }

    public int toInt() {
        return this.ideaVersionIntRepresentation;
    }

}
