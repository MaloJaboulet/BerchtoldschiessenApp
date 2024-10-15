package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextPanel extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(TextPanel.class);
    private final JLabel lastnameLabel;
    private final JLabel lastnameField;
    private final JLabel firstNameLabel;
    private final JLabel firstNameField;
    private final JLabel shooterNumberLabel;
    private final JLabel shooterNumberField;
    private final JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;
    private CompetitorDTO competitorDTO;

    public TextPanel() {
        super(new GridLayoutManager(4, 6, new Insets(0, 50, 0, 0), -1, -1));
        firstNameLabel = new JLabel("Vorname");
        firstNameLabel.setFont(FontUtil.getFont("Segoe UI", Font.BOLD, 14, firstNameLabel.getFont()));
        firstNameField = new JLabel();
        firstNameField.setFont(FontUtil.getFont("Segoe UI", Font.PLAIN, 14, firstNameField.getFont()));

        lastnameLabel = new JLabel("Nachname");
        lastnameLabel.setFont(FontUtil.getFont("Segoe UI", Font.BOLD, 14, lastnameLabel.getFont()));
        lastnameField = new JLabel();
        lastnameField.setFont(FontUtil.getFont("Segoe UI", Font.PLAIN, 14, lastnameField.getFont()));

        shooterNumberLabel = new JLabel("Schützennummer");
        shooterNumberLabel.setFont(FontUtil.getFont("Segoe UI", Font.BOLD, 14, shooterNumberLabel.getFont()));
        shooterNumberField = new JLabel();
        shooterNumberField.setFont(FontUtil.getFont("Segoe UI", Font.PLAIN, 14, shooterNumberField.getFont()));

        searchLabel = new JLabel("Suche");
        searchLabel.setFont(FontUtil.getFont("Segoe UI", Font.BOLD, 14, searchLabel.getFont()));
        createSearchTextField();
        createSearchButton();

        addComponentsToPanel();
    }

    public void addDateToFields(CompetitorDTO competitorDTO) {
        if (competitorDTO == null) {
            clearDataOfFields();
        }else {
            firstNameField.setText(competitorDTO.getFirstName());
            lastnameField.setText(competitorDTO.getLastName());
            shooterNumberField.setText(String.valueOf(competitorDTO.getLizenzNummer()));
            this.competitorDTO = competitorDTO;
            PrintPanel.makePrintButtonEnabled();
        }
    }

    public CompetitorDTO getCompetitorData() {
        return competitorDTO;
    }

    private void createSearchTextField() {
        searchField = new TextFieldWithPlaceholder();
        searchField.setFont(FontUtil.getFont("Segoe UI", Font.PLAIN, 14, searchField.getFont()));
        searchField.addActionListener(_ -> {
            log.debug("Enter pressed on Textfield");
            searchButton.grabFocus();
            searchCompetitor();
        });
    }

    private void createSearchButton() {
        searchButton = new JButton();
        searchButton.setSelected(true);
        searchButton.setRequestFocusEnabled(true);
        searchButton.setText("Search");
        searchButton.setFont(FontUtil.getFont("Segoe UI", Font.BOLD, 14, searchButton.getFont()));
        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("Search button pressed");
                searchCompetitor();
            }
        });
    }

    public void clearDataOfFields() {
        firstNameField.setText("");
        lastnameField.setText("");
        shooterNumberField.setText("");
        this.competitorDTO = null;
        PrintPanel.makePrintButtonDisabled();
    }

    private void addComponentsToPanel() {
        add(firstNameLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        add(firstNameField, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        add(lastnameLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        add(lastnameField, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        add(shooterNumberLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        add(shooterNumberField, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        add(searchLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        add(searchField, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        final Spacer spacer3 = new Spacer();
        add(spacer3, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        add(searchButton, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
    }

    private void searchCompetitor(){
        CompetitorDTO competitorDTO = CompetitorController.searchCompetitorWithSearchText(searchField.getText());
        CompetitorController.addCompetitorDataToFieldsAndShowMessage(competitorDTO);
    }
}
