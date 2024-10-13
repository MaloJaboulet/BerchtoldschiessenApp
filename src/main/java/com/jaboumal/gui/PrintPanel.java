package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jaboumal.constants.EventMessages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrintPanel extends JPanel {

    private static JButton printButton;

    public PrintPanel() {
        printButton = new JButton("Print");
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

    public static void makePrintButtonEnabled() {
        printButton.setEnabled(true);
    }
    public static void makePrintButtonDisabled() {
        printButton.setEnabled(false);
    }
}
