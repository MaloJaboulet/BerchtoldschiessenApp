package com.jaboumal.util;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SerialPortReader {
    private final static Logger log = LoggerFactory.getLogger(SerialPortReader.class);
    static byte[] barcodeBuffer = new byte[9];

    public void createSerialPortReader() {
        log.debug(Arrays.toString(SerialPort.getCommPorts()));

        SerialPort comPort = SerialPort.getCommPorts()[2];
        comPort.openPort();

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                byte[] newData = event.getReceivedData();
                log.debug("Received data of size: {}", newData.length);
                for (byte newDatum : newData) {
                    log.debug(String.valueOf((char) newDatum));
                }

                if (newData.length < 8) {
                    log.debug("Data too short");
                } else {
                    barcodeBuffer = Arrays.copyOfRange(newData, 0, 9);
                    String barcodeString = Arrays.toString(new String(barcodeBuffer, StandardCharsets.UTF_8).toCharArray());
                    String formattedBarcodeString = formatBarcode(barcodeString);

                    log.debug("Formatted Barcode String: {}", formattedBarcodeString);
                    CompetitorDTO competitorDTO = CompetitorController.searchCompetitorWithLizenzNummer(Integer.parseInt(formattedBarcodeString));
                    CompetitorController.addCompetitorDataToFieldsAndShowMessage(competitorDTO);
                }
            }
        });
    }

    public String formatBarcode(String barcode) {
        String formattedBarcode = barcode;
        formattedBarcode = formattedBarcode.replaceAll("[A-Za-z]", "");
        formattedBarcode = formattedBarcode.replaceAll(
                "[^a-zA-Z0-9]", "");

        return formattedBarcode;
    }
}
