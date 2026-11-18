/*******************************************************************************
 * Copyright (c) 2026 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.utils.constants;

import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;

import java.time.Duration;

/**
 * UI test timeout constants
 *
 * @author mszuc@redhat.com
 */
public class UITestTimeouts {
    /**
     * Very quick timeout for immediate checks (1 second)
     */
    public static final Duration VERY_QUICK_TIMEOUT = Duration.ofSeconds(1);

    /**
     * Quick timeout for fast operations and quick checks (2 seconds)
     */
    public static final Duration QUICK_TIMEOUT = Duration.ofSeconds(2);

    /**
     * Short timeout for buttons, dialogs, and simple UI operations (5 seconds)
     */
    public static final Duration SHORT_TIMEOUT = Duration.ofSeconds(5);

    /**
     * Standard timeout for fixture searches and most UI elements (10 seconds)
     */
    public static final Duration FIXTURE_TIMEOUT = Duration.ofSeconds(10);

    /**
     * Extended timeout for UI element searches requiring more time (15 seconds)
     */
    public static final Duration UI_ELEMENT_TIMEOUT = Duration.ofSeconds(15);

    /**
     * Long timeout for slow operations like builds and indexing (30 seconds)
     */
    public static final Duration LONG_TIMEOUT = Duration.ofSeconds(30);

    /**
     * Fast poll interval for frequent UI state checks (250 milliseconds)
     */
    public static final Duration FAST_POLL_INTERVAL = Duration.ofMillis(250);

    /**
     * Poll interval for checking completion of long-running operations (3 seconds)
     */
    public static final Duration POLL_INTERVAL = Duration.ofSeconds(3);

    /**
     * Medium timeout for operations like SDK list loading (20 seconds)
     */
    public static final Duration MEDIUM_TIMEOUT = Duration.ofSeconds(20);

    /**
     * Timeout for tree expansion operations (60 seconds / 1 minute)
     */
    public static final Duration TREE_EXPANSION_TIMEOUT = Duration.ofSeconds(60);

    /**
     * Timeout for build completion (300 seconds / 5 minutes)
     */
    public static final Duration BUILD_TIMEOUT = Duration.ofSeconds(300);

    /**
     * Timeout for IDE startup (600 seconds / 10 minutes)
     */
    public static final Duration STARTUP_TIMEOUT = Duration.ofSeconds(600);

    private UITestTimeouts() {
        throw new UITestException("Utility class with static methods.");
    }
}
