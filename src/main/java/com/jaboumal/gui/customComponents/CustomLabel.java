package com.jaboumal.gui.customComponents;

import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JLabel {
    public CustomLabel() {
        createFont();
    }

    public CustomLabel(Icon image) {
        super(image);
        createFont();
    }

    public CustomLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        createFont();
    }

    public CustomLabel(String text) {
        super(text);
        createFont();
    }

    public CustomLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        createFont();
    }

    public CustomLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        createFont();
    }

    private void createFont(){
        Font font = new Font("Segoe UI", Font.BOLD, 14);
        setFont(font);
    }
}
