package com.jaboumal.gui;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;

import javax.swing.*;
import java.awt.*;


/**
 * This class is responsible for the main frame of the application.
 * It contains the main panel.
 * It also contains the method to add the competitor data to the fields and to create and print the standblatt.
 *
 * @author Malo Jaboulet
 */
public class MainFrame extends JFrame {

    private static MainPanel mainPanel;

    /**
     * Constructor of the class.
     */
    public MainFrame() throws HeadlessException {
        MainFrame.mainPanel = new MainPanel();
        mainPanel.setName("mainPanel");
        mainPanel.setFont(new Font("Serif", Font.BOLD, 28));
    }

    /**
     * Method to create the frame.
     */
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

    /**
     * Method to add the competitor data to the fields.
     *
     * @param competitorDTO the competitor data
     */
    public static void addCompetitorDataToFields(CompetitorDTO competitorDTO) {
        mainPanel.addCompetitorDataToFields(competitorDTO);
    }

    /**
     * Method to create and print the standblatt.
     */
    public static void createAndPrintStandblatt() {
        CompetitorDTO competitorDTO = mainPanel.getCompetitorData();
        CompetitorController.createStandblattAndPrint(competitorDTO);
    }
}
