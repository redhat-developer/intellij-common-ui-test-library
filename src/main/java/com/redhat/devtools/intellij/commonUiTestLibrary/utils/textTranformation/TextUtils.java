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
package com.redhat.devtools.intellij.commonUiTestLibrary.utils.textTranformation;

import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Static utilities that assist and simplify data conversion and transformation
 *
 * @author zcervink@redhat.com
 */
public class TextUtils {
    /**
     * Transform a List of RemoteText labels to one String
     *
     * @param data List of RemoteText instances
     * @return String containing a concatenation of all the labels in the 'data' List
     */
    public static String listOfRemoteTextToString(List<RemoteText> data) {
        List<String> listOfStrings = data
                .stream()
                .map(RemoteText::getText)
                .collect(Collectors.toList());

        String concatString = String.join("", listOfStrings);

        return concatString;
    }
}