package de.bogenliga.application.business.ligapasse.dao;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.ligatabelle.impl.entity.LigatabelleBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class LigapasseDAO implements DataAccessObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(LigapasseDAO.class);

    //table name in the DB
    private static final String TABLE = "ligapasse";


    //business entity parameters
    private static final String MATCH_BE_WETTKAMPF_ID = "wettkampf_id";
    private static final String MATCH_BE_MATCH_ID = "match_id";
    private static final String MATCH_BE_PASSE_ID = "passe_id";
    private static final String MATCH_BE_PASSE_LFDNR = "passe_lfdnr";
    private static final String MATCH_BE_PASSE_MANSCHAFT_ID = "passe_manschaft_id";
    private static final String MATCH_BE_DSB_MITGLIED_ID = "dsb_mitglied_id";
    private static final String MATCH_BE_DSB_MITGLIED_NAME = "dsb_mitglied_name";
    private static final String MATCH_BE_MANNSCHAFTSMITGLIED_RUECKENNUMMER = "mannschaftsmitglied_rueckennummer";
    private static final String MATCH_BE_PASSE_RINGZAHL_PFEIL1 = "passe_ringzahl_pfeil1";
    private static final String MATCH_BE_PASSE_RINGZAHL_PFEIL2 = "passe_ringzahl_pfeil2";


    // table columns
    private static final String MATCH_TABLE_WETTKAMPF_ID = "ligapasse_wettkampf_id";
    private static final String MATCH_TABLE_MATCH_ID = "ligapasse_match_id";
    private static final String MATCH_TABLE_PASSE_ID = "ligapasse_passe_id";
    private static final String MATCH_TABLE_PASSE_LFDNR = "ligapasse_passe_lfdnr";
    private static final String MATCH_TABLE_PASSE_MANSCHAFT_ID = "ligapasse_passe_manschaft_id";
    private static final String MATCH_TABLE_DSB_MITGLIED_ID = "ligapasse_dsb_mitglied_id";
    private static final String MATCH_TABLE_DSB_MITGLIED_NAME = "ligapasse_dsb_mitglied_name";
    private static final String MATCH_TABLE_MANNSCHAFTSMITGLIED_RUECKENNUMMER = "ligapasse_mannschaftsmitglied_rueckennummer";
    private static final String MATCH_TABLE_PASSE_RINGZAHL_PFEIL1 = "ligapsse_passe_ringzahl_pfeil1";
    private static final String MATCH_TABLE_PASSE_RINGZAHL_PFEIL2 = "ligapasse_passe_ringzahl_pfeil2";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<LigatabelleBE> LIGAPASSE = new BusinessEntityConfiguration<>(
            LigatabelleBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private final BasicDAO BasicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public LigapasseDAO(final BasicDAO basicDao){this.BasicDao = basicDao;}


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();


        columnsToFieldsMap.put(MATCH_TABLE_WETTKAMPF_ID, MATCH_BE_WETTKAMPF_ID);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_ID, MATCH_BE_MATCH_ID);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_ID, MATCH_BE_PASSE_ID);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_LFDNR, MATCH_BE_PASSE_LFDNR);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_MANSCHAFT_ID, MATCH_BE_PASSE_MANSCHAFT_ID);
        columnsToFieldsMap.put(MATCH_TABLE_DSB_MITGLIED_ID, MATCH_BE_DSB_MITGLIED_ID);
        columnsToFieldsMap.put(MATCH_TABLE_DSB_MITGLIED_NAME, MATCH_BE_DSB_MITGLIED_NAME);
        columnsToFieldsMap.put(MATCH_TABLE_MANNSCHAFTSMITGLIED_RUECKENNUMMER, MATCH_BE_MANNSCHAFTSMITGLIED_RUECKENNUMMER);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_RINGZAHL_PFEIL1, MATCH_BE_PASSE_RINGZAHL_PFEIL1);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_RINGZAHL_PFEIL2, MATCH_BE_PASSE_RINGZAHL_PFEIL2);

        return columnsToFieldsMap;
    }


}
