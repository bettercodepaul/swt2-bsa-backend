package de.bogenliga.application.business.schuetzenstatistik.impl.mapper;

import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

import java.util.function.Function;

/**
 * I convert the user DataObjects and BusinessEntities.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class SchuetzenstatistikMapper implements ValueObjectMapper {

    /**
     * Converts a {@link SchuetzenstatistikBE} to a {@link SchuetzenstatistikDO}
     */
    public static final Function<SchuetzenstatistikBE, SchuetzenstatistikDO> toSchuetzenstatistikDO = be -> {

        final Long veranstaltungId = be.getVeranstaltungId();
        final String veranstaltungName = be.getVeranstaltungName();
        final Long wettkampfId = be.getWettkampfId();
        final int wettkampfTag = be.getWettkampfTag();
        final Long mannschaftId = be.getMannschaftId();
        final int mannschaftNummer = be.getMannschaftNummer();
        final Long vereinId = be.getVereinId();
        final String vereinName = be.getVereinName();
        final Long matchId = be.getMatchId();
        final int matchNr = be.getMatchNr();
        final Long dsbMitgliedId = be.getDsbMitgliedId();
        final String dsbMitgliedName = be.getDsbMitgliedName();
        final int rueckenNummer = be.getRueckenNummer();
        final float pfeilPunkteSchnitt = be.getPfeilpunkteSchnitt();
        final String[] schuetzeSaetze = be.getSchuetzeSaetze();

        return new SchuetzenstatistikDO(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName, matchId, matchNr, dsbMitgliedId, dsbMitgliedName, rueckenNummer, pfeilPunkteSchnitt,
                schuetzeSaetze);
    };

     /**
     * Converts a {@link SchuetzenstatistikDO} to a {@link SchuetzenstatistikBE}
     */
     public static final Function<SchuetzenstatistikDO, SchuetzenstatistikBE> toSchuetzenstatistikBE = vo -> {

         SchuetzenstatistikBE schuetzenstatistik = new SchuetzenstatistikBE();

         schuetzenstatistik.setVeranstaltungId(vo.getveranstaltungId());
         schuetzenstatistik.setVeranstaltungName(vo.getveranstaltungName());
         schuetzenstatistik.setWettkampfId(vo.getwettkampfId());
         schuetzenstatistik.setWettkampfTag(vo.getwettkampfTag());
         schuetzenstatistik.setMannschaftId(vo.getmannschaftId());
         schuetzenstatistik.setMannschaftNummer(vo.getmannschaftNummer());
         schuetzenstatistik.setVereinId(vo.getvereinId());
         schuetzenstatistik.setVereinName(vo.getvereinName());
         schuetzenstatistik.setMatchId(vo.getMatchId());
         schuetzenstatistik.setMatchNr(vo.getMatchNr());
         schuetzenstatistik.setDsbMitgliedId(vo.getDsbMitgliedId());
         schuetzenstatistik.setDsbMitgliedName(vo.getDsbMitgliedName());
         schuetzenstatistik.setRueckenNummer(vo.getRueckenNummer());
         schuetzenstatistik.setPfeilpunkteSchnitt(vo.getPfeilpunkteSchnitt());
         schuetzenstatistik.setSchuetzeSaetze(vo.getSchuetzeSaetze());

         return schuetzenstatistik;
     };
    /**
     * Private constructor
     */
    private SchuetzenstatistikMapper() {
        // empty private constructor
    }
}
