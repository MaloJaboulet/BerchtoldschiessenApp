package com.jaboumal.gui.customComponents;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for the custom button in the application.
 * It contains the method to create the font of the button.
 *
 * @author Malo Jaboulet
 */
public class CustomButton extends JButton {

    /**
     * Constructor of the class.
     */
    public CustomButton() {
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param a the action
     */
    public CustomButton(Action a) {
        super(a);
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param icon the icon of the button
     */
    public CustomButton(Icon icon) {
        super(icon);
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param text the text of the button
     */
    public CustomButton(String text) {
        super(text);
        createFont();
    }

    /**
     * Constructor of the class.
     *
     * @param text the text of the button
     * @param icon the icon of the button
     */
    public CustomButton(String text, Icon icon) {
        super(text, icon);
        createFont();
    }

    /**
     * Method to create the font of the button.
     */
    private void createFont(){
        Font font = new Font("Segoe UI", Font.BOLD, 14);
        setFont(font);
    }
}
