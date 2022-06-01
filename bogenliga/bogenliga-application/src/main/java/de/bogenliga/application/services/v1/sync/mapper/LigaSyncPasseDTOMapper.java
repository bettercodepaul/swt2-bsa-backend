package de.bogenliga.application.services.v1.sync.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;


public class LigaSyncPasseDTOMapper implements DataTransferObjectMapper {

    /**
     * Map {@link PasseDO} to {@link LigaSyncPasseDTO}
     */
    public static final Function<PasseDO, LigaSyncPasseDTO> toDTO = LigaSyncPasseDTOMapper::apply;
    public static final Function<LigaSyncPasseDTO, PasseDO> toDO = LigaSyncPasseDTOMapper::apply;

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

    public static PasseDO apply(LigaSyncPasseDTO ligasyncpasseDTO) {
        final Integer[] ringzahlen = ligasyncpasseDTO.getRingzahl();
        return new PasseDO(ligasyncpasseDTO.getId(), ligasyncpasseDTO.getMannschaftId(),
                ligasyncpasseDTO.getWettkampfId(), null, null, ligasyncpasseDTO.getLfdNr(),
                ligasyncpasseDTO.getDsbMitgliedId(), ringzahlen[0], ringzahlen[1], ringzahlen[2], ringzahlen[3], ringzahlen[4], ringzahlen[5]);
    }
}