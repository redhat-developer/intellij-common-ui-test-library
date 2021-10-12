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
package com.redhat.devtools.intellij.commonUiTestLibrary.exceptions;

/**
 * IntelliJ UI test library runtime exception
 *
 * @author zcervink@redhat.com
 */
public class UITestException extends RuntimeException {
    public UITestException(String errorMsg) {
        super(errorMsg);
    }
}
