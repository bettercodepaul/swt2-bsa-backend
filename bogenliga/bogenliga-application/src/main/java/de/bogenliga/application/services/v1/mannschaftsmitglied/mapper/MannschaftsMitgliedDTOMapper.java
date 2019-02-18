package de.bogenliga.application.services.v1.mannschaftsmitglied.mapper;

import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import org.apache.tomcat.util.buf.StringUtils;

import java.util.function.Function;

public class MannschaftsMitgliedDTOMapper implements DataTransferObject {

    public static final Function<MannschaftsmitgliedDO, MannschaftsMitgliedDTO> toDTO = mannschaftsMitgliedDO -> {
        final Long mannschaftId = mannschaftsMitgliedDO.getMannschaftId();
        final Long dsbMitgliedId = mannschaftsMitgliedDO.getDsbMitgliedId();
        final boolean dsbMitgliedEingesetzt = mannschaftsMitgliedDO.isDsbMitgliedEingesetzt();
        final String dsbMitgliedVorname = mannschaftsMitgliedDO.getDsbMitgliedVorname();
        final String dsbMitgliedNachname = mannschaftsMitgliedDO.getDsbMitgliedNachname();

        return new MannschaftsMitgliedDTO(mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt, dsbMitgliedVorname, dsbMitgliedNachname);
    };

    public static final Function<MannschaftsMitgliedDTO, MannschaftsmitgliedDO> toDO = dto -> {
        final Long mannschaftId = dto.getMannschaftsId();
        final Long dsbMitgliedId = dto.getDsbMitgliedId();
        final boolean dsbMitgliedEingesetzt = dto.isDsbMitgliedEingesetzt();
        final String dsbMitgliedVorname = dto.getDsbMitgliedVorname();
        final String dsbMitgliedNachname = dto.getDsbMitgliedNachname();

        return new MannschaftsmitgliedDO(mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt,dsbMitgliedVorname,dsbMitgliedNachname);
    };


    private MannschaftsMitgliedDTOMapper(){};

}
