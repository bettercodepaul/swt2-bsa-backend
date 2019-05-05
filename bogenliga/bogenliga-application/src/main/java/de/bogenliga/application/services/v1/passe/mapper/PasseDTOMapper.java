package de.bogenliga.application.services.v1.passe.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.Passe.api.types.PasseDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;

/**
 * TODO [AL] class documentation
 *
 * @author Yannik Prigl
 */
public class PasseDTOMapper implements DataTransferObjectMapper {


    public PasseDTOMapper() {
    }


    /**
     * Map {@link PasseDO} to {@link PasseDTO}
     */
    public static final Function<PasseDO, PasseDTO> toDTO = passeDO -> {
        final long id = passeDO.getId();
        final long mannschaftsId = passeDO.getPasseMannschaftId();
        final long wettkampfId = passeDO.getPasseWettkampfId();
        final long matchnr = passeDO.getPasseMatchNr();
        final long lfdnr = passeDO.getPasseLfdnr();
        final long dsbMitgliedId = passeDO.getPasseDsbMitgliedId();
        final int[] ringzahl = {passeDO.getPfeil1(), passeDO.getPfeil2(), passeDO.getPfeil3(),
                passeDO.getPfeil4(), passeDO.getPfeil5(), passeDO.getPfeil6()};
        return new PasseDTO(id, mannschaftsId, wettkampfId, matchnr,
                lfdnr, dsbMitgliedId, ringzahl);
    };

    /**
     * Map {@Link PasseDTO} to {@Link PasseDO}
     */
    public static final Function<PasseDTO, PasseDO> toDO = passeDTO -> {
        final long id = passeDTO.getId();
        final long mannschaftsId = passeDTO.getMannschaftId();
        final long wettkampfId = passeDTO.getWettkampfId();
        final long matchnr = passeDTO.getMatchNr();
        final long lfdnr = passeDTO.getLfdNr();
        final long dsbMitgliedId = passeDTO.getDsbMitgliedNr();
        final int pfeil1 = passeDTO.getRingzahl()[0];
        final int pfeil2 = passeDTO.getRingzahl()[1];
        final int pfeil3 = passeDTO.getRingzahl()[2];
        final int pfeil4 = passeDTO.getRingzahl()[3];
        final int pfeil5 = passeDTO.getRingzahl()[4];
        final int pfeil6 = passeDTO.getRingzahl()[5];

        return new PasseDO(id,mannschaftsId,wettkampfId,matchnr,
                lfdnr,dsbMitgliedId,pfeil1,pfeil2,pfeil3,pfeil4,pfeil5,pfeil6);
    };
}

