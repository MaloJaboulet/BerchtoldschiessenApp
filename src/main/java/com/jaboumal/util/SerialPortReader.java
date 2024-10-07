package com.jaboumal.util;

import com.jaboumal.controller.CompetitorController;
import com.jaboumal.dto.CompetitorDTO;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SerialPortReader {
    static byte[] barcodeBuffer = new byte[9];
    /*public static void main(String[] ap) throws IOException {

        System.out.println(Arrays.toString(SerialPort.getCommPorts()));

        /*SerialPort comPort = SerialPort.getCommPorts()[2];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPort.getInputStream();
        try {
            for (int j = 0; j < 1000; ++j)
                System.out.println((char) in.read());
           // in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        comPort.closePort();

        String keyboardMode = "416400";
        byte[] keyboardModeArray = keyboardMode.getBytes(StandardCharsets.UTF_8);

        SerialPort comPort = SerialPort.getCommPorts()[2];
        comPort.openPort();

        /*try {
            while (true) {
                while (comPort.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");

                if (readBuffer.length == 9) {
                    barcodeBuffer = Arrays.copyOfRange(readBuffer, 0, 9);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                byte[] newData = event.getReceivedData();
                System.out.println("Received data of size: " + newData.length);
                for (byte newDatum : newData) {
                    System.out.print((char) newDatum);
                }
                barcodeBuffer = Arrays.copyOfRange(newData, 0, 9);
                System.out.println("\n");
            }
        });

        String barcodeString = Arrays.toString(new String(barcodeBuffer, StandardCharsets.UTF_8).toCharArray());
        System.out.println(barcodeString);


        //comPort.closePort();


    }*/

    public void createSerialPortReader() {
        System.out.println(Arrays.toString(SerialPort.getCommPorts()));

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
                System.out.println("Received data of size: " + newData.length);
                for (byte newDatum : newData) {
                    System.out.print((char) newDatum);
                }

                if (newData.length < 8) {
                    System.out.println("\n");
                    System.out.println("Data too short");
                } else {
                    barcodeBuffer = Arrays.copyOfRange(newData, 0, 9);
                    String barcodeString = Arrays.toString(new String(barcodeBuffer, StandardCharsets.UTF_8).toCharArray());
                    String formattedBarcodeString = formatBarcode(barcodeString);

                    System.out.println("\n");
                    System.out.println("Formatted Barcode String: " + formattedBarcodeString);
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
