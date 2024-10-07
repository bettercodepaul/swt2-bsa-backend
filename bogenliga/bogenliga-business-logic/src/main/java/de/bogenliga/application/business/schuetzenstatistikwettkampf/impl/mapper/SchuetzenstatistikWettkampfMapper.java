package de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity.SchuetzenstatistikWettkampfBE;

/**
 * converts BE and DO objects
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfMapper {

    /**
     * Converts a {@link SchuetzenstatistikWettkampfBE} to a {@link SchuetzenstatistikWettkampftageDO}
     */
    public static final Function<SchuetzenstatistikWettkampfBE, SchuetzenstatistikWettkampftageDO> toSchuetzenstatistikWettkampfDO = be -> {


        final String dsbMitgliedName = be.getDsbMitgliedName();
        final int rueckenNummer = be.getRueckenNummer();
        final float wettkampftag1 = be.getWettkampftag1();
        final float wettkampftag2 = be.getWettkampftag2();
        final float wettkampftag3 = be.getWettkampftag3();
        final float wettkampftag4 = be.getWettkampftag4();
        final float wettkampftageSchnitt = be.getWettkampftageSchnitt();

        return new SchuetzenstatistikWettkampftageDO(dsbMitgliedName, rueckenNummer,
                wettkampftag1, wettkampftag2, wettkampftag3, wettkampftag4, wettkampftageSchnitt);
    };

    /**
     * Converts a {@link SchuetzenstatistikWettkampftageDO} to a {@link SchuetzenstatistikWettkampfBE}
     */
    public static final Function<SchuetzenstatistikWettkampftageDO, SchuetzenstatistikWettkampfBE> toSchuetzenstatistikWettkampfBE = vo -> {

        SchuetzenstatistikWettkampfBE schuetzenstatistikWettkampf = new SchuetzenstatistikWettkampfBE();

        schuetzenstatistikWettkampf.setDsbMitgliedName(vo.getDsbMitgliedName());
        schuetzenstatistikWettkampf.setRueckenNummer(vo.getRueckenNummer());
        schuetzenstatistikWettkampf.setWettkampftag1(vo.getWettkampftag1());
        schuetzenstatistikWettkampf.setWettkampftag2(vo.getWettkampftag2());
        schuetzenstatistikWettkampf.setWettkampftag3(vo.getWettkampftag3());
        schuetzenstatistikWettkampf.setWettkampftag4(vo.getWettkampftag4());
        schuetzenstatistikWettkampf.setWettkampftageSchnitt(vo.getWettkampftageSchnitt());

        return schuetzenstatistikWettkampf;
    };
    /**
     * Private constructor
     */
    private SchuetzenstatistikWettkampfMapper() {
        // empty private constructor
    }
}
