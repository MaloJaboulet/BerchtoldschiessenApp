package com.jaboumal.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Root object for the XML response.
 *
 * @author Malo Jaboulet
 */
@XmlRootElement(name = "root")
public class RootDTO {
    @XmlElement(name = "berchtoldschiessen")
    private BerchtoldschiessenDTO berchtoldschiessenDTO;

    /**
     * Default constructor.
     */
    public RootDTO() {
    }

    /**
     * Constructor with the BerchtoldschiessenDTO.
     *
     * @param berchtoldschiessenDTO the BerchtoldschiessenDTO
     */
    public RootDTO(BerchtoldschiessenDTO berchtoldschiessenDTO) {
        this.berchtoldschiessenDTO = berchtoldschiessenDTO;
    }
}
