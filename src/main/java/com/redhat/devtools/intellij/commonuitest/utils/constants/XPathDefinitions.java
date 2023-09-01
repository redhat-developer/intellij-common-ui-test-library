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

/**
 * XPath definitions
 *
 * @author zcervink@redhat.com
 */
public class XPathDefinitions {
    public static final String MAIN_IDE_WINDOW = "//div[@class='IdeFrameImpl']";
    public static final String TOOL_WINDOWS_PANE = "//div[@class='ToolWindowsPane']";
    public static final String PROJECT_TOOL_WINDOW = "//div[@accessiblename='Project Tool Window']";
    public static final String BUILD_VIEW = "//div[@class='BuildView']";
    public static final String MAVEN_TOOL_WINDOW = "//div[@accessiblename='Maven Tool Window']";
    public static final String GRADLE_TOOL_WINDOW = "//div[@accessiblename='Gradle Tool Window']";
    public static final String LINUX_MAIN_MENU = "//div[@class='LinuxIdeMenuBar']";
    public static final String WINDOWS_MAIN_MENU_2022_2_AND_NEWER = "//div[@class='IdeMenuBar']";
    public static final String WINDOWS_MAIN_MENU_2020_3_TO_2022_1 = "//div[@class='MenuFrameHeader']";
    public static final String WINDOWS_MAIN_MENU_2020_2_AND_OLDER = "//div[@class='CustomHeaderMenuBar']";
    public static final String IDE_STATUS_BAR = "//div[@class='IdeStatusBarImpl']";
    public static final String INLINE_PROGRESS_PANEL = "//div[@class='InlineProgressPanel']";
    public static final String ENGRAVED_LABEL = "//div[@class='EngravedLabel']";
    public static final String FLAT_WELCOME_FRAME = "//div[@class='FlatWelcomeFrame']";
    public static final String NEW_PROJECT_DIALOG_WIZARD = "//div[@accessiblename='New Project' and @class='MyDialog']";
    public static final String DIALOG_ROOT_PANE = "//div[@class='DialogRootPane']";
    public static final String SEARCH_EVERYWHERE_POPUP = "//div[@accessiblename='Search everywhere' and @class='SearchEverywhereUI']";
    public static final String IDE_FATAL_ERRORS_DIALOG = "//div[@accessiblename='IDE Fatal Errors' and @class='MyDialog']";
    public static final String PROJECT_STRUCTURE_DIALOG = "//div[@accessiblename='Project Structure' and @class='MyDialog']";
    public static final String TIP_DIALOG = "//div[@accessiblename='Tip of the Day' and @class='MyDialog']";
    public static final String TIP_DIALOG_2 = "//div[@text='Tip of the Day']";
    public static final String RECENT_PROJECTS = "//div[@accessiblename='Recent Projects']";
    public static final String RECENT_PROJECT_PANEL_NEW = "//div[@class='NewRecentProjectPanel']";
    public static final String RECENT_PROJECT_PANEL_NEW_2 = "//div[@class='JBViewport']/*";
    public static final String IDE_ERROR_ICON = "//div[@class='IdeErrorsIcon']";
    public static final String BUILD_VIEW_EDITOR = "//div[@accessiblename='Editor']";
    public static final String JCOMBOBOX = "//div[@class='JComboBox']";
    public static final String MORE_SETTINGS_TITLED_SEPARATOR = "//div[@class='TitledSeparator']/../../*";
    public static final String ARTIFACTS_COORDINATES_DIALOG_PANEL = "//div[@class='DialogPanel']/*";
    public static final String HEAVY_WEIGHT_WINDOW = "//div[@class='HeavyWeightWindow']";
    public static final String JDK_COMBOBOX = "//div[@class='JdkComboBox']";
    public static final String MY_DIALOG = "//div[@class='MyDialog']";
    public static final String TREE = "//div[@class='Tree']";
    public static final String TOOLTIP_TEXT_PROJECT = "//div[@tooltiptext='Project']";
    public static final String TOOLTIP_TEXT_HIDE = "//div[contains(@myvisibleactions, 'View),')]//div[@tooltiptext='Hide']";
    public static final String MY_ICON_GEAR_PLAIN = "//div[contains(@myvisibleactions, 'View),')]//div[@myicon='gearPlain.svg']";
    public static final String MY_ICON_COLLAPSE_ALL = "//div[contains(@myvisibleactions, 'View),')]//div[@myicon='collapseall.svg']";
    public static final String MY_ICON_COLLAPSE_ALL_FOR = "//div[contains(@myvisibleactions, 'For')]//div[@myicon='collapseall.svg']";
    public static final String MY_ICON_COLLAPSE_ALL_IDE = "//div[contains(@myvisibleactions, 'IDE')]//div[@myicon='collapseall.svg']";
    public static final String MY_ICON_EXPAND_ALL = "//div[contains(@myvisibleactions, 'View),')]//div[@myicon='expandall.svg']";
    public static final String MY_ICON_EXPAND_ALL_IDE = "//div[contains(@myvisibleactions, 'IDE')]//div[@myicon='expandall.svg']";
    public static final String MY_ICON_LOCATE_SVG = "//div[@myicon='locate.svg']";
    public static final String MY_ICON_REFRESH = "//div[@myicon='refresh.svg']";
    public static final String CONTENT_COMBO_LABEL = "//div[@class='ContentComboLabel']";
    public static final String JBLIST = "//div[@class='JBList']";
    public static final String DIALOG_PANEL = "//div[@class='DialogPanel']";
    public static final String MY_LIST = "//div[@class='MyList']";
    public static final String CODE_WITH_ME_JPANEL = "//div[@class='Wrapper'][.//div[@class='JBLabel']]//div[@class='JPanel']";
    public static final String BREAD_CRUMBS = "//div[@class='Breadcrumbs']";
    public static final String EMPTY_PROJECT = "//div[@visible_text='Empty Project']";
    public static final String SINGLE_HEIGHT_LABEL = "//div[@class='SingleHeightLabel']";
    public static final String COLLAPSIBLE_TITLED_SEPARATOR = "//div[@class='CollapsibleTitledSeparator']";
    public static final String COLLAPSIBLE_TITLED_SEPARATOR_NEW = "//div[@class='CollapsibleTitledSeparatorImpl']";
    public static final String COLLAPSIBLE_TITLED_SEPARATOR_SIBLINGS = COLLAPSIBLE_TITLED_SEPARATOR + "/../*";
    public static final String COLLAPSIBLE_TITLED_SEPARATOR_NEW_SIBLINGS = COLLAPSIBLE_TITLED_SEPARATOR_NEW + "/../*";
    public static final String EXTENDABLE_TEXT_FIELD = "//div[@class='ExtendableTextField']";
    public static final String JBTEXT_FIELD = "//div[@class='JBTextField']";
    public static final String REMOVE_PROJECT_BUTTON = "//div[contains(@text.key, 'button.remove')]";
    public static final String SET_LANGUAGE = "//div[@class='SegmentedButtonComponent'][.//div[contains(@action.key, 'language.groovy')]]";
    public static final String SET_BUILD_SYSTEM = "//div[@class='SegmentedButtonComponent'][.//div[@action.key='buildsystem.type.intellij']]";
    public static final String GET_SET_MODULE_NAME = "//div[@accessiblename='Module name:' and @accessiblename.key='label.project.wizard.new.project.module.name' and @class='JBTextField']";
    public static final String GET_SET_CONTENT_ROOT = "//div[@accessiblename='Content root:' and @accessiblename.key='label.project.wizard.new.project.content.root' and @class='ExtendableTextField']";
    public static final String GET_SET_MODULE_FILE_LOCATION = "//div[@accessiblename='Module file location:' and @accessiblename.key='label.project.wizard.new.project.module.file.location' and @class='ExtendableTextField']";

    private XPathDefinitions() {
        throw new UITestException("Utility class with static methods.");
    }

    public static String label(String label) {
        return "//div[@text='" + label + "']";
    }

    public static String visibleTextLabel(String label) {
        return "//div[@visible_text='" + label + "']";
    }

    public static String jBOptionButton(String label) {
        return "//div[@class='JBOptionButton' and @text='" + label + "']";
    }

    public static String nonOpaquePanel(String label) {
        return "//div[@class='NonOpaquePanel'][./div[@text='" + label + "']]";
    }

    public static String toolWindowSvg(String label) {
        return "//div[@disabledicon='toolWindow" + label + ".svg']";
    }
}
