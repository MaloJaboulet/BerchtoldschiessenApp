package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jaboumal.constants.EventMessages;
import com.jaboumal.gui.customComponents.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is responsible for the print button in the main frame.
 *
 * @author Malo Jaboulet
 */
public class PrintPanel extends JPanel {

    private static CustomButton printButton;

    /**
     * Constructor of the class.
     */
    public PrintPanel() {
        printButton = new CustomButton("Print");
        printButton.setEnabled(false);
        setLayout(new GridLayoutManager(1, 1, new Insets(0, 50, 20, 0), -1, -1));
        add(printButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        printButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.createAndPrintStandblatt();
                EventMessagePanel.addSuccessMessage(EventMessages.PRINT_SUCCESSFUL);
            }
        });
    }

    /**
     * Method to enable the print button.
     */
    public static void makePrintButtonEnabled() {
        printButton.setEnabled(true);
    }

    /**
     * Method to disable the print button.
     */
    public static void makePrintButtonDisabled() {
        printButton.setEnabled(false);
    }
}
