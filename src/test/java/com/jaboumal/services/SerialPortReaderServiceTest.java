package com.jaboumal.services;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.jaboumal.constants.EventMessages;
import com.jaboumal.controller.CompetitorController;
import com.jaboumal.gui.EventMessagePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SerialPortReaderServiceTest {

    @Mock
    CompetitorController competitorController;


    @InjectMocks
    SerialPortReaderService serialPortReaderService;


    // @BeforeEach removed as per user guideline

    @Test
    public void testSerialPortReaderCreation() {
        //Arrange
        CompetitorController competitorController = Mockito.mock(CompetitorController.class);
        SerialPortReaderService serialPortReaderService = new SerialPortReaderService(competitorController);

        SerialPort[] serialPorts = new SerialPort[2];
        serialPorts[0] = Mockito.mock(SerialPort.class);
        serialPorts[1] = Mockito.mock(SerialPort.class);

        Mockito.when(serialPorts[0].getSystemPortName()).thenReturn("COM5");
        Mockito.when(serialPorts[0].getManufacturer()).thenReturn("Apple");
        Mockito.when(serialPorts[1].getSystemPortName()).thenReturn("COM6");

        // Mocking static method
        try (MockedStatic<SerialPort> mockedStatic = Mockito.mockStatic(SerialPort.class)) {
            mockedStatic.when(SerialPort::getCommPorts).thenReturn(serialPorts);

            Mockito.when(serialPorts[1].openPort()).thenReturn(true);
            //Act & Assert
            assertDoesNotThrow(() -> serialPortReaderService.createSerialPortReader());
        }
    }

    @Test
    public void testSerialPortReaderCreation_whenPortIsMissing() {
        //Arrange
        CompetitorController competitorController = Mockito.mock(CompetitorController.class);
        SerialPortReaderService serialPortReaderService = new SerialPortReaderService(competitorController);

        SerialPort[] serialPorts = new SerialPort[2];
        serialPorts[0] = Mockito.mock(SerialPort.class);
        serialPorts[1] = Mockito.mock(SerialPort.class);

        Mockito.when(serialPorts[0].getSystemPortName()).thenReturn("COM5");
        Mockito.when(serialPorts[0].getManufacturer()).thenReturn("Apple");
        Mockito.when(serialPorts[1].getSystemPortName()).thenReturn("COM7");
        Mockito.when(serialPorts[1].getManufacturer()).thenReturn("Apple");

        // Mocking static method
        try (MockedStatic<SerialPort> mockedStatic = Mockito.mockStatic(SerialPort.class)) {
            mockedStatic.when(SerialPort::getCommPorts).thenReturn(serialPorts);

            try (MockedStatic<EventMessagePanel> eventMessagePanelMock = Mockito.mockStatic(EventMessagePanel.class)) {

                //Act & Assert
                assertDoesNotThrow(() -> serialPortReaderService.createSerialPortReader());
                eventMessagePanelMock.verify(() -> EventMessagePanel.addErrorMessage(EventMessages.SCANNER_NOT_CONNECTED), times(1));
            }
        }
    }

    @Test
    public void testSerialPortReaderCreation_whenPortThrowsException() {
        //Arrange
        CompetitorController competitorController = Mockito.mock(CompetitorController.class);
        SerialPortReaderService serialPortReaderService = new SerialPortReaderService(competitorController);

        SerialPort[] serialPorts = new SerialPort[1];
        serialPorts[0] = Mockito.mock(SerialPort.class);

        Mockito.when(serialPorts[0].getSystemPortName()).thenThrow(SerialPortInvalidPortException.class);

        // Mocking static method
        try (MockedStatic<SerialPort> mockedStatic = Mockito.mockStatic(SerialPort.class)) {
            mockedStatic.when(SerialPort::getCommPorts).thenReturn(serialPorts);

           //Act & Assert
           assertThrows(RuntimeException.class, () -> serialPortReaderService.createSerialPortReader());
        }
    }
}