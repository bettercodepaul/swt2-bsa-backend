package de.bogenliga.application.business.Passe.impl.dao;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * @author Kay Scheerer
 */
@Repository
public class PasseDAO implements DataAccessObject {

    //define logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(PasseDAO.class);

    //table name in the DB
    private static final String TABLE = "passe";


    //business entity parameters
    private static final String PASSE_BE_MANNSCHAFT_ID   =  "passeMannschaftId";
    private static final String PASSE_BE_WETTKAMPF_ID    =  "passeWettkampfId";
    private static final String PASSE_BE_MATCH_NR        =  "passeMatchNr";
    private static final String PASSE_BE_LFDNR          =   "passeLfdnr";
    private static final String PASSE_BE_DSB_MITGLIED_ID =  "passeDsbMitgliedId";
    private static final String PASSE_BE_PFEIL_1   = "passeRingzahlPfeil1";
    private static final String PASSE_BE_PFEIL_2   = "passeRingzahlPfeil2";
    private static final String PASSE_BE_PFEIL_3   = "passeRingzahlPfeil3";
    private static final String PASSE_BE_PFEIL_4   = "passeRingzahlPfeil4";
    private static final String PASSE_BE_PFEIL_5   = "passeRingzahlPfeil5";
    private static final String PASSE_BE_PFEIL_6   = "passeRingzahlPfeil6";

    private static final String PASSE_TABLE_MANNSCHAFT_ID     = "passe_mannschaft_id";
    private static final String PASSE_TABLE_WETTKAMPF_ID      = "passe_wettkampf_id";
    private static final String PASSE_TABLE_MATCH_NR          = "passe_match_nr";
    private static final String PASSE_TABLE_LFDNR            =  "passe_lfdnr";
    private static final String PASSE_TABLE_DSB_MITGLIED_ID   = "passe_dsb_mitglied_id";

    private static final String PASSE_TABLE_PFEIL_1   = "passe_ringzahl_pfeil1";
    private static final String PASSE_TABLE_PFEIL_2   = "passe_ringzahl_pfeil2";
    private static final String PASSE_TABLE_PFEIL_3   = "passe_ringzahl_pfeil3";
    private static final String PASSE_TABLE_PFEIL_4   = "passe_ringzahl_pfeil4";
    private static final String PASSE_TABLE_PFEIL_5   = "passe_ringzahl_pfeil5";
    private static final String PASSE_TABLE_PFEIL_6   = "passe_ringzahl_pfeil6";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<PasseBE> PASSE = new BusinessEntityConfiguration<>(
            PasseBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public PasseDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(PASSE_TABLE_MANNSCHAFT_ID, PASSE_BE_MANNSCHAFT_ID);
        columnsToFieldsMap.put(PASSE_TABLE_WETTKAMPF_ID, PASSE_BE_WETTKAMPF_ID);
        columnsToFieldsMap.put(PASSE_TABLE_MATCH_NR, PASSE_BE_MATCH_NR);
        columnsToFieldsMap.put(PASSE_TABLE_LFDNR, PASSE_BE_LFDNR);
        columnsToFieldsMap.put(PASSE_TABLE_DSB_MITGLIED_ID, PASSE_BE_DSB_MITGLIED_ID);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_1, PASSE_BE_PFEIL_1);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_2, PASSE_BE_PFEIL_2);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_3, PASSE_BE_PFEIL_3);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_4, PASSE_BE_PFEIL_4);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_5, PASSE_BE_PFEIL_5);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_6, PASSE_BE_PFEIL_6);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    /**
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM "+TABLE
                    + " ORDER BY "+ PASSE_BE_MATCH_NR
                    + "=?";

    private static final String FIND_BY_MANNSCHAFT_ID =
            "SELECT * "
                    + " FROM "+TABLE
                    + " WHERE "+PASSE_BE_MANNSCHAFT_ID
                    + "=?";

    private static final String FIND_BY_WETTKAMPF_ID =
            "SELECT * "
                    + " FROM "+TABLE
                    + " WHERE "+PASSE_BE_WETTKAMPF_ID
                    + "=?";

    private static final String FIND_BY_MATCHNR =
            "SELECT * "
                    + " FROM "+TABLE
                    + " WHERE MATCH_NR "
                    +"= ?";



    /**
     * Create a new kampfrichter entry
     *
     * @param passeBE
     * @param currentKampfrichterUserId
     * @return Business Entity corresponding to the created kampfrichter entry (should only be creatable by Kampfrichter entities
     */
    public PasseBE create(final PasseBE passeBE, final long currentKampfrichterUserId) {
        basicDao.setCreationAttributes(passeBE, currentKampfrichterUserId);

        return basicDao.insertEntity(PASSE, passeBE);
    }

}
