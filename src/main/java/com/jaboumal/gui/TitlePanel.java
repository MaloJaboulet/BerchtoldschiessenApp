package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jaboumal.gui.customComponents.CustomLabel;

import javax.swing.*;
import java.awt.*;

/**
 * TitlePanel class
 * This class is a JPanel that contains the title of the application.
 *
 * @author Malo Jaboulet
 */
public class TitlePanel extends JPanel {
    private final CustomLabel title;

    /**
     * Constructor
     * This constructor initializes the title label and sets the layout of the panel.
     */
    public TitlePanel() {
        title = new CustomLabel();
        setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        Font titleFont = FontUtil.getFont("Segoe UI", Font.BOLD, 24, title.getFont());
        if (titleFont != null) title.setFont(titleFont);
        title.setHorizontalAlignment(0);
        title.setHorizontalTextPosition(11);
        title.setText("Berchtoldschiessen");
        add(title, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));

    }
}
