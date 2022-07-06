package de.bogenliga.application.services.v1.sync.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;


public class LigaSyncPasseDTOMapper implements DataTransferObjectMapper {

    /**
     * Map {@link PasseDO} to {@link LigaSyncPasseDTO}
     */
    public static final Function<PasseDO, LigaSyncPasseDTO> toDTO = LigaSyncPasseDTOMapper::apply;

    /**
     * Map {@link LigaSyncPasseDTO} to {@link PasseDTO}
     */
    public static final Function<LigaSyncPasseDTO, PasseDTO> toPasseDTO = LigaSyncPasseDTOMapper::apply;

    private LigaSyncPasseDTOMapper() {
        // empty private constructor
    }

    public static LigaSyncPasseDTO apply(PasseDO passeDO) {
        final Integer[] ringzahlen = {
                passeDO.getPfeil1(), passeDO.getPfeil2(), passeDO.getPfeil3(),
                passeDO.getPfeil4(), passeDO.getPfeil5(), passeDO.getPfeil6()
        };
        return new LigaSyncPasseDTO(passeDO.getId(), passeDO.getVersion(), passeDO.getPasseMatchId(),
                passeDO.getPasseMannschaftId(), passeDO.getPasseWettkampfId(), passeDO.getPasseLfdnr(),
                passeDO.getPasseDsbMitgliedId(), ringzahlen);
    }

    public static PasseDTO apply(LigaSyncPasseDTO ligapassesyncDTO) {

        return new PasseDTO(ligapassesyncDTO.getId(), ligapassesyncDTO.getMannschaftId(), ligapassesyncDTO.getWettkampfId(),
                null, ligapassesyncDTO.getMatchId(), ligapassesyncDTO.getLfdNr(), ligapassesyncDTO.getRueckennummer(),
                ligapassesyncDTO.getDsbMitgliedId(), ligapassesyncDTO.getRingzahl());
    }
}