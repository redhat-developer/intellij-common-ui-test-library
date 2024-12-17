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
package com.redhat.devtools.intellij.commonuitest.utils.texttranformation;

import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;

import java.util.List;

/**
 * Static utilities that assist and simplify data conversion and transformation
 *
 * @author zcervink@redhat.com
 */
public class TextUtils {
    private TextUtils() {
        throw new UITestException("Text transformation utility class contains static utilities and cannot be instantiated.");
    }

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
                .toList();

        return String.join("", listOfStrings);
    }
}