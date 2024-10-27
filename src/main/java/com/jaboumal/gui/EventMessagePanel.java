package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jaboumal.gui.customComponents.CustomLabel;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for the event message panel in the main frame.
 * It contains the event message label.
 * It also contains the methods to add an error message, a success message and to clear the event message.
 *
 * @author Malo Jaboulet
 */
public class EventMessagePanel extends JPanel {
    private static CustomLabel eventMessageLabel;

    /**
     * Constructor of the class.
     */
    public EventMessagePanel() {
        eventMessageLabel = new CustomLabel();
        setLayout(new GridLayoutManager(1, 1, new Insets(0, 50, 20, 0), -1, -1));
        eventMessageLabel.setVisible(false);
        add(eventMessageLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * Method to add an error message and display it.
     *
     * @param errorMessage the error message
     */
    public static void addErrorMessage(String errorMessage) {
        eventMessageLabel.setText(errorMessage);
        eventMessageLabel.setVisible(true);
        eventMessageLabel.setForeground(new Color(-4515017));
    }

    /**
     * Method to add a success message and display it.
     *
     * @param successMessage the success message
     */
    public static void addSuccessMessage(String successMessage) {
        eventMessageLabel.setText(successMessage);
        eventMessageLabel.setVisible(true);
        eventMessageLabel.setForeground(new Color(0x1BBB23));
    }

    /**
     * Method to clear the event message.
     */
    public static void clearEventMessage() {
        eventMessageLabel.setVisible(false);
        eventMessageLabel.setText("");
    }
}
