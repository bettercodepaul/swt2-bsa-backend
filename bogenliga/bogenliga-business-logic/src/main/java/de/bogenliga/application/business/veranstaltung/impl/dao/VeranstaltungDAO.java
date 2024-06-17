package de.bogenliga.application.business.veranstaltung.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungPhase;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 *@author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
@Repository
public class VeranstaltungDAO implements DataAccessObject{

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

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(VeranstaltungDAO.class);

    // table name in the database
    private static final String TABLE = "veranstaltung";
    // business entity parameter names



    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<VeranstaltungBE> VERANSTALTUNG= new BusinessEntityConfiguration<>(
            VeranstaltungBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL_PHASE =
            "SELECT * "
                    + " FROM veranstaltung"
                    + " WHERE veranstaltung_phase IN (?, ?)"
                    + " ORDER BY veranstaltung.veranstaltung_id";

    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM veranstaltung"
                    + " ORDER BY veranstaltung.veranstaltung_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM veranstaltung "
                    + " WHERE veranstaltung_id = ?";

    private static final String FIND_BY_LIGAID_AND_SPORTJAHR =
            "SELECT * "
                    + " FROM veranstaltung "
                    + " WHERE veranstaltung_liga_id = ? AND veranstaltung_sportjahr = ?";

    private static final String FIND_BY_LIGALEITER_ID =
            "SELECT * "
                    + " FROM veranstaltung "
                    + " WHERE veranstaltung_ligaleiter_id = ?";

    private static final String FIND_BY_SPORTJAHR =
            "SELECT * "
                    + "FROM veranstaltung "
                    + "WHERE veranstaltung_sportjahr = ? ";

    private static final String FIND_BY_SPORTJAHR_EINE_PHASE =
            "SELECT * "
                    + "FROM veranstaltung "
                    + "WHERE veranstaltung_sportjahr = ? AND veranstaltung_phase = ?";

    private static final String FIND_BY_SPORTJAHR_ZWEI_PHASEN =
            "SELECT * "
                    + "FROM veranstaltung "
                    + "WHERE veranstaltung_sportjahr = ? AND veranstaltung_phase in (?, ?)";

    private static final String FIND_ALL_SPORTJAHR_DESTINCT =
            "SELECT veranstaltung_sportjahr, min(veranstaltung_id) as veranstaltung_id, min(version) as version "
                    + "FROM veranstaltung "
                    + "GROUP BY veranstaltung_sportjahr "
                    + "ORDER BY veranstaltung_sportjahr DESC";


    private static final String FIND_BY_LIGAID =
            "SELECT * "
                    + "FROM veranstaltung "
                    + "WHERE veranstaltung_liga_id = ?";

    //query do a lookup inligatabelle if Daten available and order by last modification from match and veranstaltungs-Id
    private static final String FIND_BY_SPORTJAHR_SORTED_DESTINCT_LIGA =
            "SELECT veranstaltung_liga_id, veranstaltung_name, veranstaltung_id, veranstaltung_sportjahr "
                    + "FROM liga, veranstaltung,ligatabelle, match "
                    + "WHERE (veranstaltung_id = ligatabelle_veranstaltung_id AND veranstaltung_sportjahr= ? AND ligatabelle_mannschaft_id = match_mannschaft_id AND veranstaltung_phase IN (2, 3)) "
                    + "GROUP BY veranstaltung_id "
                    + "ORDER BY max(match.last_modified_at_utc) DESC NULLS LAST, veranstaltung_id ";


    private final BasicDAO basicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public VeranstaltungDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
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


    /**
     * Return all Veranstaltung entries from the database specified by the phases attributes
     */
    public List<VeranstaltungBE> findAll(VeranstaltungPhase.Phase[] phaseList) {
        int[] phaseListInt = phaseEnumToInteger(phaseList);
        List<VeranstaltungBE> veranstaltungList;
        switch (phaseList.length) {
            case 0:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG, FIND_ALL);
                break;
            case 2:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG, FIND_ALL_PHASE, phaseListInt[0],
                        phaseListInt[1]);
                break;
            default:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG, FIND_ALL);
                break;
        }
        return veranstaltungList;
    }

    /**
     * Return all Veranstaltung entries from the database
     */
    /*public List<VeranstaltungBE> findAll()
    {
        return basicDao.selectEntityList(VERANSTALTUNG, FIND_ALL);
    }*/


    /**
     * Return Veranstaltung entry with specific id
     *
     * @param id - selected ID of Veranstaltung you want to recieve
     */
    public VeranstaltungBE findById(final long id) {
        return basicDao.selectSingleEntity(VERANSTALTUNG, FIND_BY_ID, id);
    }

    /**
     * Return Veranstaltung entry with specific liga id and sport year
     *
     * @param ligaId - selected liga ID of Veranstaltung you want to recieve
     * @param sportjahr - selected sport year of Veranstaltung you want to recieve
     */
    public VeranstaltungBE findByLigaIdAndSportjahr(final long ligaId, final long sportjahr) {
        return basicDao.selectSingleEntity(VERANSTALTUNG, FIND_BY_LIGAID_AND_SPORTJAHR, ligaId, sportjahr);
    }


    /**
     * Return Veranstaltungen with the same Sportjahr specified by the amount of passed phases (even none passed
     * phases)
     *
     * @param sportjahr
     */
    public List<VeranstaltungBE> findBySportjahr(final long sportjahr, VeranstaltungPhase.Phase[] phaseList) {

        int[] phaseListInt = phaseEnumToInteger(phaseList);
        List<VeranstaltungBE> veranstaltungList;
        switch (phaseList.length) {
            case 0:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG, FIND_BY_SPORTJAHR, sportjahr);
                break;
            case 1:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG, FIND_BY_SPORTJAHR_EINE_PHASE, sportjahr,
                        phaseListInt[0]);
                break;
            case 2:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG, FIND_BY_SPORTJAHR_ZWEI_PHASEN, sportjahr,
                        phaseListInt[0], phaseListInt[1]);
                break;
            default:
                veranstaltungList = basicDao.selectEntityList(VERANSTALTUNG, FIND_BY_SPORTJAHR, sportjahr);
                break;
        }
        return veranstaltungList;
    }


    public List<VeranstaltungBE> findByLigaID(long ligaID) {
        return basicDao.selectEntityList(VERANSTALTUNG, FIND_BY_LIGAID, ligaID);
    }


    public List<VeranstaltungBE> findByLigaleiterId(long ligaleiterId) {
        return basicDao.selectEntityList(VERANSTALTUNG, FIND_BY_LIGALEITER_ID, ligaleiterId);
    }


    /**
     * find all sportyears destinct returns a Long list with sportyears
     */
    public List<SportjahrDO> findAllSportjahreDestinct() {
        List<VeranstaltungBE> veranstaltungen = basicDao.selectEntityList(VERANSTALTUNG, FIND_ALL_SPORTJAHR_DESTINCT);
        ArrayList<SportjahrDO> sportjahre = new ArrayList<>();
        for (int i = 0; i < veranstaltungen.size(); i++) {
            sportjahre.add(new SportjahrDO(veranstaltungen.get(i).getVeranstaltungId(),
                    veranstaltungen.get(i).getVeranstaltungSportjahr(),
                    veranstaltungen.get(i).getVersion()));
        }
        return sportjahre;

    }



    /**
     * This method get a Enum Phase-Array and returns an array of integer. That is needed, because the phase in the
     * backend is an enum. The phase in the database is an integer. To select veranstaltungen in the database, int
     * values are needed.
     *
     * @param phaseList
     *
     * @return phaseListInteger
     */
    private int[] phaseEnumToInteger(VeranstaltungPhase.Phase[] phaseList) {
        int[] phaseListInteger = new int[phaseList.length];
        VeranstaltungPhase veranstaltungPhase = new VeranstaltungPhase();
        for (int index = 0; index < phaseList.length; index++) {
            phaseListInteger[index] = veranstaltungPhase.getPhaseAsInt(phaseList[index]);
        }
        return phaseListInteger;
    }

    /**
     * The sql query filters also for (veranstaltungen) with the phase 'Laufend' and 'Abgeschlossen'.
     *
     * @param sportjahr
     *
     * @return
     */
    public List<VeranstaltungBE> findBySportjahrDestinct(long sportjahr) {
        return basicDao.selectEntityList(VERANSTALTUNG, FIND_BY_SPORTJAHR_SORTED_DESTINCT_LIGA, sportjahr);
    }


    /**
     * Delete existing veranstaltung entrycreated_at_utc
     *
     * @param veranstaltungBE
     * @param currentDsbMitgliedId
     */
    public void delete(final VeranstaltungBE veranstaltungBE, final long currentDsbMitgliedId) {
        basicDao.setModificationAttributes(veranstaltungBE, currentDsbMitgliedId);

        basicDao.deleteEntity(VERANSTALTUNG, veranstaltungBE, VERANSTALTUNG_BE_ID);
    }

    /**
     * Update an existing veranstaltung entry
     *
     * @param veranstaltungBE
     * @param currentDsbMitgliedId
     * @return Business Entity corresponding to the updated dsbmitglied entry
     */
    public VeranstaltungBE update(final VeranstaltungBE veranstaltungBE, final long currentDsbMitgliedId) {
        basicDao.setModificationAttributes(veranstaltungBE, currentDsbMitgliedId);

        return basicDao.updateEntity(VERANSTALTUNG, veranstaltungBE, VERANSTALTUNG_BE_ID);
    }

    /**
     * Create a new veranstaltung entry
     *
     * @param veranstaltungBE
     * @param currentDsbMitgliedId
     * @return Business Entity corresponding to the created dsbmitglied entry
     */
    public VeranstaltungBE create(final VeranstaltungBE veranstaltungBE, final long currentDsbMitgliedId) {
        basicDao.setCreationAttributes(veranstaltungBE, currentDsbMitgliedId);

        return basicDao.insertEntity(VERANSTALTUNG, veranstaltungBE);
    }

}
