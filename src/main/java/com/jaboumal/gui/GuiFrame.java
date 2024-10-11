package com.jaboumal.gui;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;

import javax.swing.*;
import java.awt.*;

public class GuiFrame extends JFrame {

    private static MainPanel mainPanel;

    public GuiFrame() throws HeadlessException {
        GuiFrame.mainPanel = new MainPanel();
        mainPanel.setName("mainPanel");
    }

    public void createFrame() {
        mainPanel.createMainPanel();


        setContentPane(mainPanel);
        setTitle("Berchtoldschiessen");
        setSize(800, 500);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setMinimumSize(getSize());
        pack();
        getContentPane().requestFocusInWindow();

        setVisible(true);
    }

    public static void addCompetitorDataToFields(CompetitorDTO competitorDTO) {
        mainPanel.addCompetitorDataToFields(competitorDTO);
    }

    public static void createAndPrintStandblatt() {
        CompetitorDTO competitorDTO = mainPanel.getCompetitorData();
        CompetitorController.createStandblatt(competitorDTO);
    }
}
