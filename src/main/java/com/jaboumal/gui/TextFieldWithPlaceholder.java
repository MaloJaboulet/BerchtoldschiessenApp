package com.jaboumal.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextFieldWithPlaceholder extends JTextField {
    private final String placeholder = "Vorname, Nachname, Schützennummer";

    public TextFieldWithPlaceholder() {
        super();
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setText("");
                setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
                    setText(placeholder);
                }
            }
        });
    }
}
