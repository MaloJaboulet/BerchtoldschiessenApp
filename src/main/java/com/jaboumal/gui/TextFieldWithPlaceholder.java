package com.jaboumal.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This class is a JTextField that contains a placeholder.
 * The placeholder is displayed when the text field is empty and not focused.
 * When the text field is focused, the placeholder is removed.
 *
 * @author Malo Jaboulet
 */
public class TextFieldWithPlaceholder extends JTextField {
    private final String placeholder = "Vorname, Nachname, Schützennummer";

    /**
     * This constructor creates the TextFieldWithPlaceholder and initializes the components.
     */
    public TextFieldWithPlaceholder() {
        super();
        setText("Vorname, Nachname, Schützennummer");
        setDragEnabled(false);
        setFocusTraversalPolicyProvider(false);
        setFocusable(true);
        setRequestFocusEnabled(true);

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
