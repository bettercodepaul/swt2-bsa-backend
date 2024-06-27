package de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity.SchuetzenstatistikLetzteJahreBE;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreMapper {

    /**
     * Converts a {@link SchuetzenstatistikLetzteJahreBE} to a {@link SchuetzenstatistikLetzteJahreDO}
     */
    public static final Function<SchuetzenstatistikLetzteJahreBE, SchuetzenstatistikLetzteJahreDO> toSchuetzenstatistikLetzteJahreDO = be -> {

        final String schuetzenname = be.getSchuetzenname();
        final float sportjahr1 = be.getSportjahr1();
        final float sportjahr2 = be.getSportjahr2();
        final float sportjahr3 = be.getSportjahr3();
        final float sportjahr4 = be.getSportjahr4();
        final float sportjahr5 = be.getSportjahr5();
        final float allejahre_schnitt = be.getAllejahre_schnitt();

        return new SchuetzenstatistikLetzteJahreDO(schuetzenname, sportjahr1, sportjahr2, sportjahr3, sportjahr4, sportjahr5, allejahre_schnitt);
    };

    /**
     * Converts a {@link SchuetzenstatistikLetzteJahreDO} to a {@link SchuetzenstatistikLetzteJahreBE}
     */
    public static final Function<SchuetzenstatistikLetzteJahreDO, SchuetzenstatistikLetzteJahreBE> toSchuetzenstatistikLetzteJahreBE = vo -> {
        SchuetzenstatistikLetzteJahreBE schuetzenstatistikLetzteJahre = new SchuetzenstatistikLetzteJahreBE();

        schuetzenstatistikLetzteJahre.setSchuetzenname(vo.getSchuetzenname());
        schuetzenstatistikLetzteJahre.setSportjahr1(vo.getSportjahr1());
        schuetzenstatistikLetzteJahre.setSportjahr2(vo.getSportjahr2());
        schuetzenstatistikLetzteJahre.setSportjahr3(vo.getSportjahr3());
        schuetzenstatistikLetzteJahre.setSportjahr4(vo.getSportjahr4());
        schuetzenstatistikLetzteJahre.setSportjahr5(vo.getSportjahr5());
        schuetzenstatistikLetzteJahre.setAllejahre_schnitt(vo.getAllejahre_schnitt());

        return schuetzenstatistikLetzteJahre;
    };

    private SchuetzenstatistikLetzteJahreMapper() {
        // empty private constructor
    }
}
