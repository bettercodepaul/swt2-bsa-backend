package de.bogenliga.application.business.ligamatch.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 *
 * DataAccessObject for the ligamatch view
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 */
@Repository
public class LigamatchDAO implements DataAccessObject {

    //define logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(LigamatchDAO.class);

    //table name in the DB
    private static final String TABLE = "ligamatch";


    //business entity parameters
    private static final String MATCH_BE_WETTKAMPF_ID = "wettkampfId";
    private static final String MATCH_BE_MATCH_ID = "matchId";
    private static final String MATCH_BE_MATCH_ID_GEGNER = "matchIdGegner";
    private static final String MATCH_BE_MATCH_NR = "matchNr";
    private static final String MATCH_BE_MATCH_SCHEIBENNUMMER = "matchScheibennummer";
    private static final String MATCH_BE_MATCH_SCHEIBENNUMMER_GEGNER = "scheibennummerGegner";
    private static final String MATCH_BE_MATCH_MANSCHAFT_ID = "mannschaftId";
    private static final String MATCH_BE_BEGEGNUNG = "begegnung";
    private static final String MATCH_BE_NAECHSTE_MATCH_ID = "naechsteMatchId";
    private static final String MATCH_BE_NAECHSTE_NAECHSTE_MATCH_NR_MATCH_ID = "naechsteNaechsteMatchId";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_1 = "strafpunkteSatz1";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_2 = "strafpunkteSatz2";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_3 = "strafpunkteSatz3";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_4 = "strafpunkteSatz4";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_5 = "strafpunkteSatz5";
    private static final String MATCH_BE_WETTKAMPFTYP_ID = "wettkampftypId";
    private static final String MATCH_BE_WETTKAMPF_TAG = "wettkampfTag";
    private static final String MATCH_BE_MANNSCHAFT_NAME = "mannschaftName";
    private static final String MATCH_BE_MANNSCHAFT_NAME_GEGNER = "mannschaftNameGegner";
    private static final String MATCH_BE_SATZPUNKTE = "satzpunkte";
    private static final String MATCH_BE_MATCHPUNKTE = "matchpunkte";


    // table columns
    private static final String MATCH_TABLE_WETTKAMPF_ID = "ligamatch_match_wettkampf_id";
    private static final String MATCH_TABLE_MATCH_ID = "ligamatch_match_id";
    private static final String MATCH_TABLE_MATCH_NR = "ligamatch_match_nr";
    private static final String MATCH_TABLE_MATCH_SCHEIBENNUMMER = "ligamatch_match_scheibennummer";
    private static final String MATCH_TABLE_MATCH_MANSCHAFT_ID = "ligamatch_match_mannschaft_id";
    private static final String MATCH_TABLE_MANNSCHAFT_NAME = "ligamatch_mannschaft_name";
    private static final String MATCH_TABLE_MANNSCHAFT_NAME_GEGNER = "ligamatch_mannschaft__name_gegner";
    private static final String MATCH_TABLE_MATCH_SCHEIBENNUMMER_GEGNER = "ligamatch_match_scheibennummer_gegner";
    private static final String MATCH_TABLE_MATCH_ID_GEGNER = "ligamatch_match_id_gegner";
    private static final String MATCH_TABLE_NAECHSTE_MATCH_ID = "ligamatch_naechste_match_id";
    private static final String MATCH_TABLE_NAECHSTE_NAECHSTE_MATCH_NR_MATCH_ID = "ligamatch_naechste_naechste_match_nr_match_id";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_1 = "ligamatch_match_strafpunkte_satz_1";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_2 = "ligamatch_match_strafpunkte_satz_2";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_3 = "ligamatch_match_strafpunkte_satz_3";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_4 = "ligamatch_match_strafpunkte_satz_4";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_5 = "ligamatch_match_strafpunkte_satz_5";
    private static final String MATCH_TABLE_BEGEGNUNG = "ligamatch_begegnung";
    private static final String MATCH_TABLE_WETTKAMPFTYP_ID = "ligamatch_wettkampftyp_id";
    private static final String MATCH_TABLE_WETTKAMPF_TAG = "ligamatch_wettkampf_tag";
    private static final String MATCH_TABLE_SATZPUNKTE = "ligamatch_satzpunkte";
    private static final String MATCH_TABLE_MATCHPUNKTE = "ligamatch_matchpunkte";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<LigamatchBE> LIGAMATCH = new BusinessEntityConfiguration<>(
            LigamatchBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);


    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public LigamatchDAO(final BasicDAO basicDao) {this.basicDao = basicDao;}



    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();


        columnsToFieldsMap.put(MATCH_TABLE_WETTKAMPF_ID, MATCH_BE_WETTKAMPF_ID);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_ID, MATCH_BE_MATCH_ID);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_ID_GEGNER, MATCH_BE_MATCH_ID_GEGNER);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_NR, MATCH_BE_MATCH_NR);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_SCHEIBENNUMMER, MATCH_BE_MATCH_SCHEIBENNUMMER);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_SCHEIBENNUMMER_GEGNER, MATCH_BE_MATCH_SCHEIBENNUMMER_GEGNER);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_MANSCHAFT_ID, MATCH_BE_MATCH_MANSCHAFT_ID);
        columnsToFieldsMap.put(MATCH_TABLE_NAECHSTE_MATCH_ID, MATCH_BE_NAECHSTE_MATCH_ID);
        columnsToFieldsMap.put(MATCH_TABLE_NAECHSTE_NAECHSTE_MATCH_NR_MATCH_ID, MATCH_BE_NAECHSTE_NAECHSTE_MATCH_NR_MATCH_ID);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_1, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_1);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_2, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_2);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_3, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_3);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_4, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_4);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_5, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_5);
        columnsToFieldsMap.put(MATCH_TABLE_BEGEGNUNG, MATCH_BE_BEGEGNUNG);
        columnsToFieldsMap.put(MATCH_TABLE_WETTKAMPFTYP_ID, MATCH_BE_WETTKAMPFTYP_ID);
        columnsToFieldsMap.put(MATCH_TABLE_WETTKAMPF_TAG, MATCH_BE_WETTKAMPF_TAG);
        columnsToFieldsMap.put(MATCH_TABLE_MANNSCHAFT_NAME, MATCH_BE_MANNSCHAFT_NAME);
        columnsToFieldsMap.put(MATCH_TABLE_MANNSCHAFT_NAME_GEGNER, MATCH_BE_MANNSCHAFT_NAME_GEGNER);
        columnsToFieldsMap.put(MATCH_TABLE_SATZPUNKTE, MATCH_BE_SATZPUNKTE);
        columnsToFieldsMap.put(MATCH_TABLE_MATCHPUNKTE, MATCH_BE_MATCHPUNKTE);

        return columnsToFieldsMap;
    }


    /**
     *
     * @param ligamatchId
     * @return a Ligamatch found by its Id from the view ligamatch
     */
    public LigamatchBE findById(final Long ligamatchId){
        return basicDao.selectSingleEntity(LIGAMATCH, FIND_BY_MATCH_ID, ligamatchId);
    }

    public List<LigamatchBE> findLigamatchesByWettkampfId(Long wettkampfId) {
        return basicDao.selectEntityList(LIGAMATCH, FIND_LIGAMATCHES_BY_WETTKAMPF_ID, wettkampfId);
    }

    public Boolean checkIfLigamatch(Long id){
        try{
            basicDao.selectEntityList(LIGAMATCH, FIND_BY_WETTKAMPF_ID, id);
            return true;
        }catch (Exception e){
            return false;
        }
    }




    //SQL Selects
    private static final String FIND_BY_MATCH_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(MATCH_TABLE_MATCH_ID)
            .orderBy(MATCH_TABLE_MATCH_ID)
            .compose().toString();

    private static final String FIND_LIGAMATCHES_BY_WETTKAMPF_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(MATCH_TABLE_WETTKAMPF_ID)
            .orderBy(MATCH_TABLE_WETTKAMPF_ID)
            .compose().toString();


    private static final String FIND_BY_WETTKAMPF_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(MATCH_TABLE_WETTKAMPF_ID)
            .compose().toString();


}
