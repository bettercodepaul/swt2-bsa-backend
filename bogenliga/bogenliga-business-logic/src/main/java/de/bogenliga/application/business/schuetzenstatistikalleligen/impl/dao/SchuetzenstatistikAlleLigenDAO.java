package de.bogenliga.application.business.schuetzenstatistikalleligen.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
//import de.bogenliga.application.business.schuetzenstatistikalleligen.impl.entity.SchuetzenstatistikAlleLigenBE;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity.SchuetzenstatistikWettkampfBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;

/**
 * TODO [AL] class documentation
 *
 * @author Alessa Hackh
 */
/*public class SchuetzenstatistikAlleLigenDAO {

    private static final Logger LOG = LoggerFactory.getLogger(SchuetzenstatistikAlleLigenDAO.class);
    private static final String TABLE = "schuetzenstatistik";

    // business entity parameters
    private static final String DSBMITGLIEDNAME_BE = "dsbMitgliedName";
    private static final String SCHNITT_WKT1_BE = "schnitt_wettkampftage1";
    private static final String SCHNITT_WKT2_BE = "schnitt_wettkampftage2";
    private static final String SCHNITT_WKT3_BE = "schnitt_wettkampftage3";
    private static final String SCHNITT_WKT4_BE = "schnitt_wettkampftage4";
    private static final String SCHNITT_SPORTJAHR_BE = "schnitt_sportjahr";

    // table columns
    private static final String DSBMITGLIEDNAME_TABLE = "schuetzenstatistik_dbs_mitglied_name";
    private static final String SCHNITT_WKT1_TABLE = "schuetzenstatistik_schnitt_wkt1_table";
    private static final String SCHNITT_WKT2_TABLE = "schuetzenstatistik_schnitt_wkt2_table";
    private static final String SCHNITT_WKT3_TABLE = "schuetzenstatistik_schnitt_wkt3_table";
    private static final String SCHNITT_WKT4_TABLE = "schuetzenstatistik_schnitt_wkt4_table";
    private static final String SCHNITT_SPORTJAHR_TABLE = "schuetzenstatistik_sportjahr_schnitt";

    private static final String GET_SCHUETZENSTATISTIK_ALLELIGEN =
            "WITH wettkampfschnitte AS (" +
                    "SELECT" +
                    "    schuetzenstatistik_dsb_mitglied_id as dsb_mitglied_id," +
                    "    schuetzenstatistik_wettkampf_tag as wettkampf_tag," +
                    "    schuetzenstatistik_dsb_mitglied_name as dsb_mitglied_name," +
                    "    schuetzenstatistik_verein_id as verein_id," +
                    "    veranstaltung_sportjahr as sportjahr," +
                    "    ROUND(AVG(CASE WHEN schuetzenstatistik_pfeilpunkte_schnitt <> 0 THEN schuetzenstatistik_pfeilpunkte_schnitt END), 2) AS wettkampf_schnitt" +
                    " FROM schuetzenstatistik JOIN veranstaltung on schuetzenstatistik.schuetzenstatistik_veranstaltung_id = veranstaltung.veranstaltung_id" +
                    " GROUP BY schuetzenstatistik_dsb_mitglied_id, veranstaltung_sportjahr, schuetzenstatistik.schuetzenstatistik_wettkampf_tag, dsb_mitglied_name, schuetzenstatistik_verein_id)" +
            " SELECT" +
                    "    MAX(wettkampfschnitte.dsb_mitglied_name)," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 1 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_1," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 2 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_2," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 3 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_3," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 4 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_4," +
                    "    ROUND(AVG(wettkampfschnitte.wettkampf_schnitt), 2) as sportjahr_schnitt" +
            " FROM wettkampfschnitte" +
            " WHERE sportjahr = ? AND wettkampfschnitte_veranstaltung_id =? AND wettkampfschnitte_verein_id =?" +
            " GROUP BY wettkampfschnitte.dsb_mitglied_id, sportjahr;";

    private static final BusinessEntityConfiguration<SchuetzenstatistikAlleLigenBE> SCHUETZENSTATISTIK_ALLELIGEN = new BusinessEntityConfiguration<>(
            SchuetzenstatistikAlleLigenBE.class, TABLE, getColumnsToFieldsMap(), LOG);

    private final BasicDAO basicDao;

    @Autowired
    public SchuetzenstatistikAlleLigenDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(DSBMITGLIEDNAME_TABLE, DSBMITGLIEDNAME_BE);
        columnsToFieldsMap.put(SCHNITT_WKT1_TABLE, SCHNITT_WKT1_BE);
        columnsToFieldsMap.put(SCHNITT_WKT2_TABLE, SCHNITT_WKT2_BE);
        columnsToFieldsMap.put(SCHNITT_WKT3_TABLE, SCHNITT_WKT3_BE);
        columnsToFieldsMap.put(SCHNITT_WKT4_TABLE, SCHNITT_WKT4_BE);
        columnsToFieldsMap.put(SCHNITT_SPORTJAHR_TABLE, SCHNITT_SPORTJAHR_BE);

        return columnsToFieldsMap;
    }

    /**
     * Statistik des gewählten Sportjahres erhalten
     */
    /*public List<SchuetzenstatistikAlleLigenBE> getSchuetzenstatistikAlleLigenVeranstaltung(final String sportjahr) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIK_ALLELIGEN, GET_SCHUETZENSTATISTIK_ALLELIGEN, sportjahr);
    }*/
//}
