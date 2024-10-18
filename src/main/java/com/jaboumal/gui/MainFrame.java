package com.jaboumal.gui;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class MainFrame extends JFrame {

    private static MainPanel mainPanel;

    public MainFrame() throws HeadlessException {
        MainFrame.mainPanel = new MainPanel();
        mainPanel.setName("mainPanel");
        mainPanel.setFont(new Font("Serif", Font.BOLD, 28));
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

    public void setFont(){
        //Font labelFont = FontUtil.getFont("Segoe UI", Font.PLAIN, 14,null);
        Font labelFont = new Font("Serif", Font.BOLD, 28);
        System.out.println(UIManager.getFont("Label.font").toString());
        //UIManager.put("Label.font", new FontUIResource(labelFont));



        /*UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keysEnumeration = defaults.keys();
        ArrayList<Object> keysList = Collections.list(keysEnumeration);
        for (Object key : keysList)
        {
            System.out.println(key);
        }*/

       Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, new FontUIResource(labelFont));
        }
        System.out.println(UIManager.getFont("Label.font").toString());
    }

    public static void addCompetitorDataToFields(CompetitorDTO competitorDTO) {
        mainPanel.addCompetitorDataToFields(competitorDTO);
    }

    public static void createAndPrintStandblatt() {
        CompetitorDTO competitorDTO = mainPanel.getCompetitorData();
        CompetitorController.createStandblatt(competitorDTO);
    }
}
