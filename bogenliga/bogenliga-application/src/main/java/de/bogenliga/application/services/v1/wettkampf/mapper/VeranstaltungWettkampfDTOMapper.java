package de.bogenliga.application. services.v1.wettkampf.mapper;

import de. bogenliga.application.business.wettkampf.api. types.VeranstaltungWettkampfDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de. bogenliga.application.services. v1.wettkampf. model.VeranstaltungWettkampfDTO;

import java.util.function.Function;

/**
 * Mapper zwischen VeranstaltungWettkampfDO und VeranstaltungWettkampfDTO
 */
public class VeranstaltungWettkampfDTOMapper implements DataTransferObjectMapper {

    /**
     * Konvertiert VeranstaltungWettkampfDO zu VeranstaltungWettkampfDTO
     */
    public static final Function<VeranstaltungWettkampfDO, VeranstaltungWettkampfDTO> toDTO =
            veranstaltungWettkampfDO -> {

                VeranstaltungWettkampfDTO dto = new VeranstaltungWettkampfDTO();

                // Wettkampf-Felder
                dto.setWettkampfId(veranstaltungWettkampfDO.getWettkampfId());
                dto.setWettkampfDatum(veranstaltungWettkampfDO.getWettkampfDatum());
                dto.setWettkampfTag(veranstaltungWettkampfDO.getWettkampfTag());
                dto.setWettkampfStrasse(veranstaltungWettkampfDO.getWettkampfStrasse());
                dto.setWettkampfPlz(veranstaltungWettkampfDO.getWettkampfPlz());
                dto.setWettkampfOrtsname(veranstaltungWettkampfDO. getWettkampfOrtsname());
                dto.setWettkampfOrtsinfo(veranstaltungWettkampfDO.getWettkampfOrtsinfo());
                dto.setWettkampfBeginn(veranstaltungWettkampfDO.getWettkampfBeginn());
                dto.setWettkampfDisziplinId(veranstaltungWettkampfDO.getWettkampfDisziplinId());
                dto.setWettkampfTypId(veranstaltungWettkampfDO.getWettkampfTypId());
                dto.setWettkampfAusrichter(veranstaltungWettkampfDO.getWettkampfAusrichter());

                // Veranstaltung-Felder
                dto. setVeranstaltungId(veranstaltungWettkampfDO.getVeranstaltungId());
                dto.setVeranstaltungName(veranstaltungWettkampfDO.getVeranstaltungName());
                dto.setVeranstaltungSportjahr(veranstaltungWettkampfDO.getVeranstaltungSportjahr());
                dto.setVeranstaltungLigaId(veranstaltungWettkampfDO.getVeranstaltungLigaId());

                return dto;
            };

    private VeranstaltungWettkampfDTOMapper() {
        // private constructor to hide public one
    }
}