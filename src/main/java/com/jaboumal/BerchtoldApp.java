package com.jaboumal;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.gui.MainGui;
import com.jaboumal.services.ConfigService;
import com.jaboumal.util.SerialPortReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BerchtoldApp {
    private static final Logger log = LoggerFactory.getLogger(BerchtoldApp.class);

    public static void main(String[] args) {
        printWelcomeMessage();

        ConfigService.loadConfigFile();

        CompetitorController competitorController = new CompetitorController();
        competitorController.loadCompetitorsFromFile();

        MainGui gui = new MainGui();
        gui.createGui();

        SerialPortReader serialPortReader = new SerialPortReader();
        serialPortReader.createSerialPortReader();
    }



    private static void printWelcomeMessage() {
        log.info("********************************************");
        log.info("***** Welcome to BerchtoldschiessenApp *****");
        log.info("********************************************");
    }
}
