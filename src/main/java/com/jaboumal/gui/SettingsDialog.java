package com.jaboumal.gui;

import com.jaboumal.constants.FilePaths;
import com.jaboumal.gui.customComponents.CustomButton;
import com.jaboumal.gui.customComponents.CustomLabel;
import com.jaboumal.services.FileReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.jaboumal.constants.FilePaths.INPUT_COMPETITORS_PATH;
import static com.jaboumal.constants.FilePaths.INPUT_DOCX_PATH;

public class SettingsDialog {
    private final static Logger log = LoggerFactory.getLogger(SettingsDialog.class);
    private final JTextField inputTemplateNameField = new JTextField();
    private final JTextField inputCompetitorFileNameField = new JTextField();
    private final CustomButton buttonPrintTemplate = new CustomButton();
    private final CustomButton buttonCompetitor = new CustomButton();


    public void showDialog(String inputTemplateFileName, String inputCompetitorFileName) {
        final JComponent[] inputs = new JComponent[]{
                createInputTemplateNameComponents(inputTemplateFileName),
                createCompetitorNameComponents(inputCompetitorFileName),
        };

        UIManager.put("OptionPane.border", new EmptyBorder(20, 50, 20, 50));
        JOptionPane.showConfirmDialog(null, inputs, "Settings", JOptionPane.DEFAULT_OPTION);
    }

    private JPanel createCompetitorNameComponents(String inputCompetitorFileName) {
        inputCompetitorFileNameField.setText(inputCompetitorFileName);
        inputCompetitorFileNameField.setFocusable(false);

        buttonCompetitor.setIcon(UIManager.getIcon("FileView.fileIcon"));
        buttonCompetitor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile = selectFile("csv");
                if (selectedFile != null) {
                    inputCompetitorFileNameField.setText(selectedFile.getName());
                    //Copy selected file to Berchtold folder
                    FileReaderService.copyFile(selectedFile.getPath(), FilePaths.getPath(INPUT_COMPETITORS_PATH));
                }
            }
        });

        return createButtonTextfieldLabelPanel(inputCompetitorFileNameField, buttonCompetitor, "Dateiname von Teilnehmerliste");
    }

    private JPanel createInputTemplateNameComponents(String inputTemplateFileName) {
        inputTemplateNameField.setText(inputTemplateFileName);
        inputTemplateNameField.setFocusable(false);

        buttonPrintTemplate.setIcon(UIManager.getIcon("FileView.fileIcon"));
        buttonPrintTemplate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile = selectFile("docx");
                if (selectedFile != null) {
                    inputTemplateNameField.setText(selectedFile.getName());
                    //Copy selected file to Berchtold folder
                    FileReaderService.copyFile(selectedFile.getPath(), FilePaths.getPath(INPUT_DOCX_PATH));
                }
            }
        });

        return createButtonTextfieldLabelPanel(inputTemplateNameField, buttonPrintTemplate, "Dateiname von Druckvorlage");
    }

    private JPanel createButtonTextfieldLabelPanel(JTextField textField, JButton button, String labelText) {
        JPanel buttonTextFieldPanel = new JPanel();
        buttonTextFieldPanel.setLayout(new BoxLayout(buttonTextFieldPanel, BoxLayout.X_AXIS));

        buttonTextFieldPanel.add(leftJustify(textField));
        buttonTextFieldPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonTextFieldPanel.add(button);

        JPanel componentPanel = new JPanel();
        componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));

        componentPanel.add(leftJustify(new CustomLabel(labelText)));
        componentPanel.add(buttonTextFieldPanel);
        return componentPanel;
    }

    private Component leftJustify(Component component) {
        Box b = Box.createHorizontalBox();
        b.add(component);
        b.add(Box.createHorizontalGlue());
        // (Note that you could throw a lot more components
        // and struts and glue in here.)
        return b;
    }

    private File selectFile(String filetype) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home").concat("/Downloads/")));
        //start in detail view and not in list view
        Action details = fileChooser.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(filetype, filetype);
        fileChooser.addChoosableFileFilter(filter);


        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            log.info("File selected: {}", file.getName());

            return file;
        }

        return null;
    }
}
