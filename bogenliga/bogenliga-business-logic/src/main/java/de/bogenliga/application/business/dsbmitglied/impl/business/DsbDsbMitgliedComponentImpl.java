package de.bogenliga.application.business.dsbmitglied.impl.business;

import java.util.List;
import java.util.stream.Collectors;

import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.lizenz.impl.mapper.KampfrichterlizenzMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.MitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.dsbmitglied.impl.mapper.dsbMitgliedMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link DsbMitgliedComponent}
 */
@Component
public class DsbDsbMitgliedComponentImpl implements DsbMitgliedComponent {

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

    private final MitgliedDAO mitgliedDAO;
    private final LizenzDAO lizenzDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param mitgliedDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public DsbDsbMitgliedComponentImpl(final MitgliedDAO mitgliedDAO, final LizenzDAO lizenzDAO) {
        this.mitgliedDAO = mitgliedDAO;
        this.lizenzDAO = lizenzDAO;
    }


    @Override
    public List<DsbMitgliedDO> findAll() {
        final List<DsbMitgliedBE> dsbMitgliedBEList = mitgliedDAO.findAll();
        return dsbMitgliedBEList.stream().map(dsbMitgliedMapper.toDsbMitgliedDO).collect(Collectors.toList());
    }

    @Override
    public List<DsbMitgliedDO> findAllByTeamId(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_DSBMITGLIED_MANNSCHAFT_ID);
        final List<DsbMitgliedBE> dsbMitgliedBEList = mitgliedDAO.findAllByTeamId(id);
        return dsbMitgliedBEList.stream().map(dsbMitgliedMapper.toDsbMitgliedDO).collect(Collectors.toList());
    }


    @Override
    public DsbMitgliedDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);

        final DsbMitgliedBE result = mitgliedDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }
        DsbMitgliedDO dsbMitgliedDO = dsbMitgliedMapper.toDsbMitgliedDO.apply(result);
        dsbMitgliedDO.setKampfrichter(mitgliedDAO.hasKampfrichterLizenz(dsbMitgliedDO.getId()));
        return dsbMitgliedDO;
    }


    @Override
    public DsbMitgliedDO create(final DsbMitgliedDO dsbMitgliedDO, final long currentDsbMitgliedId) {
        checkDsbMitgliedDO(dsbMitgliedDO, currentDsbMitgliedId);

        final DsbMitgliedBE dsbMitgliedBE = dsbMitgliedMapper.toDsbMitgliedBE.apply(dsbMitgliedDO);
        final DsbMitgliedBE persistedDsbMitgliedBE = mitgliedDAO.create(dsbMitgliedBE, currentDsbMitgliedId);

        DsbMitgliedDO dsbMitgliedDOResponse = dsbMitgliedMapper.toDsbMitgliedDO.apply(persistedDsbMitgliedBE);

        if(dsbMitgliedDO.isKampfrichter()){
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

        final DsbMitgliedBE dsbMitgliedBE = dsbMitgliedMapper.toDsbMitgliedBE.apply(dsbMitgliedDO);

        final DsbMitgliedBE persistedDsbMitgliedBE = mitgliedDAO.update(dsbMitgliedBE, currentDsbMitgliedId);
        DsbMitgliedDO dsbMitgliedDOResponse = dsbMitgliedMapper.toDsbMitgliedDO.apply(persistedDsbMitgliedBE);

        if(dsbMitgliedDO.isKampfrichter() && !mitgliedDAO.hasKampfrichterLizenz(dsbMitgliedDOResponse.getId())){
            final LizenzBE lizenzBE = KampfrichterlizenzMapper.toKampfrichterlizenz.apply(dsbMitgliedDOResponse);
            lizenzDAO.create(lizenzBE, currentDsbMitgliedId);
        }else if(!dsbMitgliedDO.isKampfrichter() && mitgliedDAO.hasKampfrichterLizenz(dsbMitgliedDOResponse.getId())){
            lizenzDAO.delete(lizenzDAO.findKampfrichterLizenzByDsbMitgliedId(dsbMitgliedDOResponse.getId()),currentDsbMitgliedId);
        }
        return dsbMitgliedDOResponse;
    }


    @Override
    public void delete(final DsbMitgliedDO dsbMitgliedDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(dsbMitgliedDO, PRECONDITION_MSG_DSBMITGLIED);
        Preconditions.checkArgument(dsbMitgliedDO.getId() >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_CURRENT_DSBMITGLIED);
        if(mitgliedDAO.hasKampfrichterLizenz(dsbMitgliedDO.getId())){
            lizenzDAO.delete(lizenzDAO.findKampfrichterLizenzByDsbMitgliedId(dsbMitgliedDO.getId()),currentDsbMitgliedId);
        }
        final DsbMitgliedBE dsbMitgliedBE = dsbMitgliedMapper.toDsbMitgliedBE.apply(dsbMitgliedDO);
        mitgliedDAO.delete(dsbMitgliedBE, currentDsbMitgliedId);

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
