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
package com.redhat.devtools.intellij.commonuitest.utils.constants;

import com.redhat.devtools.intellij.commonuitest.exceptions.UITestException;
import org.intellij.lang.annotations.Language;

/**
 * XPath definitions
 *
 * @author zcervink@redhat.com
 */
public class XPathDefinitions {
    public static final String MAIN_IDE_WINDOW = "//div[@class='IdeFrameImpl']";
    public static final String PROJECT_TOOL_WINDOW = "//div[@accessiblename='Project Tool Window']";
    public static final String BUILD_VIEW = "//div[@class='BuildView']";
    public static final String MAVEN_TOOL_WINDOW = "//div[@accessiblename='Maven Tool Window']";
    public static final String GRADLE_TOOL_WINDOW = "//div[@accessiblename='Gradle Tool Window']";
    @Language("XPath")
    public static final String LINUX_MAIN_MENU = "//div[@class='LinuxIdeMenuBar']";
    @Language("XPath")
    public static final String WINDOWS_MAIN_MENU_2024_1_AND_NEWER = "//div[@class='IdeJMenuBar']";
    @Language("XPath")
    public static final String WINDOWS_MAIN_MENU_2022_2_TO_2023_2 = "//div[@class='IdeMenuBar']";
    public static final String IDE_STATUS_BAR = "//div[@class='IdeStatusBarImpl']";
    @Language("XPath")
    public static final String INLINE_PROGRESS_PANEL = "//div[@class='InlineProgressPanel']";
    @Language("XPath")
    public static final String ENGRAVED_LABEL = "//div[@class='EngravedLabel']";
    @Language("XPath")
    public static final String FLAT_WELCOME_FRAME = "//div[@class='FlatWelcomeFrame']";
    public static final String NEW_PROJECT_DIALOG_WIZARD = "//div[@accessiblename='New Project' and @class='MyDialog']";
    @Language("XPath")
    public static final String DIALOG_ROOT_PANE = "//div[@class='DialogRootPane']";
    public static final String SEARCH_EVERYWHERE_POPUP = "//div[@accessiblename='Search everywhere' and @class='SearchEverywhereUI']";
    public static final String IDE_FATAL_ERRORS_DIALOG = "//div[@accessiblename='IDE Fatal Errors' and @class='MyDialog']";
    public static final String PROJECT_STRUCTURE_DIALOG = "//div[@accessiblename='Project Structure' and @class='MyDialog']";
    public static final String TIP_DIALOG = "//div[@accessiblename='Tip of the Day' and @class='MyDialog']";
    @Language("XPath")
    public static final String TIP_DIALOG_2 = "//div[@text='Tip of the Day']";
    @Language("XPath")
    public static final String RECENT_PROJECT_PANEL_NEW = "//div[@class='NewRecentProjectPanel']";
    @Language("XPath")
    public static final String RECENT_PROJECT_PANEL_NEW_2 = "//div[@class='JBViewport']/*";
    @Language("XPath")
    public static final String IDE_ERROR_ICON = "//div[@class='IdeErrorsIcon']";
    @Language("XPath")
    public static final String BUILD_VIEW_EDITOR = "//div[@accessiblename='Editor']";
    @Language("XPath")
    public static final String HEAVY_WEIGHT_WINDOW = "//div[@class='HeavyWeightWindow']";
    @Language("XPath")
    public static final String JDK_COMBOBOX = "//div[@class='JdkComboBox']";
    @Language("XPath")
    public static final String JDK_COMBOBOX_PROJECT_WIZARD = "//div[@class='ProjectWizardJdkComboBox']"; // works for IntelliJ Idea 2024.1 and higher
    @Language("XPath")
    public static final String MY_DIALOG = "//div[@class='MyDialog']";
    @Language("XPath")
    public static final String TREE = "//div[@class='Tree']";
    @Language("XPath")
    public static final String TOOLTIP_TEXT_PROJECT = "//div[@tooltiptext='Project']";
    @Language("XPath")
    public static final String TOOLTIP_TEXT_HIDE = "//div[contains(@myvisibleactions, 'View),')]//div[@tooltiptext='Hide']";
    @Language("XPath")
    public static final String MAIN_MENU = "//div[@tooltiptext='Main Menu']";
    @Language("XPath")
    public static final String MY_ICON_GEAR_PLAIN = "//div[contains(@myvisibleactions, 'View),')]//div[@myicon='gearPlain.svg']";
    @Language("XPath")
    public static final String MY_ICON_MORE_VERTICAL = "//div[@myicon='moreVertical.svg']";
    @Language("XPath")
    public static final String MY_ICON_COLLAPSE_ALL = "//div[contains(@myvisibleactions, 'View),')]//div[@myicon='collapseall.svg']";
    @Language("XPath")
    public static final String MY_ICON_COLLAPSE_ALL_2024_2 = "//div[@myicon='collapseAll.svg']";
    @Language("XPath")
    public static final String MY_ICON_COLLAPSE_ALL_FOR = "//div[contains(@myvisibleactions, 'For')]//div[@myicon='collapseall.svg']";
    @Language("XPath")
    public static final String MY_ICON_COLLAPSE_ALL_IDE = "//div[contains(@myvisibleactions, 'IDE')]//div[@myicon='collapseall.svg']";
    @Language("XPath")
    public static final String MY_ICON_EXPAND_ALL = "//div[contains(@myvisibleactions, 'View),')]//div[@myicon='expandall.svg']";
    @Language("XPath")
    public static final String MY_ICON_EXPAND_ALL_2024_2 = "//div[@myicon='expandAll.svg']";
    @Language("XPath")
    public static final String MY_ICON_EXPAND_ALL_IDE = "//div[contains(@myvisibleactions, 'IDE')]//div[@myicon='expandall.svg']";
    @Language("XPath")
    public static final String MY_ICON_LOCATE_SVG = "//div[@myicon='locate.svg']";
    @Language("XPath")
    public static final String MY_ICON_REFRESH = "//div[@myicon='refresh.svg']";
    @Language("XPath")
    public static final String CONTENT_COMBO_LABEL = "//div[@class='ContentComboLabel']";
    @Language("XPath")
    public static final String JBLIST = "//div[@class='JBList']";
    @Language("XPath")
    public static final String DIALOG_PANEL = "//div[@class='DialogPanel']";
    public static final String CODE_WITH_ME_JPANEL = "//div[@class='Wrapper'][.//div[@class='JBLabel']]//div[@class='JPanel']";
    @Language("XPath")
    public static final String BREAD_CRUMBS = "//div[@class='Breadcrumbs']";
    @Language("XPath")
    public static final String COLLAPSIBLE_TITLED_SEPARATOR_NEW = "//div[@class='CollapsibleTitledSeparatorImpl']";
    @Language("XPath")
    public static final String COLLAPSIBLE_TITLED_SEPARATOR_NEW_SIBLINGS = COLLAPSIBLE_TITLED_SEPARATOR_NEW + "/../*";
    @Language("XPath")
    public static final String EXTENDABLE_TEXT_FIELD = "//div[@class='ExtendableTextField']";
    @Language("XPath")
    public static final String JBTEXT_FIELD = "//div[@class='JBTextField']";
    @Language("XPath")
    public static final String REMOVE_PROJECT_BUTTON = "//div[contains(@text.key, 'button.remove')]";
    @Language("XPath")
    public static final String SET_LANGUAGE = "//div[@class='SegmentedButtonComponent'][.//div[contains(@action.key, 'language.groovy')]]";
    @Language("XPath")
    public static final String SET_BUILD_SYSTEM = "//div[@class='SegmentedButtonComponent'][.//div[@visible_text='IntelliJ']]";
    @Language("XPath")
    public static final String SET_BUILD_SYSTEM_2024_2_AND_NEWER = "//div[@accessiblename='Build system:' and @class='SegmentedButtonComponent']";  // works for IntelliJ Idea 2024.1 and higher
    @Language("XPath")
    public static final String GET_SET_MODULE_NAME = "//div[@accessiblename='Module name:' and @class='JBTextField']";
    @Language("XPath")
    public static final String GET_SET_MODULE_NAME_2024_2_AND_NEWER = "//div[@accessiblename='Module name:' and @class='JBTextField']";
    @Language("XPath")
    public static final String GET_SET_CONTENT_ROOT = "//div[@accessiblename='Content root:' and @class='ExtendableTextField']";
    @Language("XPath")
    public static final String GET_SET_MODULE_FILE_LOCATION = "//div[@accessiblename='Module file location:' and @class='ExtendableTextField']";
    @Language("XPath")
    public static final String CREATE_NEW_PROJECT = "//div[@defaulticon='createNewProjectTab.svg']"; // works for IntelliJ Idea 2024.1 and higher
    @Language("XPath")
    public static final String PROJECT_LABEL = "//div[@accessiblename='Main.java' and @class='EditorTabLabel']//div[@class='ActionPanel']";
    public static final String TOOL_WINDOW_PANE = "//div[@class='ToolWindowPane']";
    public static final String WINDOW_LEFT_TOOLBAR = "//div[@class='ToolWindowLeftToolbar']";
    public static final String WINDOW_RIGHT_TOOLBAR = "//div[@class='ToolWindowRightToolbar']";
    @Language("XPath")
    public static final String TREE_FOR_20223 = "//div[@accessiblename='Welcome screen categories']";

    private XPathDefinitions() {
        throw new UITestException("Utility class with static methods.");
    }

    public static String label(String label) {
        return "//div[@text='" + label + "']";
    }

    public static String jBOptionButton(String label) {
        return "//div[@class='JBOptionButton' and @text='" + label + "']";
    }

    public static String nonOpaquePanel(String label) {
        return "//div[@class='NonOpaquePanel'][./div[@text='" + label + "']]";
    }

    public static String toolWindowButton(String label) {
        return "//div[@tooltiptext='" + label + "']";
    }
}
