package de.bogenliga.application.services.v1.passe.mapper;

import java.util.function.Function;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class PasseDTOMapper implements DataTransferObjectMapper {


    public PasseDTOMapper() {
    }

    public static final Function<PasseDO, PasseDTO> toDTO = passeDO -> {
        final int mannschaftsID ;
    }
}
