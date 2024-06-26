package de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.model.SchuetzenstatistikLetzteJahreDTO;

/**
 * Mapping of {@link de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO} and {@link de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.model.SchuetzenstatistikLetzteJahreDTO} objects
 * @author Alessa Hackh
 */

public class SchuetzenstatistikLetzteJahreDTOMapper {

    public static final Function<SchuetzenstatistikLetzteJahreDO, SchuetzenstatistikLetzteJahreDTO> toDTO = schuetzenstatistikLetzteJahreDO -> {

        final String schuetzenname = schuetzenstatistikLetzteJahreDO.getSchuetzenname();
        final long sportjahr1 = schuetzenstatistikLetzteJahreDO.getSportjahr1();
        final long sportjahr2 = schuetzenstatistikLetzteJahreDO.getSportjahr2();
        final long sportjahr3 = schuetzenstatistikLetzteJahreDO.getSportjahr3();
        final long sportjahr4 = schuetzenstatistikLetzteJahreDO.getSportjahr4();
        final long sportjahr5 = schuetzenstatistikLetzteJahreDO.getSportjahr5();
        final long allejahre_schnitt = schuetzenstatistikLetzteJahreDO.getAllejahre_schnitt();

        return new SchuetzenstatistikLetzteJahreDTO(schuetzenname, sportjahr1, sportjahr2, sportjahr3, sportjahr4, sportjahr5, allejahre_schnitt);
    };

    /**
     * I map the {@link SchuetzenstatistikLetzteJahreDTO} object to the {@link SchuetzenstatistikLetzteJahreDO} object
     */
    public static final Function<SchuetzenstatistikLetzteJahreDTO, SchuetzenstatistikLetzteJahreDO> toDO = dto -> {

        SchuetzenstatistikLetzteJahreDO schuetzenstatistikLetzteJahreDO = new SchuetzenstatistikLetzteJahreDO();

        schuetzenstatistikLetzteJahreDO.setSchuetzenname(dto.getSchuetzenname());
        schuetzenstatistikLetzteJahreDO.setSportjahr1(dto.getSportjahr1());
        schuetzenstatistikLetzteJahreDO.setSportjahr2(dto.getSportjahr2());
        schuetzenstatistikLetzteJahreDO.setSportjahr3(dto.getSportjahr3());
        schuetzenstatistikLetzteJahreDO.setSportjahr4(dto.getSportjahr4());
        schuetzenstatistikLetzteJahreDO.setSportjahr5(dto.getSportjahr5());
        schuetzenstatistikLetzteJahreDO.setAllejahre_schnitt(dto.getAllejahre_schnitt());

        return schuetzenstatistikLetzteJahreDO;
    };

    /**
     * Constructor
     */
    private SchuetzenstatistikLetzteJahreDTOMapper() {
        // empty private constructor
    }
}
