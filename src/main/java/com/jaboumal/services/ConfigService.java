package com.jaboumal.services;

import com.jaboumal.BerchtoldApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import static com.jaboumal.constants.FilePaths.*;

public class ConfigService {
    private static final Logger log = LoggerFactory.getLogger(ConfigService.class);
    public static final String baseDirectory = "C:/BerchtoldschiessenApp/";
    private static final Properties properties = new Properties();
    private static String fileName = baseDirectory.concat("config/config.properties");

    public static void loadConfigFile() {
        if (System.getProperty("app.env") != null && System.getProperty("app.env").toLowerCase().contains("local")) {
            fileName = "src/main/resources/config/config_local.properties";

        }

        try {
            createBaseDirectories();
        } catch (IOException e) {
            log.error("Could not create directories.", e);
            throw new RuntimeException(e);
        }

        File file = new File(fileName);
        if (file.exists()) {
            loadSystemProperties(fileName);
        } else {
            log.warn("config.yaml in BerchtoldschiessenApp not found. Create a config.yaml file.");
            copyConfigFile(fileName);
            loadSystemProperties(fileName);
        }

        for (String name : properties.stringPropertyNames()) {
            String value = properties.getProperty(name);
            System.setProperty(name, value);
        }

        log.debug("BASE_DIRECTORY: {}", properties.getProperty(BASE_DIRECTORY));
        log.debug("INPUT_DOCX: {}", properties.getProperty(INPUT_DOCX));
        log.debug("OUTPUT_DOCX: {}", properties.getProperty(OUTPUT_DOCX));
        log.debug("INPUT_XML: {}", properties.getProperty(INPUT_XML));
        log.debug("INPUT_COMPETITORS: {}", properties.getProperty(INPUT_COMPETITORS));
    }

    public static void replaceValue(String propertyName, String replacementValue) {
        properties.setProperty(propertyName, replacementValue);

        log.info("Property {} with value {} updated.", propertyName, replacementValue);
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            properties.store(fos, "Update properties");
            fos.close();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
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

    private static void loadSystemProperties(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
