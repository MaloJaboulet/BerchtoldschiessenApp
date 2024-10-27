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

/**
 * The ConfigService class provides methods to load and manage configuration properties
 * for the application. It handles loading configuration files, setting system properties,
 * and creating necessary directories.
 *
 * @author Malo Jaboulet
 */
public class ConfigService {
    private static final Logger log = LoggerFactory.getLogger(ConfigService.class);
    private static final Properties properties = new Properties();
    private static final String baseDirectory = "C:/BerchtoldschiessenApp/";
    private static String fileName = baseDirectory.concat("config/config.properties");

    /**
     * Loads the application's configuration file and sets various system properties.
     * This method performs the following steps:
     * - Determines the appropriate configuration file based on the environment setting.
     * - Creates necessary base directories.
     * - Attempts to load system properties from the configuration file.
     * - Copies the default configuration file if the specified file does not exist.
     * - Logs the configuration details for debugging purposes.
     *
     * @throws RuntimeException If an I/O error occurs when creating directories or copying the configuration file.
     */
    public static void loadConfigFile() {
        if (System.getProperty("app.env") != null && System.getProperty("app.env").toLowerCase().contains("local")) {
            fileName = "src/main/resources/config/config_local.properties";

        }

        createBaseDirectories();

        File file = new File(fileName);
        if (file.exists()) {
            loadSystemProperties(fileName);
        } else {
            log.warn("config.properties in BerchtoldschiessenApp not found. Create a config.properties file.");
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

    /**
     * Retrieves the value of a property from the configuration file.
     *
     * @param propertyName the name of the property to retrieve
     * @return the value of the property
     */
    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }


    /**
     * Creates the base directories for the application.
     *
     * @throws RuntimeException if an I/O error occurs when creating the directories
     */
    private static void createBaseDirectories() {
        try {
            Files.createDirectories(Paths.get(baseDirectory));
            Files.createDirectories(Paths.get(baseDirectory + "config"));
            Files.createDirectories(Paths.get(baseDirectory + "input"));
            Files.createDirectories(Paths.get(baseDirectory + "output"));
        } catch (IOException e) {
            log.error("Could not create directories.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Copies the default configuration file to the specified path.
     *
     * @param filePath the path of the configuration file to create
     */
    private static void copyConfigFile(String filePath) {
        try (InputStream in = BerchtoldApp.class.getResourceAsStream("/config/config.properties")) {
            File targetFile = new File(filePath);
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads system properties from the specified file.
     *
     * @param fileName the name of the file to load
     */
    private static void loadSystemProperties(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
