package com.jaboumal.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store the file paths of the input and output files.
 * The paths are stored in a map and can be accessed by the key.
 *
 * @author Malo Jaboulet
 */
public class FilePaths {
    public static final String INPUT_DOCX = "INPUT_DOCX";
    public static final String INPUT_DOCX_PATH = "INPUT_DOCX_PATH";

    public static final String INPUT_XML = "INPUT_XML";
    public static final String INPUT_XML_PATH = "INPUT_XML_PATH";

    public static final String OUTPUT_DOCX = "OUTPUT_DOCX";
    public static final String OUTPUT_DOCX_PATH = "OUTPUT_DOCX_PATH";

    public static final String INPUT_COMPETITORS = "INPUT_COMPETITORS";
    public static final String INPUT_COMPETITORS_PATH = "INPUT_COMPETITORS_PATH";

    public static final String OUTPUT_PRINT_RECORD = "OUTPUT_PRINT_RECORD";
    public static final String OUTPUT_PRINT_RECORD_PATH = "OUTPUT_PRINT_RECORD_PATH";

    public static final String BASE_DIRECTORY = "BASE_DIRECTORY";
    public static final String INPUT_FOLDER = "INPUT_FOLDER";
    public static final String OUTPUT_FOLDER = "OUTPUT_FOLDER";


    private static Map<String, String> paths;

    /**
     * Load the paths of the input and output files.
     */
    public static void loadPaths() {
        paths = new HashMap<>();

        String baseDir = System.getProperty(BASE_DIRECTORY);
        String inputDir = System.getProperty(INPUT_FOLDER);
        String outputDir = System.getProperty(OUTPUT_FOLDER);
        paths.put(INPUT_DOCX_PATH, baseDir.concat(System.getProperty(INPUT_DOCX)));
        paths.put(INPUT_XML_PATH, baseDir.concat(outputDir.concat(System.getProperty(INPUT_XML))));
        paths.put(OUTPUT_DOCX_PATH, baseDir.concat(outputDir.concat(System.getProperty(OUTPUT_DOCX))));
        paths.put(INPUT_COMPETITORS_PATH, baseDir.concat(inputDir.concat(System.getProperty(INPUT_COMPETITORS))));
        paths.put(OUTPUT_PRINT_RECORD_PATH, baseDir.concat(outputDir.concat(System.getProperty(OUTPUT_PRINT_RECORD))));
    }

    /**
     * Get the path of the file with the given filename.
     *
     * @param filename the name of the file
     * @return the path of the file
     */
    public static String getPath(String filename) {
        return paths.get(filename);
    }

    /**
     * ONLY FOR TESTING
     * Get the paths map.
     *
     * @return the paths map
     */
    Map<String, String> getPaths() {
        return FilePaths.paths;
    }
}
