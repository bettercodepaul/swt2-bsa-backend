package de.bogenliga.application.business.ligamatch.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.ligatabelle.impl.dao.LigatabelleDAO;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class LigamatchDAO implements DataAccessObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(LigatabelleDAO.class);

    //table name in the DB
    private static final String TABLE = "ligamatch";


    //business entity parameters
    private static final String MATCH_BE_WETTKAMPF_ID = "wettkampfId";
    private static final String MATCH_BE_MATCH_ID = "matchId";
    private static final String MATCH_BE_MATCH_NR = "matchNr";
    private static final String MATCH_BE_MATCH_SCHEIBENNUMMER = "scheibennummer";
    private static final String MATCH_BE_MATCH_MANSCHAFT_ID = "mannschaftId";
//    private static final String MATCH_BE_MANNSCHAFT_NAME = "mannschaftName";
//    private static final String MATCH_BE_MANNSCHAFT_NAME_GEGNER = "mannschaftNameGegner";
//    private static final String MATCH_BE_MATCH_SCHEIBENNUMMER_GEGNER = "scheibennummerGegner";
//    private static final String MATCH_BE_MATCH_ID_GEGNER = "idGegner";
    private static final String MATCH_BE_NAECHSTE_MATCH_ID = "naechsteMatchId";
    private static final String MATCH_BE_NAECHSTE_NAECHSTE_MATCH_NR_MATCH_ID = "naechsteNaechsteMatchNrMatchId";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_1 = "strafpunkteSatz1";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_2 = "strafpunkteSatz2";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_3 = "strafpunkteSatz3";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_4 = "strafpunkteSatz4";
    private static final String MATCH_BE_MATCH_STRAFPUNKTE_SATZ_5 = "strafpunkteSatz5";
    private static final String MATCH_BE_BEGEGNUNG = "begegnung";


    // table columns
    private static final String MATCH_TABLE_WETTKAMPF_ID = "ligamatch_match_wettkampf_id";
    private static final String MATCH_TABLE_MATCH_ID = "ligamatch_match_id";
    private static final String MATCH_TABLE_MATCH_NR = "ligamatch_match_nr";
    private static final String MATCH_TABLE_MATCH_SCHEIBENNUMMER = "ligamatch_match_scheibennummer";
    private static final String MATCH_TABLE_MATCH_MANSCHAFT_ID = "ligamatch_match_mannschaft_id";
//    private static final String MATCH_TABLE_MANNSCHAFT_NAME = "ligamatch_mannschaft_name";
//    private static final String MATCH_TABLE_MANNSCHAFT_NAME_GEGNER = "ligamatch_mannschaft_name_gegner";
//    private static final String MATCH_TABLE_MATCH_SCHEIBENNUMMER_GEGNER = "ligamatch_match_scheibennummer_gegner";
//    private static final String MATCH_TABLE_MATCH_ID_GEGNER = "ligamatch_match_id_gegner";
    private static final String MATCH_TABLE_NAECHSTE_MATCH_ID = "ligamatch_naechste_match_id";
    private static final String MATCH_TABLE_NAECHSTE_NAECHSTE_MATCH_NR_MATCH_ID = "ligamatch_naechste_naechste_match_nr_match_id";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_1 = "ligamatch_match_strafpunkte_satz_1";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_2 = "ligamatch_match_strafpunkte_satz_2";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_3 = "ligamatch_match_strafpunkte_satz_3";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_4 = "ligamatch_match_strafpunkte_satz_4";
    private static final String MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_5 = "ligamatch_match_strafpunkte_satz_5";
    private static final String MATCH_TABLE_BEGEGNUNG = "ligamatch_begegnung";


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
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_NR, MATCH_BE_MATCH_NR);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_SCHEIBENNUMMER, MATCH_BE_MATCH_SCHEIBENNUMMER);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_MANSCHAFT_ID, MATCH_BE_MATCH_MANSCHAFT_ID);
//        columnsToFieldsMap.put(MATCH_TABLE_MANNSCHAFT_NAME, MATCH_TABLE_MANNSCHAFT_NAME);
//        columnsToFieldsMap.put(MATCH_TABLE_MANNSCHAFT_NAME_GEGNER, MATCH_BE_MANNSCHAFT_NAME_GEGNER);
//        columnsToFieldsMap.put(MATCH_TABLE_MATCH_SCHEIBENNUMMER_GEGNER, MATCH_BE_MATCH_SCHEIBENNUMMER_GEGNER);
//        columnsToFieldsMap.put(MATCH_TABLE_MATCH_ID_GEGNER, MATCH_BE_MATCH_ID_GEGNER);
        columnsToFieldsMap.put(MATCH_TABLE_NAECHSTE_MATCH_ID, MATCH_BE_NAECHSTE_MATCH_ID);
        columnsToFieldsMap.put(MATCH_TABLE_NAECHSTE_NAECHSTE_MATCH_NR_MATCH_ID, MATCH_BE_NAECHSTE_NAECHSTE_MATCH_NR_MATCH_ID);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_1, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_1);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_2, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_2);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_3, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_3);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_4, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_4);
        columnsToFieldsMap.put(MATCH_TABLE_MATCH_STRAFPUNKTE_SATZ_5, MATCH_BE_MATCH_STRAFPUNKTE_SATZ_5);
        columnsToFieldsMap.put(MATCH_TABLE_BEGEGNUNG, MATCH_BE_BEGEGNUNG);

        return columnsToFieldsMap;
    }

    public LigamatchBE findById(final Long ligamatchId){
        return basicDao.selectSingleEntity(LIGAMATCH, FIND_BY_MATCH_ID, ligamatchId);
    }

    //SQL Selects

    private static final String FIND_BY_MATCH_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(MATCH_TABLE_MATCH_ID)
            .orderBy(MATCH_TABLE_MATCH_ID)
            .compose().toString();

//    private static final String FIND_BY_MATCH_ID =
//            "SELECT ligamatch.*, match.match_begegnung FROM ligamatch" +
//            " LEFT JOIN  match" +
//            " ON ligamatch.ligamatch_match_id = match.match_id" +
//            " WHERE match_id = ?";





}
