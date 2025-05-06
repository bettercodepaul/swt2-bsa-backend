package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSessionDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSessionEntity;
import de.bogenliga.application.business.schusszettel.impl.mapper.TabletSchusszettelMapper;
import de.bogenliga.application.business.schuetze.impl.dao.MitgliedZuordnungDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.common.errorhandling.exception.UnauthorizedException;
import de.bogenliga.application.common.errorhandling.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static de.bogenliga.application.api.ResourceStrings.StatusValues.*;

/*
*  Impl der TabletSchusszettelComponent
*  @author Marty Lauterbach, mklemmingen
*/
@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    @Autowired
    private TabletSessionDAO tabletSessionDAO;

    @Autowired
    private TabletSchusszettelDAO tabletSchusszettelDAO;

    @Autowired
    private MitgliedZuordnungDAO mitgliedZuordnungDAO;

    @Autowired
    private MannschaftsmitgliedDAO mannschaftsmitgliedDAO;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private MatchComponent matchComponent;

    @Override
    public TabletSchusszettelDO getStatus(long wettkampfid, long teamid, String token) {
        TabletSessionEntity session = tabletSessionDAO.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Ungültiger Token"));

        if (!session.getTeamId().equals(teamid) || !session.getWettkampfId().equals(wettkampfid)) {
            return TabletSchusszettelDO.notAllowed();
        }

        TabletSchusszettelDO result = new TabletSchusszettelDO();
        result.setStatus(mapStatus(session.getStatus()));

        if (SATZEINGABE.equals(session.getStatus())) {
            List<TabletSchusszettelEntity> satzdaten = tabletSchusszettelDAO.findByWettkampfUndTeam(wettkampfid, teamid);
            result.setSatzErgebnisse(SchusszettelHelper.buildSatzErgebnisse(satzdaten, session.getCurrentPasseNumber()));

            result.setEigenesTeam(SchusszettelHelper.buildTeamInfo(session.getTeamId(), matchComponent));
            result.setGegnerischesTeam(SchusszettelHelper.buildTeamInfo(session.getGegnerTeamId(), matchComponent));
            result.setSchuetzenMatchPunkte(SchusszettelHelper.buildMatchPunkte(satzdaten));
            result.setSchuetzeStammDaten(SchusszettelHelper.buildSchuetzeStammdaten(session.getCurrentMatchId(), mitgliedZuordnungDAO));
        }

        if (WARTE.equals(session.getStatus())) {
            Optional<TabletSessionEntity> gegner = tabletSessionDAO.findByWettkampfUndTeam(wettkampfid, session.getGegnerTeamId());
            boolean beideWarten = gegner.isPresent() && WARTE.equals(gegner.get().getStatus());

            if (beideWarten) {
                List<TabletSchusszettelEntity> satzdaten = tabletSchusszettelDAO.findByWettkampfUndTeam(wettkampfid, teamid);
                int anzahlSaetze = (int) satzdaten.stream().map(TabletSchusszettelEntity::getSatzNr).distinct().count();

                if (anzahlSaetze >= 5) {
                    session.setStatus(WETTKAMPF_ENDE);
                } else {
                    session.setStatus(SATZEINGABE);
                    session.setCurrentPasseNumber(session.getCurrentPasseNumber() + 1);
                }
                tabletSessionDAO.updateStatus(session, -1L); // -1L = System
                result.setStatus(mapStatus(session.getStatus()));
            }
        }

        return result;
    }

    @Override
    public void submitSchuetzen(long wettkampfid, long teamid, String token, SchuetzenMeldungDO input) {
        TabletSessionEntity session = tabletSessionDAO.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Ungültiger Token"));

        for (Long schuetzenId : input.getGemeldeteSchuetzen()) {
            if (!mannschaftsmitgliedDAO.isMemberOfTeam(schuetzenId, teamid)) {
                throw new ValidationException("Schütze " + schuetzenId + " gehört nicht zum Team " + teamid);
            }
            mitgliedZuordnungDAO.assignToMatch(session.getCurrentMatchId(), schuetzenId);
        }

        session.setStatus(SATZEINGABE);
        session.setCurrentPasseNumber(1);
        tabletSessionDAO.updateStatus(session, -1L);
    }

    @Override
    public void submitSatz(long wettkampfid, long teamid, String token, SatzEingabeDO eingabe) {
        TabletSessionEntity session = tabletSessionDAO.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Ungültiger Token"));

        int satzNr = session.getCurrentPasseNumber();
        List<TabletSchusszettelEntity> entities = TabletSchusszettelMapper.fromDTO(wettkampfid, teamid, eingabe);
        entities.forEach(e -> e.setSatzNr(satzNr));
        entities.forEach(tabletSchusszettelDAO::saveSatzEingabe);

        int satzCount = (int) tabletSchusszettelDAO
                .findByWettkampfUndTeam(wettkampfid, teamid)
                .stream().map(TabletSchusszettelEntity::getSatzNr).distinct().count();

        session.setStatus(satzCount >= 5 ? WARTE : SATZEINGABE);
        session.setCurrentPasseNumber(satzNr + 1);
        tabletSessionDAO.updateStatus(session, -1L);
    }

    private TabletSchusszettelDO.TabletSchusszettelStatus mapStatus(String status) {
        return switch (status) {
            case SATZEINGABE -> TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE;
            case SCHUETZENMELDUNG -> TabletSchusszettelDO.TabletSchusszettelStatus.SCHUETZENMELDUNG;
            case WARTE -> TabletSchusszettelDO.TabletSchusszettelStatus.WARTE;
            case WETTKAMPF_ENDE -> TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE;
            default -> TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED;
        };
    }
}
