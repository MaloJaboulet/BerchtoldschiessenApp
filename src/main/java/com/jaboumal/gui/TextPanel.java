package com.jaboumal.gui;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextPanel extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(TextPanel.class);
    private JLabel lastnameLabel;
    private JLabel lastnameField;
    private JLabel firstNameLabel;
    private JLabel firstNameField;
    private JLabel shooterNumberLabel;
    private JLabel shooterNumberField;
    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;
    private CompetitorDTO competitorDTO;

    public TextPanel() {
        super(new GridLayoutManager(4, 6, new Insets(0, 50, 0, 0), -1, -1));
        firstNameLabel = new JLabel("Vorname");
        firstNameField = new JLabel();

        lastnameLabel = new JLabel("Nachname");
        lastnameField = new JLabel();

        shooterNumberLabel = new JLabel("Schützennummer");
        shooterNumberField = new JLabel();

        searchLabel = new JLabel("Suche");
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
        searchField.setDragEnabled(false);
        searchField.setFocusTraversalPolicyProvider(false);
        searchField.setFocusable(true);
        searchField.setRequestFocusEnabled(true);
        searchField.setText("Vorname, Nachname, Schützennummer");

        searchField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("Enter pressed on Textfield");
                searchButton.grabFocus();
                searchCompetitor();
            }
        });
    }

    private void createSearchButton() {
        searchButton = new JButton();
        searchButton.setSelected(true);
        searchButton.setRequestFocusEnabled(true);
        searchButton.setText("Search");
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
        searchField.setText("Vorname, Nachname, Schützennummer");
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
