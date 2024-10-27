package com.jaboumal.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jaboumal.dto.CompetitorDTO;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for the main panel in the main frame.
 * It contains the title panel, the text panel, the event message panel, the print panel and the settings panel.
 * It also contains the method to add the competitor data to the fields.
 *
 * @author Malo Jaboulet
 */
public class MainPanel extends JPanel {

    private final JPanel titlePanel;
    private final TextPanel textPanel;
    private final EventMessagePanel eventMessagePanel;
    private final PrintPanel printPanel;
    private final SettingsPanel settingsPanel;

    /**
     * Constructor of the class.
     */
    public MainPanel() {
        this.titlePanel = new TitlePanel();
        titlePanel.setName("titlePanel");
        this.textPanel = new TextPanel();
        textPanel.setName("textPanel");
        this.eventMessagePanel = new EventMessagePanel();
        eventMessagePanel.setName("errorPanel");
        this.printPanel = new PrintPanel();
        printPanel.setName("printPanel");
        settingsPanel = new SettingsPanel();
        settingsPanel.createSettingsPanel();
    }

    /**
     * Method to add the competitor data to the fields.
     *
     * @param competitorDTO the competitor data
     */
    public void addCompetitorDataToFields(CompetitorDTO competitorDTO) {
        textPanel.addDateToFields(competitorDTO);
    }

    /**
     * Method to create the main panel.
     * It sets the layout and adds the title panel, the text panel, the event message panel, the print panel and the settings panel.
     */
    public void createMainPanel() {
        setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));

        add(titlePanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        add(eventMessagePanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        add(textPanel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        add(printPanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));


        add(new Spacer(), new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        add(settingsPanel, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

    }

    /**
     * Method to get the competitor data.
     *
     * @return the competitor data
     */
    public CompetitorDTO getCompetitorData(){
        return textPanel.getCompetitorData();
    }
}
