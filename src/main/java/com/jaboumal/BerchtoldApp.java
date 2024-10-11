package com.jaboumal;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.gui.MainGui;
import com.jaboumal.services.ConfigService;
import com.jaboumal.services.SerialPortReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BerchtoldApp {
    private static final Logger log = LoggerFactory.getLogger(BerchtoldApp.class);

    public static void main(String[] args) {
        printWelcomeMessage();

        ConfigService.loadConfigFile();

        MainGui gui = new MainGui();
        gui.createGui();

        CompetitorController competitorController = new CompetitorController();
        competitorController.loadCompetitorsFromFile();



        SerialPortReaderService serialPortReaderService = new SerialPortReaderService();
        serialPortReaderService.createSerialPortReader();
    }



    private static void printWelcomeMessage() {
        log.info("********************************************");
        log.info("***** Welcome to BerchtoldschiessenApp *****");
        log.info("********************************************");
    }
}
