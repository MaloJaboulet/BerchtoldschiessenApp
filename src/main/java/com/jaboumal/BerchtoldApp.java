package com.jaboumal;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.gui.MainGui;
import com.jaboumal.util.SerialPortReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class BerchtoldApp {

    public static final String baseDirectory = "C:/BerchtoldschiessenApp/";

    public static void main(String[] args) {
               /* Util.BarcodeCreater barcodeCreater = new Util.BarcodeCreater();
        String barcode = barcodeCreater.createBarcode(925177);

        Util.XMLCreate xmlCreate = new Util.XMLCreate();
        xmlCreate.createXml("Hans Peter", "01.01.2000", barcode);

        DataFileLoader loader = new DataFileLoader();
        loader.loadDataInFile();*/

       /* Gui com.jaboumal.gui = new Gui();
        com.jaboumal.gui.createFrame();

        com.jaboumal.gui.setLastnameField("Peter", com.jaboumal.gui);
        com.jaboumal.gui.setNameField("Peter");*/


        loadConfigFile();

        CompetitorController competitorController = new CompetitorController();
        competitorController.loadCompetitorsFromFile();

        MainGui gui = new MainGui();
        gui.createGui();

        SerialPortReader serialPortReader = new SerialPortReader();
        serialPortReader.createSerialPortReader();
    }

    public static void loadConfigFile() {
        Properties prop = new Properties();
        String fileName = "";
        if (System.getProperty("app.env") != null && System.getProperty("app.env").toLowerCase().contains("local")) {
            fileName = "src/main/resources/config/config_local.yaml";
        } else {
            fileName = baseDirectory.concat("config/config.yaml");
        }

        try {
            createBaseDirectories();
        } catch (IOException e) {
            System.out.println("Could not create directories.");
            throw new RuntimeException(e);
        }

        File file = new File(fileName);
        if (file.exists()) {
            loadSystemProperties(fileName, prop);
        } else {
            System.out.println("config.yaml in BerchtoldschiessenApp not found. Create a config.yaml file.");
            copyConfigFile(fileName);
            loadSystemProperties(fileName, prop);
        }

        for (String name : prop.stringPropertyNames()) {
            String value = prop.getProperty(name);
            System.setProperty(name, value);
        }
        System.out.println(prop.getProperty("INPUT_DOCX"));
        System.out.println(prop.getProperty("OUTPUT_DOCX"));
    }

    private static void createBaseDirectories() throws IOException {
        Files.createDirectories(Paths.get(baseDirectory));
        Files.createDirectories(Paths.get(baseDirectory + "config"));
        Files.createDirectories(Paths.get(baseDirectory + "input"));
        Files.createDirectories(Paths.get(baseDirectory + "output"));
    }

    private static void copyConfigFile(String fileName) {
        try (InputStream in = BerchtoldApp.class.getResourceAsStream("/config/config.yaml")) {
            File targetFile = new File(fileName);
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadSystemProperties(String fileName, Properties prop) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
