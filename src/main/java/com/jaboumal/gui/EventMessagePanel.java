package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jaboumal.gui.customComponents.CustomLabel;

import javax.swing.*;
import java.awt.*;

public class EventMessagePanel extends JPanel {
    private static CustomLabel eventMessageLabel;

    public EventMessagePanel() {
        eventMessageLabel = new CustomLabel();
        setLayout(new GridLayoutManager(1, 1, new Insets(0, 50, 20, 0), -1, -1));
        eventMessageLabel.setVisible(false);
        add(eventMessageLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    public static void addErrorMessage(String errorMessage) {
        eventMessageLabel.setText(errorMessage);
        eventMessageLabel.setVisible(true);
        eventMessageLabel.setForeground(new Color(-4515017));
    }

    public static void addSuccessMessage(String successMessage) {
        eventMessageLabel.setText(successMessage);
        eventMessageLabel.setVisible(true);
        eventMessageLabel.setForeground(new Color(0x1BBB23));
    }

    public static void clearEventMessage() {
        eventMessageLabel.setVisible(false);
        eventMessageLabel.setText("");
    }
}
