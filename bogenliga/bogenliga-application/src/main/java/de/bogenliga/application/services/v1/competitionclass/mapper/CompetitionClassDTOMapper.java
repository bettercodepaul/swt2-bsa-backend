package de.bogenliga.application.services.v1.competitionclass.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.competitionclass.model.CompetitionClassDTO;

/**
 * I map the {@link CompetitionClassDO} and {@link CompetitionClassDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class CompetitionClassDTOMapper implements DataTransferObjectMapper {

    private CompetitionClassDTOMapper() {
        // Empty constructor
        // Comment Text
    }

    /**
     * I map the {@link CompetitionClassDO} data object to the {@link CompetitionClassDTO} data access object
     */
    public static final Function<CompetitionClassDO, CompetitionClassDTO> toDTO = competitionClassDO -> {

        final Long klasseId = competitionClassDO.getId();
        final String klasseName = competitionClassDO.getKlasseName();
        final Long klasseJahrgangMin = competitionClassDO.getKlasseJahrgangMin();
        final Long klasseJahrgangMax = competitionClassDO.getKlasseJahrgangMax();
        final Long klasseNr = competitionClassDO.getKlasseNr();

        return new CompetitionClassDTO(klasseId, klasseName, klasseJahrgangMin, klasseJahrgangMax, klasseNr);
    };

    /**
     * I map the {@link CompetitionClassDTO} data access object to the {@link CompetitionClassDO} data object
     */
    public static final Function<CompetitionClassDTO, CompetitionClassDO> toDO = competitionClassDTO -> {
        final Long klasseId = competitionClassDTO.getId();
        final String klasseName = competitionClassDTO.getKlasseName();
        final Long klasseJahrgangMin = competitionClassDTO.getKlasseJahrgangMin();
        final Long klasseJahrgangMax = competitionClassDTO.getKlasseJahrgangMax();
        final Long klasseNr = competitionClassDTO.getKlasseNr();

        return new CompetitionClassDO(klasseId, klasseName, klasseJahrgangMin, klasseJahrgangMax, klasseNr);
    };
}
