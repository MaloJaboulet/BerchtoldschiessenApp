package com.jaboumal.gui.settings;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jaboumal.gui.customComponents.CustomButton;
import com.jaboumal.services.ConfigService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.jaboumal.constants.FilePaths.*;


/**
 * SettingsPanel class is responsible for creating the settings panel
 * in the main window.
 *
 * @author Malo Jaboulet
 */
public class SettingsPanel extends JPanel {

    /**
     * Creates the settings panel.
     */
    public void createSettingsPanel() {
        setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 20, 30), -1, -1));
        final Spacer spacer1 = new Spacer();
        add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));

        CustomButton button = new CustomButton("Settings");
        add(button, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             SettingsDialog settingsDialog = new SettingsDialog();
             settingsDialog.showDialog(ConfigService.getProperty(INPUT_GEWEHR_PDF), ConfigService.getProperty(INPUT_PISTOLE_PDF), ConfigService.getProperty(INPUT_COMPETITORS));
            }
        });
    }
}
