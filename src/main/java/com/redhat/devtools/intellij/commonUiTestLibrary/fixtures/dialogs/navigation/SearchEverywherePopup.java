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
package com.redhat.devtools.intellij.commonUiTestLibrary.fixtures.dialogs.navigation;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.dataExtractor.RemoteText;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;

/**
 * Search everywhere popup fixture
 *
 * @author zcervink@redhat.com
 */
@DefaultXpath(by = "SearchEverywhereUI type", xpath = "//div[@accessiblename='Search everywhere' and @class='SearchEverywhereUI']")
@FixtureName(name = "Search Everywhere Popup")
public class SearchEverywherePopup extends CommonContainerFixture {
    public SearchEverywherePopup(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    /**
     * Activate given tab
     *
     * @param tabName name of the tab in the Search Everywhere popup
     */
    public void activateTab(String tabName) {
        ComponentFixture tab = find(ComponentFixture.class, byXpath("//div[@accessiblename='" + tabName + "' and @class='SETab' and @text='" + tabName + "']"));
        tab.click();
    }

    /**
     * Activate search field
     */
    public void activateSearchField() {
        find(ComponentFixture.class, byXpath("//div[@class='SearchField']")).click();
    }

    /**
     * Get search results
     *
     * @return search results
     */
    public List<RemoteText> getSearchResults() {
        ComponentFixture searchResultsList = find(ComponentFixture.class, byXpath("//div[@class='JBList']"));
        return searchResultsList.findAllText();
    }
}