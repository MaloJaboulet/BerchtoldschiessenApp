package com.jaboumal.gui;

import com.jaboumal.dto.CompetitorDTO;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private final JPanel titlePanel;
    private final TextPanel textPanel;
    private final EventMessagePanel eventMessagePanel;
    private final PrintPanel printPanel;

    public MainPanel() {
        this.titlePanel = new TitlePanel();
        titlePanel.setName("titlePanel");
        this.textPanel = new TextPanel();
        textPanel.setName("textPanel");
        this.eventMessagePanel = new EventMessagePanel();
        eventMessagePanel.setName("errorPanel");
        this.printPanel = new PrintPanel();
        printPanel.setName("printPanel");
    }

    public void addCompetitorDataToFields(CompetitorDTO competitorDTO) {
        textPanel.addDateToFields(competitorDTO);
    }

    public void createMainPanel() {
        setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));

        add(titlePanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        add(eventMessagePanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        add(textPanel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        add(printPanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));


        add(new Spacer(), new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    public CompetitorDTO getCompetitorData(){
        return textPanel.getCompetitorData();
    }
}
