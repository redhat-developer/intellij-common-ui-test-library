package com.redhat.devtools.intellij.commonuitest.fixtures.mainidewindow.toolwindowspane;

import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.CommonContainerFixture;
import com.intellij.remoterobot.fixtures.DefaultXpath;
import com.intellij.remoterobot.fixtures.FixtureName;
import com.intellij.remoterobot.fixtures.JButtonFixture;
import com.redhat.devtools.intellij.commonuitest.UITestRunner;
import com.redhat.devtools.intellij.commonuitest.utils.constants.ButtonLabels;
import com.redhat.devtools.intellij.commonuitest.utils.constants.XPathDefinitions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.assertj.swing.core.MouseClickInfo.button;

@DefaultXpath(by = "ToolWindowRightToolbar type", xpath = "//div[@class='ToolWindowRightToolbar']")
@FixtureName(name = "Tool Window Right Toolbar")
public class ToolWindowRightToolbar extends CommonContainerFixture {

    public ToolWindowRightToolbar(@NotNull RemoteRobot remoteRobot, @NotNull RemoteComponent remoteComponent) {
        super(remoteRobot, remoteComponent);
    }

    public JButtonFixture stripeButton(String label) {
        return button(byXpath(XPathDefinitions.toolWindowButton(label)), Duration.ofSeconds(2));
    }

    public void clickStripeButton(String label) {
        stripeButton(label).click();
    }

    public void findStripeButton(String label) {
        stripeButton(label);
    }
}