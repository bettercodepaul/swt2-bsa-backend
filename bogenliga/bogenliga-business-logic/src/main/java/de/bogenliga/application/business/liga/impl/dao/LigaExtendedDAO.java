package de.bogenliga.application.business.liga.impl.dao;

import de.bogenliga.application.business.liga.impl.entity.LigaExtendedBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataAccessObject extension for advanced Liga operations.
 */
@Repository
public class LigaExtendedDAO implements DataAccessObject {

    // Define logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(LigaExtendedDAO.class);

    // Table name in the database
    private static final String TABLE = "liga";

    // Business entity parameter names
    private static final String LIGA_BE_ID = "ligaId";
    private static final String LIGA_BE_NAME = "ligaName";
    private static final String LIGA_BE_DISZIPLIN_ID = "ligaDisziplinId";
    private static final String LIGA_BE_REGION_ID = "ligaRegionId";
    private static final String LIGA_BE_UEBERGEORDNET_ID = "ligaUebergeordnetId";
    private static final String LIGA_BE_VERANTWORTLICH_ID = "ligaVerantwortlichId";
    private static final String LIGA_BE_LIGADETAIL = "ligaDetail";
    private static final String LIGA_BE_LIGAFILEBASE64 = "ligaFileBase64";
    private static final String LIGA_BE_LIGAFILENAME = "ligaFileName";
    private static final String LIGA_BE_LIGAFILETYPE = "ligaFileType";

    private static final String LIGA_TABLE_ID = "liga_id";
    private static final String LIGA_TABLE_NAME = "liga_name";
    private static final String LIGA_TABLE_DISZIPLIN_ID = "liga_disziplin_id";
    private static final String LIGA_TABLE_REGION_ID = "liga_region_id";
    private static final String LIGA_TABLE_UEBERGEORDNET = "liga_uebergeordnet";
    private static final String LIGA_TABLE_VERANTWORTLICH = "liga_verantwortlich";
    private static final String LIGA_TABLE_DETAIL = "liga_detail";
    private static final String LIGA_TABLE_FILE_BASE64 = "liga_file_base64";
    private static final String LIGA_TABLE_FILE_NAME = "liga_file_name";
    private static final String LIGA_TABLE_FILE_TYPE = "liga_file_type";

    // Business Entity Configuration
    private static final BusinessEntityConfiguration<LigaExtendedBE> LIGA_EXTENDED = new BusinessEntityConfiguration<>(
        LigaExtendedBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
    * SQL queries for extended Liga operations
    */
    private static final String FIND_EVERYTHING =
            " SELECT "
                    + " l.*, "
                    + " loe.liga_name AS uebergeordneteLigaName, "
                    + " vp.benutzer_email AS verantwortlicherName, "
                    + " d.disziplin_name AS disziplinName, "
                    + " r.region_name AS regionName "
                    + " FROM "
                    + " liga l "
                    + " LEFT JOIN "
                    + " liga loe ON l.liga_uebergeordnet = loe.liga_id "
                    + " LEFT JOIN "
                    + " benutzer vp ON l.liga_verantwortlich = vp.benutzer_id "
                    + " LEFT JOIN "
                    + " disziplin d ON l.liga_disziplin_id = d.disziplin_id "
                    + " LEFT JOIN "
                    + " region r ON l.liga_region_id = r.region_id "
                    + " ORDER BY liga_id; ";

    private static final String FIND_BY_SEARCH =
            " SELECT "
                    + " l.*, "
                    + " loe.liga_name AS uebergeordneteLigaName, "
                    + " vp.benutzer_email AS verantwortlicherName, "
                    + " d.disziplin_name AS disziplinName, "
                    + " r.region_name AS regionName "
                    + " FROM "
                    + " liga l "
                    + " LEFT JOIN "
                    + " liga loe ON l.liga_uebergeordnet = loe.liga_id "
                    + " LEFT JOIN "
                    + " benutzer vp ON l.liga_verantwortlich = vp.benutzer_id "
                    + " LEFT JOIN "
                    + " disziplin d ON l.liga_disziplin_id = d.disziplin_id "
                    + " LEFT JOIN "
                    + " region r ON l.liga_region_id = r.region_id "
                    + " WHERE "
                    + " LOWER(l.liga_name) LIKE (?) "
                    + " ORDER BY "
                    + " l.liga_id; ";

    private static final String FIND_ADDITIONAL_DATA_BY_LIGAID =
            " SELECT "
                    + " loe.liga_name AS uebergeordneteLigaName, "
                    + " r.region_name AS regionName, "
                    + " u.benutzer_email AS verantwortlicherName, "
                    + " d.disziplin_name AS disziplinName "
                    + " FROM "
                    + " liga l "
                    + " LEFT JOIN "
                    + " liga loe ON l.liga_uebergeordnet = loe.liga_id "
                    + " LEFT JOIN "
                    + " region r ON l.liga_region_id = r.region_id "
                    + " LEFT JOIN "
                    + " benutzer u ON l.liga_verantwortlich = u.benutzer_id "
                    + " LEFT JOIN "
                    + " disziplin d ON l.liga_disziplin_id = d.disziplin_id "
                    + " WHERE "
                    + " l.liga_id = ? ";


    private final BasicDAO basicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     * */
    @Autowired
    public LigaExtendedDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    /**
     * Return the mapping of table columns to entity fields.
     */
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(LIGA_TABLE_ID, LIGA_BE_ID);
        columnsToFieldsMap.put(LIGA_TABLE_DISZIPLIN_ID, LIGA_BE_DISZIPLIN_ID);
        columnsToFieldsMap.put(LIGA_TABLE_NAME, LIGA_BE_NAME);
        columnsToFieldsMap.put(LIGA_TABLE_REGION_ID, LIGA_BE_REGION_ID);
        columnsToFieldsMap.put(LIGA_TABLE_UEBERGEORDNET, LIGA_BE_UEBERGEORDNET_ID);
        columnsToFieldsMap.put(LIGA_TABLE_VERANTWORTLICH, LIGA_BE_VERANTWORTLICH_ID);
        columnsToFieldsMap.put(LIGA_TABLE_DETAIL, LIGA_BE_LIGADETAIL);
        columnsToFieldsMap.put(LIGA_TABLE_FILE_BASE64, LIGA_BE_LIGAFILEBASE64);
        columnsToFieldsMap.put(LIGA_TABLE_FILE_NAME, LIGA_BE_LIGAFILENAME);
        columnsToFieldsMap.put(LIGA_TABLE_FILE_TYPE, LIGA_BE_LIGAFILETYPE);

        // Add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    /**
     * Fetch extended Liga information with details.
     */
    public List<LigaExtendedBE> findEverything() {
        return basicDao.selectEntityList(LIGA_EXTENDED, FIND_EVERYTHING);
    }

    public List<LigaExtendedBE> findBySearch(final String searchTerm){
        return basicDao.selectEntityList(LIGA_EXTENDED, FIND_BY_SEARCH, new StringBuilder()
                                                                            .append("%")
                                                                            .append(searchTerm.toLowerCase())
                                                                            .append("%")
                                                                            .toString());
    }

    public LigaExtendedBE findAdditionalDataByLigaId(final long ligaID) {
        return basicDao.selectSingleEntity(LIGA_EXTENDED, FIND_ADDITIONAL_DATA_BY_LIGAID, ligaID);
    }
}


