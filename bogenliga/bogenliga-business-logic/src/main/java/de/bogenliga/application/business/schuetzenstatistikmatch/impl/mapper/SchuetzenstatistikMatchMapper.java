package de.bogenliga.application.business.schuetzenstatistikmatch.impl.mapper;
import java.util.function.Function;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity.SchuetzenstatistikMatchBE;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * I convert the user DataObjects and BusinessEntities.
 *
 * @author  Lennart Raach
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class SchuetzenstatistikMatchMapper implements ValueObjectMapper {

    /**
     * Converts a {@link SchuetzenstatistikMatchBE} to a {@link SchuetzenstatistikMatchDO}
     */
    public static final Function<SchuetzenstatistikMatchBE, SchuetzenstatistikMatchDO> toSchuetzenstatistikMatchDO = be -> {

        final int rueckennummer = be.getRueckennummer();
        final String dsbMitgliedName = be.getDsbMitgliedName();
        final float match1 = be.getMatch1();
        final float match2 = be.getMatch2();
        final float match3 = be.getMatch3();
        final float match4 = be.getMatch4();
        final float match5 = be.getMatch5();
        final float match6 = be.getMatch6();
        final float match7 = be.getMatch7();
        final float pfeilpunkteSchnitt = be.getPfeilpunkteSchnitt();
        return new SchuetzenstatistikMatchDO(rueckennummer, dsbMitgliedName, match1, match2,
                match3, match4, match5, match6, match7, pfeilpunkteSchnitt);
    };

    /**
     * Converts a {@link SchuetzenstatistikMatchDO} to a {@link SchuetzenstatistikMatchBE}
     */
    public static final Function<SchuetzenstatistikMatchDO, SchuetzenstatistikMatchBE> toSchuetzenstatistikMatchBE = vo -> {

        SchuetzenstatistikMatchBE schuetzenstatistikMatchBE  = new SchuetzenstatistikMatchBE();

        schuetzenstatistikMatchBE.setRueckennummer(vo.getRueckennummer());
        schuetzenstatistikMatchBE.setDsbMitgliedName(vo.getDsbMitgliedName());
        schuetzenstatistikMatchBE.setMatch1(vo.getMatch1());
        schuetzenstatistikMatchBE.setMatch2(vo.getMatch2());
        schuetzenstatistikMatchBE.setMatch3(vo.getMatch3());
        schuetzenstatistikMatchBE.setMatch4(vo.getMatch4());
        schuetzenstatistikMatchBE.setMatch5(vo.getMatch5());
        schuetzenstatistikMatchBE.setMatch6(vo.getMatch6());
        schuetzenstatistikMatchBE.setMatch7(vo.getMatch7());
        schuetzenstatistikMatchBE.setPfeilpunkteSchnitt(vo.getPfeilpunkteSchnitt());
        return schuetzenstatistikMatchBE;
    };
}
