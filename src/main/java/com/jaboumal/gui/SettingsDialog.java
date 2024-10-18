package com.jaboumal.gui;

import com.jaboumal.services.ConfigService;

import javax.swing.*;

import static com.jaboumal.constants.FilePaths.INPUT_COMPETITORS;
import static com.jaboumal.constants.FilePaths.INPUT_DOCX;

public class SettingsDialog {
    private JTextField inputTemplateNameField = new JTextField();
    private JTextField inputCompetitorFileNameField = new JTextField();


    public void showDialog(String inputTemplateFileName, String inputCompetitorFileName) {
        inputTemplateNameField.setText(inputTemplateFileName);
        inputCompetitorFileNameField.setText(inputCompetitorFileName);


        final JComponent[] inputs = new JComponent[]{
                new JLabel("Dateiname von Vorlage"),
                inputTemplateNameField,
                new JLabel("Dateiname von Teilnehmerliste"),
                inputCompetitorFileNameField,
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "My custom dialog", JOptionPane.DEFAULT_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            replaceProperties(inputTemplateFileName, inputCompetitorFileName);
        } else {
            System.out.println("User canceled / closed the dialog, result = " + result);
        }
    }

    private void replaceProperties(String inputTemplateFileName, String inputCompetitorFileName) {
        String templateInput = inputTemplateNameField.getText();
        if (!templateInput.equals(inputTemplateFileName) && templateInput.contains(".docx")) {
            ConfigService.replaceValue(INPUT_DOCX, inputTemplateNameField.getText());
        }

        String competitorInput = inputCompetitorFileNameField.getText();
        if (!competitorInput.equals(inputCompetitorFileName) && competitorInput.contains(".csv")) {
            ConfigService.replaceValue(INPUT_COMPETITORS, inputCompetitorFileNameField.getText());
        }

        System.out.println("You entered " +
                inputTemplateNameField.getText() + ", " +
                inputCompetitorFileNameField.getText());
    }
}
