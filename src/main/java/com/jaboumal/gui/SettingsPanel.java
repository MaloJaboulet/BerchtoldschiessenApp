package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jaboumal.services.ConfigService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.jaboumal.constants.FilePaths.INPUT_COMPETITORS;
import static com.jaboumal.constants.FilePaths.INPUT_DOCX;

public class SettingsPanel extends JPanel {

    public void createSettingsPanel() {
        setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 20, 30), -1, -1));
        final Spacer spacer1 = new Spacer();
        add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));

        JButton button = new JButton("Settings");
        button.setText("Settings");
        add(button, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             SettingsDialog settingsDialog = new SettingsDialog();
             settingsDialog.showDialog(ConfigService.getProperty(INPUT_DOCX), ConfigService.getProperty(INPUT_COMPETITORS));
            }
        });
    }
}
