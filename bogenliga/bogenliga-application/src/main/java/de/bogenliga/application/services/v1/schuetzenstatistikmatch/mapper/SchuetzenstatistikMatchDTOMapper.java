package de.bogenliga.application.services.v1.schuetzenstatistikmatch.mapper;


import java.util.function.Function;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDO;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity.SchuetzenstatistikMatchBE;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.schuetzenstatistikmatch.model.SchuetzenstatistikMatchDTO;


/**
 * I map the {@link SchuetzenstatistikMatchDO} and {@link SchuetzenstatistikMatchDTO} objects
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchDTOMapper implements DataTransferObjectMapper {
    /**
     * I map the {@link SchuetzenstatistikMatchDO} object to the {@link SchuetzenstatistikMatchDTO} object
     */
    public static final Function<SchuetzenstatistikMatchDO, SchuetzenstatistikMatchDTO> toDTO = schuetzenstatistikDO -> {

        final int rueckennummer = schuetzenstatistikDO.getRueckennummer();
        final String dsbMitgliedName = schuetzenstatistikDO.getDsbMitgliedName();
        final float match1 = schuetzenstatistikDO.getMatch1();
        final float match2 = schuetzenstatistikDO.getMatch2();
        final float match3 = schuetzenstatistikDO.getMatch3();
        final float match4 = schuetzenstatistikDO.getMatch4();
        final float match5 = schuetzenstatistikDO.getMatch5();
        final float match6 = schuetzenstatistikDO.getMatch6();
        final float match7 = schuetzenstatistikDO.getMatch7();
        final float pfeilpunkteSchnitt = schuetzenstatistikDO.getPfeilpunkteSchnitt();

        return new SchuetzenstatistikMatchDTO(rueckennummer, dsbMitgliedName, match1, match2,
                match3, match4, match5, match6, match7, pfeilpunkteSchnitt);
    };

    /**
     * Converts a {@link SchuetzenstatistikMatchDO} to a {@link SchuetzenstatistikMatchBE}
     */
    public static final Function<SchuetzenstatistikMatchDTO, SchuetzenstatistikMatchDO> toDO = dto -> {

        SchuetzenstatistikMatchDO schuetzenstatistikMatchDO  = new SchuetzenstatistikMatchDO();

        schuetzenstatistikMatchDO.setRueckennummer(dto.getRueckennummer());
        schuetzenstatistikMatchDO.setDsbMitgliedName(dto.getDsbMitgliedName());
        schuetzenstatistikMatchDO.setMatch1(dto.getMatch1());
        schuetzenstatistikMatchDO.setMatch2(dto.getMatch2());
        schuetzenstatistikMatchDO.setMatch3(dto.getMatch3());
        schuetzenstatistikMatchDO.setMatch4(dto.getMatch4());
        schuetzenstatistikMatchDO.setMatch5(dto.getMatch5());
        schuetzenstatistikMatchDO.setMatch6(dto.getMatch6());
        schuetzenstatistikMatchDO.setMatch7(dto.getMatch7());
        schuetzenstatistikMatchDO.setPfeilpunkteSchnitt(dto.getPfeilpunkteSchnitt());

        return schuetzenstatistikMatchDO;
    };
}
