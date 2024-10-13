package com.jaboumal.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class RootDTO {
    @XmlElement(name = "berchtoldschiessen")
    private BerchtoldschiessenDTO berchtoldschiessenDTO;


    public RootDTO() {
    }

    public RootDTO(BerchtoldschiessenDTO berchtoldschiessenDTO) {
        this.berchtoldschiessenDTO = berchtoldschiessenDTO;
    }
}
