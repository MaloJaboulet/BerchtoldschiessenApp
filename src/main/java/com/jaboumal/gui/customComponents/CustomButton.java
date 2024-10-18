package com.jaboumal.gui.customComponents;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {

    public CustomButton() {
        createFont();
    }

    public CustomButton(Action a) {
        super(a);
        createFont();
    }

    public CustomButton(Icon icon) {
        super(icon);
        createFont();
    }

    public CustomButton(String text) {
        super(text);
        createFont();
    }

    public CustomButton(String text, Icon icon) {
        super(text, icon);
        createFont();
    }

    private void createFont(){
        Font font = new Font("Segoe UI", Font.BOLD, 14);
        setFont(font);
    }
}
