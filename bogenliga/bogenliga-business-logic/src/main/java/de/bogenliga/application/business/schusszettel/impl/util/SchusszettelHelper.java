package de.bogenliga.application.business.schusszettel.impl.util;

import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.business.schuetze.impl.dao.MitgliedZuordnungDAO;
import de.bogenliga.application.business.schuetze.impl.entity.MitgliedMatchBE;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Hilfsklasse für die Zusammenstellung von TabletSchusszettelDO.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class SchusszettelHelper {

    public static List<SatzErgebnisDO> buildSatzErgebnisse(List<TabletSchusszettelEntity> satzdaten, int aktuellePasse) {
        Map<Integer, List<TabletSchusszettelEntity>> gruppiertNachSatz = satzdaten.stream()
                .collect(Collectors.groupingBy(TabletSchusszettelEntity::getSatzNr));

        List<SatzErgebnisDO> result = new ArrayList<>();
        for (Map.Entry<Integer, List<TabletSchusszettelEntity>> entry : gruppiertNachSatz.entrySet()) {
            int satzNr = entry.getKey();
            List<TabletSchusszettelEntity> eintraege = entry.getValue();

            int team1Summe = eintraege.stream()
                    .filter(e -> e.getTeamId().equals(eintraege.get(0).getTeamId()))
                    .mapToInt(e -> safeInt(e.getSchuss1()) + safeInt(e.getSchuss2()) + safeInt(e.getSchuss3()))
                    .sum();

            // Hinweis: Gegnerische Punkte setzen wir auf 0, da das Tablet nur eigene Teamdaten hat
            result.add(new SatzErgebnisDO(satzNr, team1Summe, 0));
        }

        // Sortiert zurückgeben
        result.sort(Comparator.comparingInt(SatzErgebnisDO::getSatzNr));
        return result;
    }

    public static TeamInfoDO buildTeamInfo(Long teamId, MatchComponent matchComponent) {
        String name = matchComponent.getMannschaftsNameByID(teamId);
        return new TeamInfoDO(teamId, name);
    }

    public static List<SchuetzeMatchPunkteDO> buildMatchPunkte(List<TabletSchusszettelEntity> daten) {
        Map<Long, Integer> punkteProSchuetze = new HashMap<>();

        for (TabletSchusszettelEntity entry : daten) {
            int punkte = safeInt(entry.getSchuss1()) + safeInt(entry.getSchuss2()) + safeInt(entry.getSchuss3());
            punkteProSchuetze.merge(entry.getSchuetzenId(), punkte, Integer::sum);
        }

        return punkteProSchuetze.entrySet().stream()
                .map(e -> new SchuetzeMatchPunkteDO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public static List<SchuetzeStammdatenDO> buildSchuetzeStammdaten(Long matchId, MitgliedZuordnungDAO dao) {
        List<MitgliedMatchBE> zugeordnete = dao.findByMatchId(matchId);

        return zugeordnete.stream()
                .map(be -> new SchuetzeStammdatenDO(
                        be.getMitgliedId(),
                        be.getRueckennummer(),
                        be.getVorname(),
                        be.getNachname()
                ))
                .collect(Collectors.toList());
    }

    private static int safeInt(Integer i) {
        return i == null ? 0 : i;
    }
}
