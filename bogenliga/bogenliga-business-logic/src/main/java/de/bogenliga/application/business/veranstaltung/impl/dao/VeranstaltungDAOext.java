package de.bogenliga.application.business.veranstaltung.impl.dao;


import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBEext;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungPhase;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import java.util.List;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class VeranstaltungDAOext implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(VeranstaltungDAOext.class);

    // table name in the database
    private static final String TABLE = "veranstaltung";
    // business entity parameter names

    private static final String VERANSTALTUNG_BE_ID = "veranstaltungId";
    private static final String VERANSTALTUNG_BE_WETTKAMPFTYP_ID= "veranstaltungWettkampftypId";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_NAME= "veranstaltungName";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_SPORTJAHR = "veranstaltungSportjahr";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_MELDEDEADLINE = "veranstaltungMeldedeadline";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_LIGALEITER_ID= "veranstaltungLigaleiterId";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_LIGA_ID = "veranstaltungLigaId";

    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_PHASE = "veranstaltungPhase";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_GROESSE = "veranstaltungGroesse";

    private static final String VERANSTALTUNG_TABLE_ID = "veranstaltung_id";
    private static final String VERANSTALTUNG_TABLE_WETTKAMPFTYP_ID= "veranstaltung_wettkampftyp_id";
    private static final String VERANSTALTUNG_TABLE_NAME= "veranstaltung_name";
    private static final String VERANSTALTUNG_TABLE_SPORTJAHR = "veranstaltung_sportjahr";
    private static final String VERANSTALTUNG_TABLE_MELDEDEADLINE = "veranstaltung_meldedeadline";
    private static final String VERANSTALTUNG_TABLE_LIGALEITER_ID= "veranstaltung_ligaleiter_id";
    private static final String VERANSTALTUNG_TABLE_LIGA_ID = "veranstaltung_liga_id";

    private static final String VERANSTALTUNG_TABLE_PHASE = "veranstaltung_phase";
    private static final String VERANSTALTUNG_TABLE_GROESSE = "veranstaltung_groesse";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<VeranstaltungBEext> VERANSTALTUNG_EXTENDED = new BusinessEntityConfiguration<>(
            VeranstaltungBEext.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_EVERYTHING =
            "SELECT " +
                    "    v.*, " +
                    "    l.liga_name AS liga_name, " +
                    "    w.wettkampftyp_name AS wettkampftyp_name, " +
                    "    b.benutzer_email AS ligaleiter_email" +
                    "FROM " +
                    "    veranstaltung v " +
                    "LEFT JOIN " +
                    "    liga l ON v.veranstaltung_liga_id = l.liga_id " +
                    "LEFT JOIN " +
                    "    wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id " +
                    "LEFT JOIN " +
                    "    benutzer b ON v.veranstaltung_ligaleiter_id = b.benutzer_id " +
                    "ORDER BY " +
                    "    v.veranstaltung_id";

    private static final String FIND_ALL_PHASE =
            "SELECT " +
                    "    v.*, " +
                    "    l.liga_name AS liga_name, " +
                    "    w.wettkampftyp_name AS wettkampftyp_name, " +
                    "    b.benutzer_email AS ligaleiter_email" +
                    "FROM " +
                    "    veranstaltung v " +
                    "LEFT JOIN " +
                    "    liga l ON v.veranstaltung_liga_id = l.liga_id " +
                    "LEFT JOIN " +
                    "    wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id " +
                    "LEFT JOIN " +
                    "    benutzer b ON v.veranstaltung_ligaleiter_id = b.benutzer_id " +
                    "WHERE " +
                    "    v.veranstaltung_phase IN (?, ?) " +
                    "ORDER BY " +
                    "    v.veranstaltung_id";

    private static final String FIND_BY_SPORTJAHR =
            "SELECT v.*,"
                        +" l.liga_name AS ligaName,"
                        +" l.liga_verantwortlich AS ligaLeiterId,"
                        +" b.benutzer_email AS ligaLeiterEmail "
                    +"FROM "
                        +"veranstaltung v "
                    +"LEFT JOIN "
                        +"liga l ON v.veranstaltung_liga_id = l.liga_id "
                    +"LEFT JOIN "
                        +"wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id "
                    +"LEFT JOIN "
                        +"benutzer b ON l.liga_verantwortlich = b.benutzer_id "
                    +"WHERE "
                        +"v.veranstaltung_sportjahr = ?;";

    private static final String FIND_BY_SPORTJAHR_EINE_PHASE =
            "SELECT " +
                    "    v.*, " +
                    "    l.liga_name AS liga_name, " +
                    "    w.wettkampftyp_name AS wettkampftyp_name, " +
                    "b.benutzer_email AS ligaLeiterEmail " +
                    "FROM " +
                    "    veranstaltung v " +
                    "LEFT JOIN " +
                    "    liga l ON v.veranstaltung_liga_id = l.liga_id " +
                    "LEFT JOIN " +
                    "    wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id " +
                    "LEFT JOIN " +
                    "    benutzer b ON v.veranstaltung_ligaleiter_id = b.benutzer_id " +
                    "WHERE " +
                    "    v.veranstaltung_sportjahr = ? AND v.veranstaltung_phase = ?";

    private static final String FIND_BY_SPORTJAHR_ZWEI_PHASEN =
            "SELECT " +
                    "    v.*, " +
                    "    l.liga_name AS liga_name, " +
                    "    w.wettkampftyp_name AS wettkampftyp_name, " +
                    "b.benutzer_email AS ligaLeiterEmail " +
                    "FROM " +
                    "    veranstaltung v " +
                    "LEFT JOIN " +
                    "    liga l ON v.veranstaltung_liga_id = l.liga_id " +
                    "LEFT JOIN " +
                    "    wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id " +
                    "LEFT JOIN " +
                    "    benutzer b ON v.veranstaltung_ligaleiter_id = b.benutzer_id " +
                    "WHERE " +
                    "    v.veranstaltung_sportjahr = ? AND v.veranstaltung_phase IN (?, ?)";

    private static final String FIND_BY_SPORTJAHR_SORTED_DISTINCT_LIGA =
            "SELECT " +
                    "    v.veranstaltung_liga_id, " +
                    "    v.veranstaltung_name, " +
                    "    v.veranstaltung_id, " +
                    "    v.veranstaltung_sportjahr, " +
                    "    l.liga_name, " +
                    "    w.wettkampftyp_name, " +
                    "b.benutzer_email AS ligaLeiterEmail, " +
                    "    MAX(m.last_modified_at_utc) AS last_modified_at " +
                    "FROM " +
                    "    veranstaltung v " +
                    "LEFT JOIN " +
                    "    liga l ON v.veranstaltung_liga_id = l.liga_id " +
                    "LEFT JOIN " +
                    "    wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id " +
                    "LEFT JOIN " +
                    "    ligatabelle lt ON v.veranstaltung_id = lt.ligatabelle_veranstaltung_id " +
                    "LEFT JOIN " +
                    "    match m ON lt.ligatabelle_mannschaft_id = m.match_mannschaft_id " +
                    "LEFT JOIN " +
                    "    benutzer b ON v.veranstaltung_ligaleiter_id = b.benutzer_id " +
                    "WHERE " +
                    "    v.veranstaltung_sportjahr = ? " +
                    "    AND v.veranstaltung_phase IN (2, 3) " +
                    "GROUP BY " +
                    "    v.veranstaltung_liga_id, " +
                    "    v.veranstaltung_name, " +
                    "    v.veranstaltung_id, " +
                    "    v.veranstaltung_sportjahr, " +
                    "    l.liga_name, " +
                    "    w.wettkampftyp_name, " +
                    "    b.benutzer_id " +
                    "ORDER BY " +
                    "    MAX(m.last_modified_at_utc) DESC NULLS LAST, " +
                    "    v.veranstaltung_id";

    private static final String FIND_BY_LIGALEITER_ID =
            "SELECT v.*,"
                    +" l.liga_name AS ligaName,"
                    +" l.liga_verantwortlich AS ligaLeiterId,"
                    +" b.benutzer_email AS ligaLeiterEmail "
                    +"FROM "
                    +"veranstaltung v "
                    +"LEFT JOIN "
                    +"liga l ON v.veranstaltung_liga_id = l.liga_id "
                    +"LEFT JOIN "
                    +"wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id "
                    +"LEFT JOIN "
                    +"benutzer b ON l.liga_verantwortlich = b.benutzer_id "
                    + " WHERE veranstaltung_ligaleiter_id = ?";

    private static final String FIND_BY_LIGAID =
            "SELECT v.*,"
                    +" l.liga_name AS ligaName,"
                    +" l.liga_verantwortlich AS ligaLeiterId,"
                    +" b.benutzer_email AS ligaLeiterEmail "
                    +"FROM "
                    +"veranstaltung v "
                    +"LEFT JOIN "
                    +"liga l ON v.veranstaltung_liga_id = l.liga_id "
                    +"LEFT JOIN "
                    +"wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id "
                    +"LEFT JOIN "
                    +"benutzer b ON l.liga_verantwortlich = b.benutzer_id "
                    + "WHERE veranstaltung_liga_id = ?";

    private static final String FIND_BY_ID =
            "SELECT v.*,"
                    +" l.liga_name AS ligaName,"
                    +" l.liga_verantwortlich AS ligaLeiterId,"
                    +" b.benutzer_email AS ligaLeiterEmail "
                    +"FROM "
                    +"veranstaltung v "
                    +"LEFT JOIN "
                    +"liga l ON v.veranstaltung_liga_id = l.liga_id "
                    +"LEFT JOIN "
                    +"wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id "
                    +"LEFT JOIN "
                    +"benutzer b ON l.liga_verantwortlich = b.benutzer_id "
                    + " WHERE veranstaltung_id = ?";

    private static final String FIND_BY_LIGAID_AND_SPORTJAHR =
            "SELECT v.*,"
                    +" l.liga_name AS ligaName,"
                    +" l.liga_verantwortlich AS ligaLeiterId,"
                    +" b.benutzer_email AS ligaLeiterEmail "
                    +"FROM "
                    +"veranstaltung v "
                    +"LEFT JOIN "
                    +"liga l ON v.veranstaltung_liga_id = l.liga_id "
                    +"LEFT JOIN "
                    +"wettkampftyp w ON v.veranstaltung_wettkampftyp_id = w.wettkampftyp_id "
                    +"LEFT JOIN "
                    +"benutzer b ON l.liga_verantwortlich = b.benutzer_id "
                    + " WHERE veranstaltung_liga_id = ? AND veranstaltung_sportjahr = ?";

    private final BasicDAO basicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public VeranstaltungDAOext(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private int[] phaseEnumToInteger(VeranstaltungPhase.Phase[] phaseList) {
        int[] phaseListInteger = new int[phaseList.length];
        VeranstaltungPhase veranstaltungPhase = new VeranstaltungPhase();
        for (int index = 0; index < phaseList.length; index++) {
            phaseListInteger[index] = veranstaltungPhase.getPhaseAsInt(phaseList[index]);
        }
        return phaseListInteger;
    }

    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_ID, VERANSTALTUNG_BE_ID);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_WETTKAMPFTYP_ID, VERANSTALTUNG_BE_WETTKAMPFTYP_ID);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_NAME, VERANSTALTUNG_BE_VERANSTALTUNG_NAME);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_SPORTJAHR, VERANSTALTUNG_BE_VERANSTALTUNG_SPORTJAHR);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_MELDEDEADLINE, VERANSTALTUNG_BE_VERANSTALTUNG_MELDEDEADLINE);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_LIGALEITER_ID, VERANSTALTUNG_BE_VERANSTALTUNG_LIGALEITER_ID);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_LIGA_ID, VERANSTALTUNG_BE_VERANSTALTUNG_LIGA_ID);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_PHASE, VERANSTALTUNG_BE_VERANSTALTUNG_PHASE);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_GROESSE, VERANSTALTUNG_BE_VERANSTALTUNG_GROESSE);
        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    public List<VeranstaltungBEext> findEverything(VeranstaltungPhase.Phase[] phaseList) {
        int[] phaseListInt = phaseEnumToInteger(phaseList);
        List<VeranstaltungBEext> veranstaltungList;
        switch (phaseList.length) {
            case 0:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_EVERYTHING);
                break;
            case 2:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_ALL_PHASE, phaseListInt[0],
                        phaseListInt[1]);
                break;
            default:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_EVERYTHING);
                break;
        }
        return veranstaltungList;
    }

    public List<VeranstaltungBEext> findBySportjahrDestinct(long sportjahr) {
        return basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_BY_SPORTJAHR_SORTED_DISTINCT_LIGA, sportjahr);
    }
    /**
     * Return Veranstaltungen with the same Sportjahr specified by the amount of passed phases (even none passed
     * phases)
     *
     * @param sportjahr required
     * @param phaseList required
     */
    public List<VeranstaltungBEext> findBySportjahr(final long sportjahr, VeranstaltungPhase.Phase[] phaseList) {

        int[] phaseListInt = phaseEnumToInteger(phaseList);
        List<VeranstaltungBEext> veranstaltungList;
        switch (phaseList.length) {
            case 0:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_BY_SPORTJAHR, sportjahr);
                break;
            case 1:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_BY_SPORTJAHR_EINE_PHASE, sportjahr,
                        phaseListInt[0]);
                break;
            case 2:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_BY_SPORTJAHR_ZWEI_PHASEN, sportjahr,
                        phaseListInt[0], phaseListInt[1]);
                break;
            default:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_BY_SPORTJAHR, sportjahr);
                break;
        }
        return veranstaltungList;
    }


    public List<VeranstaltungBEext> findByLigaleiterId(long ligaleiterId) {
        return basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_BY_LIGALEITER_ID, ligaleiterId);
    }

    public List<VeranstaltungBEext> findByLigaID(long ligaID) {
        return basicDao.selectEntityList(VERANSTALTUNG_EXTENDED, FIND_BY_LIGAID, ligaID);
    }

    public VeranstaltungBEext findById(final long id) {
        return basicDao.selectSingleEntity(VERANSTALTUNG_EXTENDED, FIND_BY_ID, id);
    }

    public VeranstaltungBEext findByLigaIdAndSportjahr(final long ligaId, final long sportjahr) {
        return basicDao.selectSingleEntity(VERANSTALTUNG_EXTENDED, FIND_BY_LIGAID_AND_SPORTJAHR, ligaId, sportjahr);
    }
}
