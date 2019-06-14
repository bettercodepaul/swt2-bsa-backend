package de.bogenliga.application.business.lizenz.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.lizenz.api.LizenzComponent;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.dao.LizenzDAO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.lizenz.impl.mapper.LizenzMapper;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.mapper.MannschaftsmitgliedMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;


@Component
public class LizenzComponentImpl implements LizenzComponent {

    private static final String PRECONDITION_LIZENZ = "LizenzDO must not be null";
    private static final String PRECONDITION_CURRENT_USER_ID = "Current User ID must not be null";
    private static final String PRECONDITION_CURRENT_USER_ID_NEGATIVE = "Current User ID must not be negativ";

    private static final String PRECONDITION_LIZENZ_LIZENZTYP = "LizenzDO_Lizenztyp must not be null";
    private static final String PRECONDITION_LIZENZ_DSBMITGLIED = "LizenzDO_DSBMITGLIED must not be null";
    private static final String PRECONDITION_LIZENZ_ID = "LizenzDO_LizenzID must not be null";

    private static final String PRECONDITION_LIZENZ_LIZENZNUMMER = "LizenzDO_lizenznummer must not be null";
    private static final String PRECONDITION_LIZENZ_DISZIPLIN = "LizenzDO_Disziplin must not be null";
    private static final String PRECONDITION_LIZENZ_REGION = "LizenzDO_Region must not be null";


    private final LizenzDAO lizenzDAO;


    public void checkCurrentUserPreconditions(final Long id) {
        Preconditions.checkNotNull(id, PRECONDITION_CURRENT_USER_ID);
        Preconditions.checkArgument(id >= 0, PRECONDITION_CURRENT_USER_ID_NEGATIVE);
    }


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param lizenzDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public LizenzComponentImpl(final LizenzDAO lizenzDAO) {
        this.lizenzDAO = lizenzDAO;
    }


    @Override
    public List<LizenzDO> findAll() {
        final List<LizenzBE> lizenzBEList = this.lizenzDAO.findAll();
        return lizenzBEList.stream().map(LizenzMapper.toLizenzDO).collect(
                Collectors.toList());
    }


    @Override
    public List<LizenzDO> findByDsbMitgliedId(long id) {
        final List<LizenzBE> LizenzBEList = lizenzDAO.findByDsbMitgliedId(id);
        return LizenzBEList.stream().map(LizenzMapper.toLizenzDO).collect(
                Collectors.toList());

    }

    private void checkPrecondition(LizenzDO lizenzDO, long currentUserId) {
        Preconditions.checkNotNull(lizenzDO, PRECONDITION_LIZENZ);
        Preconditions.checkArgument(currentUserId >= 0, PRECONDITION_CURRENT_USER_ID);
        Preconditions.checkNotNull(lizenzDO.getLizenztyp(), PRECONDITION_LIZENZ_LIZENZTYP);
        Preconditions.checkNotNull(lizenzDO.getLizenzDsbMitgliedId(), PRECONDITION_LIZENZ_DSBMITGLIED);
        if(lizenzDO.getLizenztyp().equals("Liga")) {
            Preconditions.checkNotNull(lizenzDO.getLizenznummer(), PRECONDITION_LIZENZ_LIZENZNUMMER);
            Preconditions.checkNotNull(lizenzDO.getLizenzRegionId(), PRECONDITION_LIZENZ_REGION);
            Preconditions.checkNotNull(lizenzDO.getLizenzDisziplinId(), PRECONDITION_LIZENZ_DISZIPLIN);
        }
    }

    @Override
    public LizenzDO create(LizenzDO lizenzDO, long currentUserId) {

        this.checkCurrentUserPreconditions(currentUserId);
        this.checkPrecondition(lizenzDO, currentUserId);

        LizenzBE lizenzBE = lizenzDAO.create(LizenzMapper.toLizenzBE.apply(lizenzDO), currentUserId);
        return LizenzMapper.toLizenzDO.apply(lizenzBE);
    }


    @Override
    public LizenzDO update(LizenzDO lizenzDO, long currentUserId) {

        this.checkCurrentUserPreconditions(currentUserId);
        this.checkPrecondition(lizenzDO,currentUserId);


        LizenzBE lizenzBE = this.lizenzDAO.update(LizenzMapper.toLizenzBE.apply(lizenzDO), currentUserId);
        return LizenzMapper.toLizenzDO.apply(lizenzBE);
    }


    @Override
    public void delete(LizenzDO lizenzDO, long currentUserId) {

        Preconditions.checkNotNull(lizenzDO.getLizenzId(), PRECONDITION_LIZENZ_ID);
        this.checkCurrentUserPreconditions(currentUserId);

        LizenzBE lizenzBE = LizenzMapper.toLizenzBE.apply(lizenzDO);
        lizenzDAO.delete(lizenzBE, currentUserId);
    }

}
