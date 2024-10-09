package com.jaboumal.gui;

import com.jaboumal.dto.CompetitorDTO;

public class MainGui {
    public static void main(String[] args) {

        GuiFrame gui = new GuiFrame();

        gui.createFrame();
    }

    public void createGui(){
        GuiFrame gui = new GuiFrame();

        gui.createFrame();
    }

    public void showCompetitorData(CompetitorDTO competitor){
        GuiFrame.addCompetitorDataToFields(competitor);
    }
}
