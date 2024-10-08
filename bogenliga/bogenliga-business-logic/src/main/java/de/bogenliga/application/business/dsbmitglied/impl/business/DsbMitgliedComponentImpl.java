package de.bogenliga.application.business.dsbmitglied.impl.business;

import java.util.List;

import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedWithoutVereinsnameBE;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.lizenz.impl.mapper.KampfrichterlizenzMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.dsbmitglied.impl.mapper.DsbMitgliedMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link DsbMitgliedComponent}
 */
@Component
public class DsbMitgliedComponentImpl implements DsbMitgliedComponent {

    private static final String PRECONDITION_MSG_DSBMITGLIED = "DsbMitgliedDO must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_ID = "DsbMitgliedDO ID must not be negative";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VORNAME = "DsbMitglied vorname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NACHNAME = "DsbMitglied nachname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_GEBURTSDATUM = "DsbMitglied geburtsdatum must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NATIONALITAET = "DsbMitglied nationalitaet must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_MITGLIEDSNUMMER = "DsbMitglied mitgliedsnummer must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID = "DsbMitglied vereins id must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID_NEGATIVE = "DsbMitglied vereins id must not be negative";
    private static final String PRECONDITION_MSG_CURRENT_DSBMITGLIED = "Current dsbmitglied id must not be negative";
    private static final String PRECONDITION_MSG_DSBMITGLIED_MANNSCHAFT_ID = "Team id must not be negative";
    private static final String PRECONDITION_MSG_DSBMITGLIED_SEARCHTERM = "Search term must not be null";

    private final DsbMitgliedDAO dsbMitgliedDAO;
    private final LizenzDAO lizenzDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param dsbMitgliedDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public DsbMitgliedComponentImpl(final DsbMitgliedDAO dsbMitgliedDAO, final LizenzDAO lizenzDAO) {
        this.dsbMitgliedDAO = dsbMitgliedDAO;
        this.lizenzDAO = lizenzDAO;
    }


    @Override
    public List<DsbMitgliedDO> findAll() {
        final List<DsbMitgliedBE> dsbMitgliedBEList = dsbMitgliedDAO.findAll();
        return dsbMitgliedBEList.stream().map(DsbMitgliedMapper.toDsbMitgliedDO).toList();
    }


    @Override
    public List<DsbMitgliedDO> findAllByTeamId(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_DSBMITGLIED_MANNSCHAFT_ID);
        final List<DsbMitgliedBE> dsbMitgliedBEList = dsbMitgliedDAO.findAllByTeamId(id);
        return dsbMitgliedBEList.stream().map(DsbMitgliedMapper.toDsbMitgliedDO).toList();
    }


    @Override
    public List<DsbMitgliedDO> findAllNotInTeam(long id, long vereinId) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_DSBMITGLIED_MANNSCHAFT_ID);
        final List<DsbMitgliedBE> dsbMitgliedBEList = dsbMitgliedDAO.findAllNotInTeamId(id, vereinId);
        return dsbMitgliedBEList.stream().map(DsbMitgliedMapper.toDsbMitgliedDO).toList();
    }


    @Override
    public DsbMitgliedDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);

        final DsbMitgliedBE result = dsbMitgliedDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }
        DsbMitgliedDO dsbMitgliedDO = DsbMitgliedMapper.toDsbMitgliedDO.apply(result);
        dsbMitgliedDO.setKampfrichter(dsbMitgliedDAO.hasKampfrichterLizenz(dsbMitgliedDO.getId()));
        return dsbMitgliedDO;
    }


    @Override
    public List<DsbMitgliedDO> findBySearch(final String searchTerm) {
        Preconditions.checkNotNull(searchTerm, PRECONDITION_MSG_DSBMITGLIED_SEARCHTERM);

        final List<DsbMitgliedBE> dsbMitgliedBEList = dsbMitgliedDAO.findBySearch(searchTerm);
        return dsbMitgliedBEList.stream().map(DsbMitgliedMapper.toDsbMitgliedDO).toList();
    }

    /*
     * Die Funktion prüft, ob ein DsbMitglied eine Kampfrichterlizenz besitzt
     * und gibt true zurück wenn die Kampfrichterlizenz existiert.
     * @param id from DsbMitglied
     * @return boolean if dsbMitglied has a lizenz
     */
    @Override
    public Boolean hasKampfrichterLizenz(final long id) {
        // Id muss gültig sein und damit >= null sein.
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);
        return (dsbMitgliedDAO.hasKampfrichterLizenz(id) );
    }



    @Override
    public DsbMitgliedDO create(final DsbMitgliedDO dsbMitgliedDO, final long currentDsbMitgliedId) {
        checkDsbMitgliedDO(dsbMitgliedDO, currentDsbMitgliedId);

        final DsbMitgliedWithoutVereinsnameBE dsbMitgliedBE = DsbMitgliedMapper.toDsbMitgliedWithoutVereinsnameBE.apply(dsbMitgliedDO);
        final DsbMitgliedWithoutVereinsnameBE persistedDsbMitgliedBE = dsbMitgliedDAO.create(dsbMitgliedBE, currentDsbMitgliedId);

        DsbMitgliedDO dsbMitgliedDOResponse = DsbMitgliedMapper.toDsbMitgliedDOWithoutVereinsname.apply(persistedDsbMitgliedBE);

        if (Boolean.FALSE.equals(dsbMitgliedDO.isKampfrichter())) {
            final LizenzBE lizenzBE = KampfrichterlizenzMapper.toKampfrichterlizenz.apply(dsbMitgliedDOResponse);
            lizenzDAO.create(lizenzBE, currentDsbMitgliedId);
            dsbMitgliedDOResponse.setKampfrichter(true);
        }
        return dsbMitgliedDOResponse;
    }


    @Override
    public DsbMitgliedDO update(final DsbMitgliedDO dsbMitgliedDO, final long currentDsbMitgliedId) {
        checkDsbMitgliedDO(dsbMitgliedDO, currentDsbMitgliedId);
        Preconditions.checkArgument(dsbMitgliedDO.getId() >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);

        final DsbMitgliedWithoutVereinsnameBE dsbMitgliedBE = DsbMitgliedMapper.toDsbMitgliedWithoutVereinsnameBE.apply(dsbMitgliedDO);

        final DsbMitgliedWithoutVereinsnameBE persistedDsbMitgliedBE = dsbMitgliedDAO.update(dsbMitgliedBE, currentDsbMitgliedId);
        DsbMitgliedDO dsbMitgliedDOResponse = DsbMitgliedMapper.toDsbMitgliedDOWithoutVereinsname.apply(persistedDsbMitgliedBE);

        if (Boolean.FALSE.equals(dsbMitgliedDO.isKampfrichter()) && !Boolean.FALSE.equals(dsbMitgliedDAO.hasKampfrichterLizenz(dsbMitgliedDOResponse.getId()))) {
            final LizenzBE lizenzBE = KampfrichterlizenzMapper.toKampfrichterlizenz.apply(dsbMitgliedDOResponse);
            lizenzDAO.create(lizenzBE, currentDsbMitgliedId);
        } else if (Boolean.FALSE.equals(!dsbMitgliedDO.isKampfrichter()) && Boolean.FALSE.equals(dsbMitgliedDAO.hasKampfrichterLizenz(
                dsbMitgliedDOResponse.getId()))) {
            lizenzDAO.delete(lizenzDAO.findKampfrichterLizenzByDsbMitgliedId(dsbMitgliedDOResponse.getId()),
                    currentDsbMitgliedId);
        }
        return dsbMitgliedDOResponse;
    }


    @Override
    public void delete(final DsbMitgliedDO dsbMitgliedDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(dsbMitgliedDO, PRECONDITION_MSG_DSBMITGLIED);
        Preconditions.checkArgument(dsbMitgliedDO.getId() >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_CURRENT_DSBMITGLIED);

        if (Boolean.FALSE.equals(dsbMitgliedDAO.hasKampfrichterLizenz(dsbMitgliedDO.getId()))) {
            lizenzDAO.delete(lizenzDAO.findKampfrichterLizenzByDsbMitgliedId(dsbMitgliedDO.getId()),
                    currentDsbMitgliedId);
        }
        final DsbMitgliedWithoutVereinsnameBE dsbMitgliedBE = DsbMitgliedMapper.toDsbMitgliedWithoutVereinsnameBE.apply(dsbMitgliedDO);
        dsbMitgliedDAO.delete(dsbMitgliedBE, currentDsbMitgliedId);

    }


    private void checkDsbMitgliedDO(final DsbMitgliedDO dsbMitgliedDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(dsbMitgliedDO, PRECONDITION_MSG_DSBMITGLIED);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_CURRENT_DSBMITGLIED);
        Preconditions.checkNotNull(dsbMitgliedDO.getVorname(), PRECONDITION_MSG_DSBMITGLIED_VORNAME);
        Preconditions.checkNotNull(dsbMitgliedDO.getNachname(), PRECONDITION_MSG_DSBMITGLIED_NACHNAME);
        Preconditions.checkNotNull(dsbMitgliedDO.getGeburtsdatum(), PRECONDITION_MSG_DSBMITGLIED_GEBURTSDATUM);
        Preconditions.checkNotNull(dsbMitgliedDO.getNationalitaet(), PRECONDITION_MSG_DSBMITGLIED_NATIONALITAET);
        Preconditions.checkNotNull(dsbMitgliedDO.getMitgliedsnummer(), PRECONDITION_MSG_DSBMITGLIED_MITGLIEDSNUMMER);
        Preconditions.checkNotNull(dsbMitgliedDO.getVereinsId(), PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID);
        Preconditions.checkArgument(dsbMitgliedDO.getVereinsId() >= 0, PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID_NEGATIVE);
    }
}
