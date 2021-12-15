package de.bogenliga.application.business.ligapasse.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.ligapasse.impl.entity.LigapasseBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 *
 * DataAccessObject for the ligapasse view
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 */

@Repository
public class LigapasseDAO implements DataAccessObject {

    //define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(LigapasseDAO.class);

    //table name in the DB
    private static final String TABLE = "ligapasse";

    //business entity parameter names
    private static final String MATCH_BE_WETTKAMPF_ID = "wettkampfId";
    private static final String MATCH_BE_MATCH_ID = "matchId";
    private static final String MATCH_BE_PASSE_ID = "passeId";
    private static final String MATCH_BE_PASSE_LFDNR = "passeLfdnr";
    private static final String MATCH_BE_PASSE_MANNSCHAFT_ID = "passeMannschaftId";
    private static final String MATCH_BE_DSB_MITGLIED_ID = "dsbMitgliedId";
    private static final String MATCH_BE_DSB_MITGLIED_NAME = "dsbMitgliedName";
    private static final String MATCH_BE_MANNSCHAFTSMITGLIED_RUECKENNUMMER = "mannschaftsmitglied_Rueckennummer";
    private static final String MATCH_BE_PASSE_RINGZAHL_PFEIL1 = "passeRingzahlPfeil1";
    private static final String MATCH_BE_PASSE_RINGZAHL_PFEIL2 = "passeRingzahlPfeil2";
    private static final String MATCH_BE_PASSE_MATCH_NR = "passeMatchNr";


    // table column names
    private static final String MATCH_TABLE_WETTKAMPF_ID = "ligapasse_wettkampf_id";
    private static final String MATCH_TABLE_MATCH_ID = "ligapasse_match_id";
    private static final String MATCH_TABLE_PASSE_ID = "ligapasse_passe_id";
    private static final String MATCH_TABLE_PASSE_LFDNR = "ligapasse_passe_lfdnr";
    private static final String MATCH_TABLE_PASSE_MANNSCHAFT_ID = "ligapasse_passe_mannschaft_id";
    private static final String MATCH_TABLE_DSB_MITGLIED_ID = "ligapasse_dsb_mitglied_id";
    private static final String MATCH_TABLE_DSB_MITGLIED_NAME = "ligapasse_dsb_mitglied_name";
    private static final String MATCH_TABLE_MANNSCHAFTSMITGLIED_RUECKENNUMMER = "ligapasse_mannschaftsmitglied_rueckennummer";
    private static final String MATCH_TABLE_PASSE_RINGZAHL_PFEIL1 = "ligapsse_passe_ringzahl_pfeil1";
    private static final String MATCH_TABLE_PASSE_RINGZAHL_PFEIL2 = "ligapasse_passe_ringzahl_pfeil2";
    private static final String MATCH_TABLE_PASSE_MATCH_NR = "ligapasse_passe_match_nr";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<LigapasseBE> LIGAPASSE = new BusinessEntityConfiguration<>(
            LigapasseBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */

    @Autowired
    public LigapasseDAO(final BasicDAO basicDao){this.basicDao = basicDao;}


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();


        columnsToFieldsMap.put(MATCH_TABLE_WETTKAMPF_ID, MATCH_BE_WETTKAMPF_ID);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_ID, MATCH_BE_MATCH_ID);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_ID, MATCH_BE_PASSE_ID);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_LFDNR, MATCH_BE_PASSE_LFDNR);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_MANNSCHAFT_ID, MATCH_BE_PASSE_MANNSCHAFT_ID);
        columnsToFieldsMap.put(MATCH_TABLE_DSB_MITGLIED_ID, MATCH_BE_DSB_MITGLIED_ID);
        columnsToFieldsMap.put(MATCH_TABLE_DSB_MITGLIED_NAME, MATCH_BE_DSB_MITGLIED_NAME);
        columnsToFieldsMap.put(MATCH_TABLE_MANNSCHAFTSMITGLIED_RUECKENNUMMER, MATCH_BE_MANNSCHAFTSMITGLIED_RUECKENNUMMER);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_RINGZAHL_PFEIL1, MATCH_BE_PASSE_RINGZAHL_PFEIL1);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_RINGZAHL_PFEIL2, MATCH_BE_PASSE_RINGZAHL_PFEIL2);
        columnsToFieldsMap.put(MATCH_TABLE_PASSE_MATCH_NR, MATCH_BE_PASSE_MATCH_NR);

        return columnsToFieldsMap;
    }


    /**
     *
     * @param ligapasseID
     * @return a list of ligapassen with the given param ligapasseID
     */
    public List<LigapasseBE> findLigapassenByLigamatchId(Long ligapasseID){
        return this.basicDao.selectEntityList(LIGAPASSE, FIND_BY_LIGAPASSE_ID, ligapasseID);

    }

    //SQL Selects
    private static final String FIND_BY_LIGAPASSE_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(MATCH_TABLE_MATCH_ID)
            .orderBy(MATCH_TABLE_PASSE_LFDNR)
            .compose().toString();





}
