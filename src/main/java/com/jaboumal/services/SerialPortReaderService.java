package com.jaboumal.services;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.jaboumal.constants.EventMessages;
import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.EventMessagePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Service class for reading the serial port
 *
 * @author Malo Jaboulet
 * @since 26.10.2024
 */
public class SerialPortReaderService {
    private final static Logger log = LoggerFactory.getLogger(SerialPortReaderService.class);
    static byte[] barcodeBuffer = new byte[9];

    private final CompetitorController competitorController;

    public SerialPortReaderService(CompetitorController competitorController) {
        this.competitorController = competitorController;
    }

    /**
     * Create a serial port reader
     */
    public void createSerialPortReader() {
        log.debug(Arrays.toString(SerialPort.getCommPorts()));

        try {
            SerialPort comPort = null;

            for (SerialPort serialPort : SerialPort.getCommPorts()) {
                if (serialPort.getSystemPortName().equals("COM6") || serialPort.getManufacturer().contains("Honeywell")) {
                    comPort = serialPort;
                    break;
                }
            }

            if (comPort == null) {
                log.error(EventMessages.SCANNER_NOT_CONNECTED);
                EventMessagePanel.addErrorMessage(EventMessages.SCANNER_NOT_CONNECTED);
            } else {
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
                            CompetitorDTO competitorDTO = competitorController.searchCompetitorWithLizenzNummer(Integer.parseInt(formattedBarcodeString));
                            competitorController.addCompetitorDataToFieldsAndShowMessage(competitorDTO);
                        }
                    }
                });
            }
        } catch (SerialPortInvalidPortException exception) {
            log.error(exception.getMessage());
            EventMessagePanel.addErrorMessage(EventMessages.SCANNER_NOT_CONNECTED);
        }
    }

    /**
     * Format the barcode
     *
     * @param barcode the barcode to format
     * @return the formatted barcode
     */
    public String formatBarcode(String barcode) {
        String formattedBarcode = barcode;
        formattedBarcode = formattedBarcode.replaceAll("[A-Za-z]", "");
        formattedBarcode = formattedBarcode.replaceAll(
                "[^a-zA-Z0-9]", "");

        return formattedBarcode;
    }
}
