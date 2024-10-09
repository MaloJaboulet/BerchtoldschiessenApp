package com.jaboumal.services;

import com.jaboumal.BerchtoldApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import static com.jaboumal.constants.FilePaths.*;

public class ConfigService {
    private static final Logger log = LoggerFactory.getLogger(ConfigService.class);
    public static final String baseDirectory = "C:/BerchtoldschiessenApp/";

    public static void loadConfigFile() {
        Properties prop = new Properties();
        String fileName;

        if (System.getProperty("app.env") != null && System.getProperty("app.env").toLowerCase().contains("local")) {
            fileName = "src/main/resources/config/config_local.yaml";

        } else {
            fileName = baseDirectory.concat("config/config.yaml");
        }

        try {
            createBaseDirectories();
        } catch (IOException e) {
            log.error("Could not create directories.", e);
            throw new RuntimeException(e);
        }

        File file = new File(fileName);
        if (file.exists()) {
            loadSystemProperties(fileName, prop);
        } else {
            log.warn("config.yaml in BerchtoldschiessenApp not found. Create a config.yaml file.");
            copyConfigFile(fileName);
            loadSystemProperties(fileName, prop);
        }

        for (String name : prop.stringPropertyNames()) {
            String value = prop.getProperty(name);
            System.setProperty(name, value);
        }
        log.debug("BASE_DIRECTORY: {}", prop.getProperty("BASE_DIRECTORY"));
        log.debug("INPUT_DOCX: {}", prop.getProperty(INPUT_DOCX));
        log.debug("OUTPUT_DOCX: {}", prop.getProperty(OUTPUT_DOCX));
        log.debug("INPUT_XML: {}", prop.getProperty(INPUT_XML));
        log.debug("INPUT_COMPETITORS: {}", prop.getProperty(INPUT_COMPETITORS));
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
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void loadSystemProperties(String fileName, Properties prop) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
