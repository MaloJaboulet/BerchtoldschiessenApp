package com.jaboumal.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class RootDTO {


    public RootDTO() {
    }

    //Class only needed to read data from XML file
    public RootDTO(BerchtoldschiessenDTO berchtoldschiessenDTO) {
    }
}
