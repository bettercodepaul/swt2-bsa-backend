package de.bogenliga.application.business.ligatabelle.impl.dao;

import de.bogenliga.application.business.ligatabelle.impl.entity.LigatabelleBE;
import de.bogenliga.application.business.role.impl.entity.RoleBE;
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
 * DataAccessObject for the ligatabelle entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class LigatabelleDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOG = LoggerFactory.getLogger(LigatabelleDAO.class);

    // table name in the database
    private static final String TABLE = "ligatabelle";
    // business entity parameter names

    private static final String VERANSTALTUNGID_BE = "veranstaltungId";
    private static final String WETTKAMPFTAG_BE = "wettkampfTag";
    private static final String MANNSCHAFTID_BE = "mannschaftId";
    private static final String MANNSCHAFTNUMMER_BE = "mannschaftNummer";
    private static final String TABELLENPLATZ_BE = "tabellenplatz";
    private static final String MATCHPKT_BE = "matchpkt";
    private static final String MATCHPKTGEGEN_BE = "matchpkt_gegen";
    private static final String SATZPKT_BE = "satzpkt";
    private static final String SATZPKTGEGEN_BE = "satzpkt_gegen";
    private static final String VEREINID_BE = "verein_id";
    private static final String VEREINNAME_BE = "verein_name";

    private static final String VERANSTALTUNGID_TABLE = "ligatabelle_verantsaltung_id";
    private static final String WETTKAMPFTAG_TABLE = "ligatabelle_wettkampf_tag";
    private static final String MANNSCHAFTID_TABLE = "ligatabelle_mannschaft_id";
    private static final String MANNSCHAFTNUMMER_TABLE = "mannschaft_Nummer";
    private static final String TABELLENPLATZ_TABLE = "tabellenplatz";
    private static final String MATCHPKT_TABLE = "matchpkt";
    private static final String MATCHPKTGEGEN_TABLE = "matchpkt_gegen";
    private static final String SATZPKT_TABLE = "satzpkt";
    private static final String SATZPKTGEGEN_TABLE = "satzpkt_gegen";
    private static final String VEREINID_TABLE = "verein_id";
    private static final String VEREINNAME_TABLE = "verein_name";

    /*
     * SQL queries
     */
    private static final String GET_LIGATABELLE =
            "SELECT lt.ligatabelle_tabellenplatz, " +
                    "lt.ligatabelle_wettkampftag ," +
                    "lt.ligatabelle_mannschaft_id, " +
                    "ms.mannschaft_nummer, " +
                    "lt.ligatabelle_tabellenplatz, " +
                    "lt.ligatabelle_matchpkt, " +
                    "lt.ligatabelle_matchpkt_gegen, " +
                    "lt.ligatabelle_satzpkt, " +
                    "lt.ligatabelle_satzpkt_gegen, " +
                    "v.verein_id, " +
                    "v.verein_name"
                    + " FROM ligatabelle as lt"
                    + " INNER JOIN mannschaft AS ms ON lt.veranstaltung_id = ms.mannschaft_veranstaltung_id"
                    + " INNER JOIN verein AS v ON v.verein_id = ms.mannschaft_verein_id"
                    + " INNER JOIN ligatabelle lt ON ms.mannschaft_id = lt.ligatabelle_mannschaft_id"
                    + " INNER JOIN wettkampf AS wk ON vs.veranstaltung_id = wk.wettkampf_veranstaltung_id"
                    + " WHERE wk.wettkampf_id = ?"
                    + " AND lt.ligatabelle_mannschaft_id = ms.mannschaft_id"
                    + " AND lt.ligatabelle_veranstaltung_id = wk.wettkampf_veranstaltung_id"
                    + " ORDER BY lt.ligatabelle_tabellenplatz";




    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<LigatabelleBE> LIGATABELLE = new BusinessEntityConfiguration<>(
            LigatabelleBE.class, TABLE, getColumnsToFieldsMap(), LOG);


    /*
     * SQL queries
     */

    private static final String FIND_BY_WETTKAMPF_ID =
            "SELECT * "
                    + " FROM rolle "
                    + " WHERE upper(rolle_name) = upper(?)";



    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public LigatabelleDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(ROLE_TABLE_ID, ROLE_BE_ID);
        columnsToFieldsMap.put(ROLE_TABLE_NAME, ROLE_NAME);

        return columnsToFieldsMap;
    }

    /**
     * Return all role entries
     */
    public List<RoleBE> findAll() {
        return basicDao.selectEntityList(ROLE, FIND_ALL);
    }

    /**
     * Return user entry with specific email adress
     *
     * @param roleName - role name to search
     */
    public RoleBE findByName(final String roleName) {
        return basicDao.selectSingleEntity(ROLE, FIND_BY_NAME, roleName);
    }





}
