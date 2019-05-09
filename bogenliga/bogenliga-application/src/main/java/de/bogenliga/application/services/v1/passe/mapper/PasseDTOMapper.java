package de.bogenliga.application.services.v1.passe.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;


public class PasseDTOMapper implements DataTransferObjectMapper {

    private PasseDTOMapper() {
    }


    /**
     * Map {@link PasseDO} to {@link PasseDTO}
     */
    public static final Function<PasseDO, PasseDTO> toDTO = passeDO -> {
        final Integer[] ringzahlen = {
                passeDO.getPfeil1(), passeDO.getPfeil2(), passeDO.getPfeil3(),
                passeDO.getPfeil4(), passeDO.getPfeil5(), passeDO.getPfeil6()
        };

        return new PasseDTO(
                passeDO.getId(), passeDO.getPasseMannschaftId(), passeDO.getPasseWettkampfId(),
                passeDO.getPasseMatchNr(), passeDO.getPasseMatchId(),
                passeDO.getPasseLfdnr(), passeDO.getPasseDsbMitgliedId(),
                ringzahlen
        );
    };

    /**
     * Map {@Link PasseDTO} to {@Link PasseDO}
     */
    public static final Function<PasseDTO, PasseDO> toDO = passeDTO -> {
        Integer[] ringzahlen = passeDTO.getRingzahl();

        return new PasseDO(
                passeDTO.getId(), passeDTO.getMannschaftId(), passeDTO.getWettkampfId(),
                passeDTO.getMatchNr(), passeDTO.getMatchId(),
                passeDTO.getLfdNr(), passeDTO.getDsbMitgliedId(),
                ringzahlen[0], ringzahlen[1], ringzahlen[2], ringzahlen[3], ringzahlen[4], ringzahlen[5]
        );
    };
}
