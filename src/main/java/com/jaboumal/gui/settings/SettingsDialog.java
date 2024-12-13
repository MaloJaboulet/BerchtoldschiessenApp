package com.jaboumal.gui.settings;

import com.jaboumal.constants.FilePaths;
import com.jaboumal.controller.CompetitorController;
import com.jaboumal.gui.customComponents.CustomButton;
import com.jaboumal.gui.customComponents.CustomLabel;
import com.jaboumal.services.ConfigService;
import com.jaboumal.services.FileReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.jaboumal.constants.FilePaths.*;

/**
 * SettingsDialog class is responsible for creating the settings dialog
 * in the main window.
 * It allows the user to select the input template and competitor file.
 * The selected files are copied to the Berchtold folder.
 * The dialog is displayed when the user clicks on the "Settings" button.
 *
 * @author Malo Jaboulet
 */
public class SettingsDialog {
    private final static Logger log = LoggerFactory.getLogger(SettingsDialog.class);
    private final JTextField inputTemplateNameField = new JTextField();
    private final JTextField inputCompetitorFileNameField = new JTextField();
    private final CustomButton buttonPrintTemplate = new CustomButton();
    private final CustomButton buttonCompetitor = new CustomButton();

    /**
     * Displays the settings dialog.
     *
     * @param printTemplateFileName   name of the print template file
     * @param inputCompetitorFileName name of the input competitor file
     */
    public void showDialog(String printTemplateFileName, String inputCompetitorFileName) {
        final JComponent[] inputs = new JComponent[]{
                createInputTemplateNameComponents(printTemplateFileName),
                createCompetitorNameComponents(inputCompetitorFileName),
                createInfoPanel(),
        };

        UIManager.put("OptionPane.border", new EmptyBorder(20, 50, 20, 50));
        JOptionPane.showConfirmDialog(null, inputs, "Settings", JOptionPane.DEFAULT_OPTION);
    }

    /**
     * Creates the components for the input competitor file.
     *
     * @param inputCompetitorFileName name of the input competitor file
     * @return JPanel with the components
     */
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

                    String fileName = selectedFile.getName();
                    String destinationPath = FilePaths.getPath(BASE_DIRECTORY).concat(FilePaths.getPath(INPUT_FOLDER)).concat(fileName);
                    log.debug("Copying file: {} to {}", fileName, destinationPath);

                    //Copy selected file to Berchtold folder
                    FileReaderService.copyFile(selectedFile.getPath(), destinationPath);
                    FileReaderService.deleteFile(FilePaths.getPath(INPUT_COMPETITORS_PATH));
                    ConfigService.replaceProperty(INPUT_COMPETITORS, fileName);
                    CompetitorController.loadCompetitorsFromFile();
                }
            }
        });

        return createButtonTextfieldLabelPanel(inputCompetitorFileNameField, buttonCompetitor, "Dateiname von Teilnehmerliste");
    }

    /**
     * Creates the components for the input template file.
     *
     * @param printTemplateFileName name of the print template file
     * @return JPanel with the components
     */
    private JPanel createInputTemplateNameComponents(String printTemplateFileName) {
        inputTemplateNameField.setText(printTemplateFileName);
        inputTemplateNameField.setFocusable(false);

        buttonPrintTemplate.setIcon(UIManager.getIcon("FileView.fileIcon"));
        buttonPrintTemplate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile = selectFile("pdf");
                if (selectedFile != null) {
                    inputTemplateNameField.setText(selectedFile.getName());

                    String fileName = selectedFile.getName();
                    String destinationPath = FilePaths.getPath(BASE_DIRECTORY).concat(fileName);
                    log.debug("Copying file: {} to {}", fileName, destinationPath);

                    //Copy selected file to Berchtold folder
                    FileReaderService.copyFile(selectedFile.getPath(), destinationPath);
                    FileReaderService.deleteFile(FilePaths.getPath(INPUT_PDF_PATH));
                    ConfigService.replaceProperty(INPUT_PDF, fileName);
                }
            }
        });

        return createButtonTextfieldLabelPanel(inputTemplateNameField, buttonPrintTemplate, "Dateiname von Druckvorlage");
    }

    /**
     * Creates a panel with a text field, a button and a label.
     *
     * @param textField text field
     * @param button    button
     * @param labelText label text
     * @return JPanel with the components
     */
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

    /**
     * Creates a panel with an information button.
     *
     * @return JPanel with the information button
     */
    private JPanel createInfoPanel() {
        CustomButton buttonInfo = new CustomButton();
        buttonInfo.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        buttonInfo.setBorderPainted(false);
        buttonInfo.setContentAreaFilled(false);

        JTextPane textPane = createInfoTextPane();

        final JComponent[] inputs = new JComponent[]{
                textPane,
        };

        buttonInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, inputs, "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel infoButtonPanel = new JPanel();
        infoButtonPanel.setLayout(new BoxLayout(infoButtonPanel, BoxLayout.X_AXIS));
        infoButtonPanel.add(Box.createHorizontalGlue());
        infoButtonPanel.add(buttonInfo);


        return infoButtonPanel;
    }

    /**
     * Creates a text pane with information about the application.
     *
     * @return JTextPane with the information
     */
    private JTextPane createInfoTextPane() {
        String text = "<html>" +
                "Der gesamte Code für diese Applikation befindet sich unter diesem Link: <a href=\"https://github.com/MaloJaboulet/BerchtoldschiessenApp\">https://github.com/MaloJaboulet/BerchtoldschiessenApp</a><BR>" +
                "Dort befindet sich ebenfalls die Dokumentation für die App." +
                "</html>";

        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setBackground(null);
        textPane.setBorder(null);
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true); //allows to use the system font
        textPane.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    log.error("Error while opening the link: {}", ex.getMessage());
                }
            }
        });
        return textPane;
    }

    /**
     * Left justifies a component.
     *
     * @param component component to justify
     * @return left justified component
     */
    private Component leftJustify(Component component) {
        Box b = Box.createHorizontalBox();
        b.add(component);
        b.add(Box.createHorizontalGlue());
        // (Note that you could throw a lot more components
        // and struts and glue in here.)
        return b;
    }

    /**
     * Opens a file chooser dialog and returns the selected file.
     *
     * @param filetype file type
     * @return selected file
     */
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
