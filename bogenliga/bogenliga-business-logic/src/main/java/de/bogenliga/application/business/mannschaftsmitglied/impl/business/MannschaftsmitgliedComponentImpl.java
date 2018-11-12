package de.bogenliga.application.business.mannschaftsmitglied.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.dsbmitglied.impl.mapper.DsbMitgliedMapper;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.mapper.MannschaftsmitgliedMapper;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class MannschaftsmitgliedComponentImpl implements MannschaftsmitgliedComponent {

    private static final String PRECONDITION_MANNSCHAFTSMITGLIED = "MannschaftsmitgliedDO must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID = "MannschaftsmitgliedDO_Mannschaft_ID must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID = "MannschaftsmitgliedDO_Mitglied_ID must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_EINGESETZT = "MannschaftsmitgliedDO_Mitglied_Eingesetzt must not be null";

    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV = "MannschaftsmitgliedDO_Mannschaft_ID must not be negativ";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV = "MannschaftsmitgliedDO_Mitglied_ID must not be negativ";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_EINGESETZT_NEGATIV = "MannschaftsmitgliedDO_Mitglied_Eingesetzt must not be negativ";


    private final MannschaftsmitgliedDAO mannschaftsmitgliedDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param MannschaftsmitgliedDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public MannschaftsmitgliedComponentImpl(final MannschaftsmitgliedDAO mannschaftsmitgliedDAO) { this.mannschaftsmitgliedDAO = mannschaftsmitgliedDAO; }


    @Override
    public List<MannschaftsmitgliedDO> findAll() {
        return null;
    }


    @Override
    public List<MannschaftsmitgliedDO> findAllSchuetze() {
        return null;
    }


    @Override
    public MannschaftsmitgliedDO findById(long mannschaftId, long mitgliedId) {
        return null;
    }


    @Override
    public DsbMitgliedDO create(MannschaftsmitgliedDO mannschaftsmitgliedDO,
                                long currentMannschaftsmitgliedMannschaftId,
                                long currentMannschaftsmitgliedMitgliedId) {
        return null;
    }


    @Override
    public DsbMitgliedDO update(MannschaftsmitgliedDO mannschaftsmitgliedDO,
                                long currentMannschaftsmitgliedMannschaftId,
                                long currentMannschaftsmitgliedMitgliedId) {
        return null;
    }


    @Override
    public void delete(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMannschaftsmitgliedMannschaftId,
                       long currentMannschaftsmitgliedMitgliedId) {

    }
}
