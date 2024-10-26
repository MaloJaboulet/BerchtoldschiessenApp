package com.jaboumal.util;

import com.jaboumal.constants.FilePaths;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {
    private static final Properties properties = new Properties();

    public static void loadConfigFile() {
        // Load the application's configuration file
        String fileName = "src/test/resources/config/config-test.properties";

        loadSystemProperties(fileName);


        for (String name : properties.stringPropertyNames()) {
            String value = properties.getProperty(name);
            System.setProperty(name, value);
        }

        FilePaths.loadPaths();
    }

    private static void loadSystemProperties(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
        } catch (IOException ex) {
            throw new RuntimeException("Could not load configuration file: " + fileName, ex);
        }
    }
}
