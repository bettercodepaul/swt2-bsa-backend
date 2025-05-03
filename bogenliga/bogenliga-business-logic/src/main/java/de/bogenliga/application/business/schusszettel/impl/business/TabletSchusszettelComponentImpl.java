package de.bogenliga.application.business.schusszettel.impl.business;

import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSessionDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSessionEntity;
import de.bogenliga.application.business.schusszettel.impl.mapper.TabletSessionMapper;
import de.bogenliga.application.business.schusszettel.impl.mapper.TabletSchusszettelMapper;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.passe.impl.dao.PasseDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.schuetze.impl.dao.MitgliedZuordnungDAO;
import de.bogenliga.application.common.errorhandling.exception.UnauthorizedException;
import de.bogenliga.application.common.errorhandling.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementierung der TabletSchusszettelComponent Businesslogik.
 *
 * @author Marty Lauterbach, mklemmingen
 */
@Service
public class TabletSchusszettelComponentImpl implements TabletSchusszettelComponent {

    @Autowired
    private TabletSessionDAO tabletSessionDAO;

    @Autowired
    private PasseDAO passeDAO;

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private MannschaftsmitgliedDAO mannschaftsmitgliedDAO;

    @Autowired
    private MitgliedZuordnungDAO mitgliedZuordnungDAO;

    @Override
    public TabletSchusszettelDO getStatus(long wettkampfid, long teamid, String token) {
        TabletSessionEntity session = tabletSessionDAO.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Ungültiger Token"));

        if (!session.getTeamId().equals(teamid) || !session.getWettkampfId().equals(wettkampfid)) {
            return TabletSchusszettelDO.notAllowed();
        }

        TabletSchusszettelDO resultDO = new TabletSchusszettelDO();
        resultDO.setStatus(TabletSessionMapper.mapStatus(session.getStatus()));

        if ("SATZEINGABE".equals(session.getStatus())) {
            MatchBE match = matchDAO.findById(session.getCurrentMatchId());
            resultDO.setSatzErgebnisse(passeDAO.findGroupedBySchuetze(match.getId(), teamid));
            // TODO: fill rest of TabletSchusszettelDO with TeamInfo, Schützen, etc.
        }

        if ("WARTE".equals(session.getStatus())) {
            Optional<TabletSessionEntity> gegnerSession = tabletSessionDAO.findByWettkampfUndTeam(
                    wettkampfid, session.getGegnerTeamId());

            boolean beideInWarte = gegnerSession.isPresent()
                    && "WARTE".equals(gegnerSession.get().getStatus());

            if (beideInWarte) {
                int passeCount = passeDAO.countByMatchAndTeam(session.getCurrentMatchId(), teamid);
                if (passeCount >= 15) {
                    tabletSessionDAO.updateStatus(session.getId(), "WETTKAMPF_ENDE",
                            session.getCurrentPasseNumber(), session.getCurrentMatchId());
                    resultDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.WETTKAMPF_ENDE);
                } else {
                    tabletSessionDAO.updateStatus(session.getId(), "SATZEINGABE",
                            session.getCurrentPasseNumber(), session.getCurrentMatchId());
                    resultDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
                }
            }
        }

        return resultDO;
    }

    @Override
    public void submitSchuetzen(long wettkampfid, long teamid, String token, SchuetzenMeldungDO doObj) {
        TabletSessionEntity session = tabletSessionDAO.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Ungültiger Token"));

        for (Long mitgliedId : doObj.getGemeldeteSchuetzen()) {
            if (!mannschaftsmitgliedDAO.isMemberOfTeam(mitgliedId, teamid)) {
                throw new ValidationException("Schütze gehört nicht zum Team");
            }
            mitgliedZuordnungDAO.assignToMatch(session.getCurrentMatchId(), mitgliedId);
        }

        tabletSessionDAO.updateStatus(session.getId(), "SATZEINGABE", 1, session.getCurrentMatchId());
    }

    @Override
    public void submitSatz(long wettkampfid, long teamid, String token, SatzEingabeDO doObj) {
        TabletSessionEntity session = tabletSessionDAO.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Ungültiger Token"));

        int aktuellePasse = session.getCurrentPasseNumber();

        doObj.getSatzeingabe().forEach(satz -> passeDAO.insertPasse(
                session.getCurrentMatchId(),
                wettkampfid,
                teamid,
                satz.getSchuetzenId(),
                aktuellePasse,
                satz.getSchuss1(),
                satz.getSchuss2(),
                satz.getSchuss3()
        ));

        int totalSatzEingaben = passeDAO.countByMatchAndTeam(session.getCurrentMatchId(), teamid);

        if (totalSatzEingaben >= 15) {
            tabletSessionDAO.updateStatus(session.getId(), "WARTE", aktuellePasse + 1, session.getCurrentMatchId());
        } else {
            tabletSessionDAO.updateStatus(session.getId(), "SATZEINGABE", aktuellePasse + 1, session.getCurrentMatchId());
        }
    }
}
