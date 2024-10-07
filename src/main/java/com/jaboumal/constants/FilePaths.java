package com.jaboumal.constants;

import java.util.HashMap;
import java.util.Map;

public class FilePaths {
    public static final String BARCODE_INPUT = "src/main/resources/output/barcode.png";
    private static final String INPUT_DOCX_LOCAL = "src/main/resources/Berchtoldschiessen_test2.docx";

    private static final String INPUT_XML_LOCAL = "src/main/resources/output/data_output.xml";

    private static final String OUTPUT_DOCX_LOCAL = "src/main/resources/output/Berchtoldschiessen_%s.docx";
    private static final String INPUT_COMPETITORS_LOCAL = "src/main/resources/input/competitors.csv";

    public static final String INPUT_DOCX = "INPUT_DOCX";

    public static final String INPUT_XML = "INPUT_XML";

    public static final String OUTPUT_DOCX = "OUTPUT_DOCX";
    public static final String INPUT_COMPETITORS = "INPUT_COMPETITORS";

    private static Map<String, String> paths;

    public FilePaths() {
        paths = new HashMap<>();
        if (System.getProperty("app.env").toLowerCase().contains("local")) {
            paths.put(INPUT_DOCX, INPUT_DOCX_LOCAL);
            paths.put(INPUT_XML, INPUT_XML_LOCAL);
            paths.put(OUTPUT_DOCX, OUTPUT_DOCX_LOCAL);
            paths.put(INPUT_COMPETITORS, INPUT_COMPETITORS_LOCAL);
        } else {
            String baseDir = System.getProperty("BASE_DIRECTORY");
            paths.put(INPUT_DOCX, baseDir.concat(System.getProperty("INPUT_DOCX")));
            paths.put(INPUT_XML, baseDir.concat(System.getProperty("INPUT_XML")));
            paths.put(OUTPUT_DOCX, baseDir.concat(System.getProperty("OUTPUT_DOCX")));
            paths.put(INPUT_COMPETITORS, baseDir.concat(System.getProperty("INPUT_COMPETITORS")));
        }
    }

    public String getPath(String filename) {
        return paths.get(filename);
    }
}
