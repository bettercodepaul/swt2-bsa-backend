package de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity.SchuetzenstatistikLetzteJahreBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * Data Access Object for the statistics that show the average of an archer for the last five years
 * @author Alessa Hackh
 */

@Repository
public class SchuetzenstatistikLetzteJahreDAO implements DataAccessObject {
    private static final Logger LOG = LoggerFactory.getLogger(SchuetzenstatistikLetzteJahreDAO.class);

    // table name in the database
    private static final String TABLE = "schuetzenstatistik";


    // business entity parameter names
    private static final String SCHUETZENNAME_BE = "schuetzenname";
    private static final String SPORTJAHR1_BE = "sportjahr1";
    private static final String SPORTJAHR2_BE = "sportjahr2";
    private static final String SPORTJAHR3_BE = "sportjahr3";
    private static final String SPORTJAHR4_BE = "sportjahr4";
    private static final String SPORTJAHR5_BE = "sportjahr5";
    private static final String ALLEJAHRE_SCHNITT_BE = "allejahre_schnitt";

    // entity names in table
    private static final String SCHUETZENNAME_TABLE = "schuetzenname";
    private static final String SPORTJAHR1_TABLE = "sportjahr1";
    private static final String SPORTJAHR2_TABLE = "sportjahr2";
    private static final String SPORTJAHR3_TABLE = "sportjahr3";
    private static final String SPORTJAHR4_TABLE = "sportjahr4";
    private static final String SPORTJAHR5_TABLE = "sportjahr5";
    private static final String ALLEJAHRE_SCHNITT_TABLE = "allejahre_schnitt";

    private static final String GET_SCHUETZENSTATISTIKLETZTEJAHRE =
            "WITH veranstaltungschnitte AS (" +
                    "SELECT " +
                    "   MAX(pfeilschnitte_schuetze_veranstaltung_veranstaltung_schnitt) AS veranstaltung_pfeilschnitt, " +
                    "   schuetzenstatistik.schuetzenstatistik_veranstaltung_id, " +
                    "   MAX(schuetzenstatistik_dsb_mitglied_name) AS schuetzenname, " +
                    "   MAX(veranstaltung.veranstaltung_sportjahr) AS veranstaltung_sportjahr, " +
                    "   MAX(highest_veranstaltung.veranstaltung_sportjahr) AS highest_sportjahr " +
                    "FROM schuetzenstatistik JOIN veranstaltung ON veranstaltung.veranstaltung_id = schuetzenstatistik_veranstaltung_id " +
                    "                        JOIN veranstaltung highest_veranstaltung on veranstaltung.veranstaltung_liga_id = highest_veranstaltung.veranstaltung_liga_id " +
                    "                        JOIN pfeilschnitte_schuetze_veranstaltung on pfeilschnitte_schuetze_veranstaltung_veranstaltung_id = veranstaltung.veranstaltung_id " +
                    "WHERE highest_veranstaltung.veranstaltung_sportjahr = ? " +
                    "AND highest_veranstaltung.veranstaltung_id = ? " +
                    "AND schuetzenstatistik.schuetzenstatistik_verein_id = ? " +
                    "AND (highest_veranstaltung.veranstaltung_sportjahr - veranstaltung.veranstaltung_sportjahr) < 5 " +
                    "AND schuetzenstatistik_pfeilpunkte_schnitt > 0 " +
                    "AND pfeilschnitte_schuetze_veranstaltung_dsb_mitglied_id = schuetzenstatistik_dsb_mitglied_id " +
                    "AND veranstaltung.veranstaltung_liga_id = highest_veranstaltung.veranstaltung_liga_id " +
                    "GROUP BY schuetzenstatistik_dsb_mitglied_id, schuetzenstatistik_veranstaltung_id) " +
                    "SELECT " +
                    "   schuetzenname, " +
                    "   MAX(CASE WHEN highest_sportjahr - veranstaltung_sportjahr  = 4 " +
                    "            THEN veranstaltung_pfeilschnitt ELSE 0 END) as sportjahr1, " +
                    "   MAX(CASE WHEN highest_sportjahr - veranstaltung_sportjahr  = 3 " +
                    "            THEN veranstaltung_pfeilschnitt ELSE 0 END) as sportjahr2, " +
                    "   MAX(CASE WHEN highest_sportjahr - veranstaltung_sportjahr  = 2 " +
                    "            THEN veranstaltung_pfeilschnitt ELSE 0 END) as sportjahr3, " +
                    "   MAX(CASE WHEN highest_sportjahr - veranstaltung_sportjahr  = 1 " +
                    "            THEN veranstaltung_pfeilschnitt ELSE 0 END) as sportjahr4, " +
                    "   MAX(CASE WHEN highest_sportjahr - veranstaltung_sportjahr  = 0 " +
                    "            THEN veranstaltung_pfeilschnitt ELSE 0 END) as sportjahr5, " +
                    "   ROUND(AVG(veranstaltung_pfeilschnitt), 2) as allejahre_schnitt " +
                    "FROM veranstaltungschnitte " +
                    "WHERE highest_sportjahr - veranstaltung_sportjahr >= 0 AND highest_sportjahr - veranstaltung_sportjahr <= 4 " +
                    "GROUP BY schuetzenname;";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<SchuetzenstatistikLetzteJahreBE> SCHUETZENSTATISTIKLETZTEJAHRE = new BusinessEntityConfiguration<>(
            SchuetzenstatistikLetzteJahreBE.class, TABLE, getColumnsToFieldsMap(), LOG);

    private final BasicDAO basicDao;

    @Autowired
    public SchuetzenstatistikLetzteJahreDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(SCHUETZENNAME_TABLE, SCHUETZENNAME_BE);
        columnsToFieldsMap.put(SPORTJAHR1_TABLE, SPORTJAHR1_BE);
        columnsToFieldsMap.put(SPORTJAHR2_TABLE, SPORTJAHR2_BE);
        columnsToFieldsMap.put(SPORTJAHR3_TABLE, SPORTJAHR3_BE);
        columnsToFieldsMap.put(SPORTJAHR4_TABLE, SPORTJAHR4_BE);
        columnsToFieldsMap.put(SPORTJAHR5_TABLE, SPORTJAHR5_BE);
        columnsToFieldsMap.put(ALLEJAHRE_SCHNITT_TABLE, ALLEJAHRE_SCHNITT_BE);

        return columnsToFieldsMap;
    }

    public List<SchuetzenstatistikLetzteJahreBE> getSchuetzenstatistikLetzteJahre(final long sportjahr, final long veranstaltungId, final long vereinId) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIKLETZTEJAHRE, GET_SCHUETZENSTATISTIKLETZTEJAHRE, sportjahr, veranstaltungId,vereinId);
    }
}
