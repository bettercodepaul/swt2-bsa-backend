package de.bogenliga.application.business.ligatabelle.impl.mapper;

import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.business.ligatabelle.impl.entity.LigatabelleBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;

/**
 * I convert the user DataObjects and BusinessEntities.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class LigatabelleMapper implements ValueObjectMapper {

    /**
     * Converts a {@link LigatabelleBE} to a {@link LigatabelleDO}
     */
    public static final Function<LigatabelleBE, LigatabelleDO> toLigatabelleDO = be -> {

        final Long veranstaltungId= be.getVeranstaltungId();
        final String veranstaltungName= be.getVeranstaltungName();
        final Long wettkampfId= be.getWettkampfId();
        final int wettkampfTag= be.getWettkampfTag();
        final Long mannschaftId= be.getMannschaftId();
        final int mannschaftNummer=be.getMannschaftNummer();
        final Long vereinId=be.getVereinId();
        final String vereinName=be.getVereinName();
        final int matchpkt=be.getMatchpkt();
        final int matchpkt_gegen=be.getMatchpkt_gegen();
        final int satzpkt=be.getSatzpkt();
        final int satzpkt_gegen=be.getSatzpkt_gegen();
        final int satzpkt_differenz=be.getSatzpkt_differenz();
        final int sortierung=be.getSortierung();
        final int tabellenplatz=be.getTabellenplatz();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new LigatabelleDO(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName, matchpkt, matchpkt_gegen, satzpkt, satzpkt_gegen,
                satzpkt_differenz, sortierung,tabellenplatz);

    };

     /**
     * Converts a {@link LigatabelleDO} to a {@link LigatabelleBE}
     */
    public static final Function<LigatabelleDO, LigatabelleBE> toLigatabelleBE = vo -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getLastModifiedAtUtc());

        LigatabelleBE ligatabelleBE = new LigatabelleBE();

        ligatabelleBE.setVeranstaltungId(vo.getveranstaltungId());
        ligatabelleBE.setVeranstaltungName(vo.getveranstaltungName());
        ligatabelleBE.setWettkampfId(vo.getwettkampfId());
        ligatabelleBE.setWettkampfTag(vo.getwettkampfTag());
        ligatabelleBE.setMannschaftId(vo.getmannschaftId());
        ligatabelleBE.setMannschaftNummer(vo.getmannschaftNummer());
        ligatabelleBE.setVereinId(vo.getvereinId());
        ligatabelleBE.setVereinName(vo.getvereinName());
        ligatabelleBE.setMatchpkt(vo.getmatchpkt());
        ligatabelleBE.setMatchpkt_gegen(vo.getmatchpktGegen());
        ligatabelleBE.setSatzpkt(vo.getsatzpkt());
        ligatabelleBE.setSatzpkt_gegen(vo.getsatzpktGegen());
        ligatabelleBE.setSatzpkt_differenz(vo.getsatzpktDifferenz());
        ligatabelleBE.setSortierung(vo.getsortierung());
        ligatabelleBE.setTabellenplatz(vo.gettabellenplatz());


        return ligatabelleBE;
    };



    /**
     * Private constructor
     */
    private LigatabelleMapper() {
        // empty private constructor
    }
}
