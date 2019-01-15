package de.bogenliga.application.business.competitionclass.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.competitionclass.impl.entity.CompetitionClassBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * Data access object for the klasse entity in the database
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */

@Repository
public class CompetitionClassDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionClassDAO.class);

    // table name in the db
    private static final String TABLE = "klasse";

    // business entity parameters
    private static final String COMPETITIONCLASS_BE_ID = "klasseId";
    private static final String COMPETITIONCLASS_BE_NAME = "klasseName";
    private static final String COMPETITIONCLASS_BE_ALTER_MIN = "klasseAlterMin";
    private static final String COMPETITIONCLASS_BE_ALTER_MAX = "klasseAlterMax";
    private static final String COMPETITIONCLASS_BE_KLASSE_NR = "klasseNr";

    private static final String COMPETITIONCLASS_TABLE_ID = "klasse_id";
    private static final String COMPETITIONCLASS_TABLE_NAME = "klasse_name";
    private static final String COMPETITIONCLASS_TABLE_ALTER_MIN = "klasse_alter_min";
    private static final String COMPETITIONCLASS_TABLE_ALTER_MAX = "klasse_alter_max";
    private static final String COMPETITIONCLASS_TABLE_KLASSE_NR = "klasse_nr";


    private static final BusinessEntityConfiguration<CompetitionClassBE> COMPETITIONCLASS = new BusinessEntityConfiguration<>(
            CompetitionClassBE.class, TABLE, getColumsToFieldsMap(), LOGGER);

    /**
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * " + " FROM klasse" +  " ORDER BY klasse_nr";

    private static final String FIND_BY_ID =
            "SELECT * " + " FROM klasse" + " WHERE klasse_id = ?";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public CompetitionClassDAO(final BasicDAO basicDao) { this.basicDao = basicDao; }

    private static Map<String, String> getColumsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(COMPETITIONCLASS_TABLE_ID, COMPETITIONCLASS_BE_ID);
        columnsToFieldsMap.put(COMPETITIONCLASS_TABLE_NAME, COMPETITIONCLASS_BE_NAME);
        columnsToFieldsMap.put(COMPETITIONCLASS_TABLE_ALTER_MIN, COMPETITIONCLASS_BE_ALTER_MIN);
        columnsToFieldsMap.put(COMPETITIONCLASS_TABLE_ALTER_MAX, COMPETITIONCLASS_BE_ALTER_MAX);
        columnsToFieldsMap.put(COMPETITIONCLASS_TABLE_KLASSE_NR, COMPETITIONCLASS_BE_KLASSE_NR);

        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all competition class entries
     * @return List with Klasse Business Entities
     */
    public List<CompetitionClassBE> findAll() { return basicDao.selectEntityList(COMPETITIONCLASS, FIND_ALL); }


    /**
     * Return a competition class matching the id
     * @param id of competition class to be returned
     * @return a single competition class
     */
    public CompetitionClassBE findById(final long id) {
        return basicDao.selectSingleEntity(COMPETITIONCLASS, FIND_BY_ID, id);
    }


    /**
     * Create a new competitionClass entry
     *
     * @param competitionClassBE
     * @param currentClassId
     * @return
     */
    public CompetitionClassBE create(final CompetitionClassBE competitionClassBE, final long currentClassId){
        basicDao.setCreationAttributes(competitionClassBE, currentClassId);

        return basicDao.insertEntity(COMPETITIONCLASS, competitionClassBE);
    }

    /**
     * Update an existing Competition Class entry
     *
     * @param competitionClassBE
     * @param currentClassId
     *
     */
    public CompetitionClassBE update(final CompetitionClassBE competitionClassBE, final long currentClassId){
        basicDao.setModificationAttributes(competitionClassBE,currentClassId);

        return basicDao.updateEntity(COMPETITIONCLASS,competitionClassBE,COMPETITIONCLASS_BE_ID);
    }
}