package de.bogenliga.application.business.wettkampfanzeige.impl.dao;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.wettkampfanzeige.impl.entity.WettkampfAnzeigeBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;

/**
 * DAO for accessing the `anzeigen` table.
 * @author Mira Dietschmann, mira.dietschmann@student.reutlingen-university.de
 */
@Repository
public class WettkampfAnzeigeDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(WettkampfAnzeigeDAO.class);

    // Database table name.
    private static final String TABLE = "anzeigen";

    //
    private static final String WETTKAMPF_ANZEIGE_BE_ANZEIGEN_ID = "anzeigenId";
    private static final String WETTKAMPF_ANZEIGE_BE_PHYSISCHE_BILDSCHIRM_ID = "physischeBildschirmId";
    private static final String WETTKAMPF_ANZEIGE_BE_TABLE_TYP = "tableTyp";
    private static final String WETTKAMPF_ANZEIGE_BE_VERANSTALTUNG_ID = "veranstaltungId";

    private static final String WETTKAMPF_ANZEIGE_TABLE_ANZEIGEN_ID = "anzeigen_id";
    private static final String WETTKAMPF_ANZEIGE_TABLE_PHYSISCHE_BILDSCHIRM_ID = "physische_bildschirm_id";
    private static final String WETTKAMPF_ANZEIGE_TABLE_TABLE_TYP = "table_typ";
    private static final String WETTKAMPF_ANZEIGE_TABLE_VERANSTALTUNG_ID = "veranstaltung_id";

    private static final BusinessEntityConfiguration<WettkampfAnzeigeBE> WETTKAMPF_ANZEIGE = new BusinessEntityConfiguration<>(
            WettkampfAnzeigeBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL Queries
     */
    private final String DOES_PHYSISCHE_BILDSCHIRM_ID_EXIST = "";

    private final BasicDAO basicDao;

    @Autowired
    public WettkampfAnzeigeDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(WETTKAMPF_ANZEIGE_TABLE_ANZEIGEN_ID, WETTKAMPF_ANZEIGE_BE_ANZEIGEN_ID);
        columnsToFieldsMap.put(WETTKAMPF_ANZEIGE_TABLE_PHYSISCHE_BILDSCHIRM_ID, WETTKAMPF_ANZEIGE_BE_PHYSISCHE_BILDSCHIRM_ID);
        columnsToFieldsMap.put(WETTKAMPF_ANZEIGE_TABLE_TABLE_TYP, WETTKAMPF_ANZEIGE_BE_TABLE_TYP);
        columnsToFieldsMap.put(WETTKAMPF_ANZEIGE_TABLE_VERANSTALTUNG_ID, WETTKAMPF_ANZEIGE_BE_VERANSTALTUNG_ID);

        // add technical columns
        columnsToFieldsMap.putAll(de.bogenliga.application.common.component.dao.BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }
}
