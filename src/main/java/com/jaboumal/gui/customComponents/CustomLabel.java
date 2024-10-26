package com.jaboumal.gui.customComponents;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for the custom label in the application.
 * It contains the method to create the font of the label.
 *
 * @author Malo Jaboulet
 */
public class CustomLabel extends JLabel {
    /**
     * Constructor of the class.
     */
    public CustomLabel() {
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param image the icon of the label
     */
    public CustomLabel(Icon image) {
        super(image);
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param image               the icon of the label
     * @param horizontalAlignment the horizontal alignment of the label
     */
    public CustomLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param text the text of the label
     */
    public CustomLabel(String text) {
        super(text);
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param text                the text of the label
     * @param horizontalAlignment the horizontal alignment of the label
     */
    public CustomLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param text                the text of the label
     * @param icon                the icon of the label
     * @param horizontalAlignment the horizontal alignment of the label
     */
    public CustomLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        createFont();
    }

    /**
     * Method to create the font of the label.
     */
    private void createFont() {
        Font font = new Font("Segoe UI", Font.BOLD, 14);
        setFont(font);
    }
}
