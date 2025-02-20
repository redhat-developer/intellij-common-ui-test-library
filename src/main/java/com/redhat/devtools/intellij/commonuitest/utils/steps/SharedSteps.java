/*******************************************************************************
 * Copyright (c) 2024 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors: mszuc@redhat.com
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.commonuitest.utils.steps;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.search.locators.Locator;
import java.time.Duration;
import static com.intellij.remoterobot.utils.RepeatUtilsKt.waitFor;

/**
 * SharedSteps provides a collection of utility methods that can be used across the entire IntelliJ platform.
 * These methods facilitate common automation steps that can be reused in various test scenarios.
 */
public class SharedSteps {
    private SharedSteps() {}

    /**
     * Waits for a component to be visible within the IDE's UI hierarchy based on the provided XPath locator.
     *
     * @param robot    The RemoteRobot instance for interaction with the IntelliJ IDEA UI.
     * @param duration The maximum time to wait for the component to become visible, in seconds.
     * @param interval The interval at which to check the component's visibility, in seconds.
     * @param xpath    The XPath locator used to find the component within the UI hierarchy.
     */
    public static void waitForComponentByXpath(RemoteRobot robot, int duration, int interval , Locator xpath) {
        waitFor(Duration.ofSeconds(duration), Duration.ofSeconds(interval), () -> robot.findAll(ComponentFixture.class, xpath)
                .stream()
                .anyMatch(ComponentFixture::isShowing));
    }
}
