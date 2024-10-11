package com.jaboumal.gui;

import com.jaboumal.dto.CompetitorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

public class CompetitorSelectionDialog {
    private static final Logger log = LoggerFactory.getLogger(CompetitorSelectionDialog.class);

    public CompetitorDTO showDialog(List<CompetitorDTO> competitorDTOList) {

        Object selected = JOptionPane.showInputDialog(
                null,
                "Welchen Teilnehmer meinen sie?",
                "Teilnehmer",
                JOptionPane.QUESTION_MESSAGE,
                null,
                competitorDTOList.toArray(),
                "0");

        //null if the user cancels.
        if (selected != null) {
            return (CompetitorDTO) selected;
        } else {
            log.info("User cancelled competitor selection");
            return null;
        }
    }
}
