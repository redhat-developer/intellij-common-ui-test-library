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
package com.redhat.devtools.intellij.commonUiTestLibrary;

import org.junit.jupiter.api.Test;
import com.redhat.devtools.intellij.commonUiTestLibrary.UITestRunner;

/**
 * Basic JUnit tests to test the library
 *
 * @author zcervink@redhat.com
 */
class UITestRunnerTest {
    @Test
    void testRunIdeForUiTests() {
        UITestRunner.runIde("IC-2021.1", 8580);
        UITestRunner.closeIde();
    }
}