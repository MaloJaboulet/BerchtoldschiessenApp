package com.jaboumal;

import com.jaboumal.constants.FilePaths;
import com.jaboumal.controller.CompetitorController;
import com.jaboumal.gui.Gui;
import com.jaboumal.services.ConfigService;
import com.jaboumal.services.SerialPortReaderService;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;


/**
 * Main class of the BerchtoldApp
 *
 * @author Malo Jaboulet
 */
public class BerchtoldApp {
    private static final Logger log = LoggerFactory.getLogger(BerchtoldApp.class);

    public static void main(String[] args) {
        setLog4jConfig();
        printWelcomeMessage();

        ConfigService.loadConfigFile();
        FilePaths.loadPaths();

        Gui gui = new Gui();
        gui.createGui();

        CompetitorController competitorController = new CompetitorController();
        competitorController.loadCompetitorsFromFile();


        SerialPortReaderService serialPortReaderService = new SerialPortReaderService(competitorController);
        serialPortReaderService.createSerialPortReader();
    }


    /**
     * Set the log4j2 configuration file depending on the app.env system property
     */
    private static void setLog4jConfig() {
        if (System.getProperty("app.env") != null && System.getProperty("app.env").toLowerCase().contains("local")) {
            URI configSourceUri = new File("src/main/resources/log4j2-local.xml").toURI();
            Configurator.reconfigure(configSourceUri);
        }
    }

    private static void printWelcomeMessage() {
        log.info("********************************************");
        log.info("***** Welcome to BerchtoldschiessenApp *****");
        log.info("********************************************");
    }
}
