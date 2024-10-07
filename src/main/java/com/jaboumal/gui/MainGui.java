package com.jaboumal.gui;

import com.jaboumal.dto.CompetitorDTO;

public class MainGui {
    private GuiFrame gui;
    public static void main(String[] args) {

        GuiFrame gui = new GuiFrame();

        gui.createFrame();
    }

    public void createGui(){
        GuiFrame gui = new GuiFrame();

        gui.createFrame();
    }

    public void showCompetitorData(CompetitorDTO competitor){
        gui.addCompetitorDataToFields(competitor);
    }
}
