package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.customComponents.CustomButton;
import com.jaboumal.gui.customComponents.CustomLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TextPanel class
 * This class is a JPanel that contains the text fields for the competitor data.
 *
 * @author Malo Jaboulet
 */
public class TextPanel extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(TextPanel.class);
    private final CustomLabel lastnameLabel;
    private final CustomLabel lastnameField;
    private final CustomLabel firstNameLabel;
    private final CustomLabel firstNameField;
    private final CustomLabel shooterNumberLabel;
    private final CustomLabel shooterNumberField;
    private final CustomLabel searchLabel;
    private JTextField searchField;
    private CustomButton searchButton;
    private CompetitorDTO competitorDTO;

    /**
     * Constructor
     * This constructor creates the TextPanel and initializes the components.
     */
    public TextPanel() {
        super(new GridLayoutManager(4, 6, new Insets(0, 50, 0, 0), -1, -1));
        firstNameLabel = new CustomLabel("Vorname");
        firstNameField = new CustomLabel();
        firstNameField.setFont(FontUtil.getFont("Segoe UI", Font.PLAIN, 14, firstNameField.getFont()));

        lastnameLabel = new CustomLabel("Nachname");
        lastnameField = new CustomLabel();
        lastnameField.setFont(FontUtil.getFont("Segoe UI", Font.PLAIN, 14, lastnameField.getFont()));

        shooterNumberLabel = new CustomLabel("Schützennummer");
        shooterNumberField = new CustomLabel();
        shooterNumberField.setFont(FontUtil.getFont("Segoe UI", Font.PLAIN, 14, shooterNumberField.getFont()));

        searchLabel = new CustomLabel("Suche");
        createSearchTextField();
        createSearchButton();

        addComponentsToPanel();
    }

    /**
     * This method adds the competitor data to the text fields.
     *
     * @param competitorDTO The competitor data to add to the text fields.
     */
    public void addDateToFields(CompetitorDTO competitorDTO) {
        if (competitorDTO == null) {
            clearDataOfFields();
        } else {
            firstNameField.setText(competitorDTO.getFirstName());
            lastnameField.setText(competitorDTO.getLastName());
            shooterNumberField.setText(String.valueOf(competitorDTO.getLizenzNummer()));
            this.competitorDTO = competitorDTO;
            PrintPanel.makePrintButtonEnabled();
        }
    }

    /**
     * This method returns the competitor data from the text fields.
     *
     * @return The competitor data from the text fields.
     */
    public CompetitorDTO getCompetitorData() {
        return competitorDTO;
    }

    /**
     * This method creates the search text field.
     */
    private void createSearchTextField() {
        searchField = new TextFieldWithPlaceholder();
        searchField.addActionListener(_ -> {
            log.debug("Enter pressed on Textfield");
            searchButton.grabFocus();
            searchCompetitor();
        });
    }

    /**
     * This method creates the search button.
     */
    private void createSearchButton() {
        searchButton = new CustomButton();
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

    /**
     * This method clears the data of the text fields.
     */
    public void clearDataOfFields() {
        firstNameField.setText("");
        lastnameField.setText("");
        shooterNumberField.setText("");
        this.competitorDTO = null;
        PrintPanel.makePrintButtonDisabled();
    }

    /**
     * This method adds the components to the panel.
     */
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

    /**
     * This method searches for a competitor with the search text.
     */
    private void searchCompetitor() {
        CompetitorDTO competitorDTO = CompetitorController.searchCompetitorWithSearchText(searchField.getText());
        CompetitorController competitorController = new CompetitorController();
        competitorController.addCompetitorDataToFieldsAndShowMessage(competitorDTO);
    }
}
