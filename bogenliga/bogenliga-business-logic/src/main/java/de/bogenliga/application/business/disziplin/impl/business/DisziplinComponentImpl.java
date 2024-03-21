package de.bogenliga.application.business.disziplin.impl.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.disziplin.impl.dao.DisziplinDAO;
import de.bogenliga.application.business.disziplin.impl.entity.DisziplinBE;
import de.bogenliga.application.business.disziplin.impl.mapper.DisziplinMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * @author Marcel Neumann
 * @author Robin Mueller
 */
@Component
public class DisziplinComponentImpl implements DisziplinComponent {

    private static final String PRECONDITION_MSG_TEMPLATE = "Disziplin: %s must not be null and must not be negative";
    public static final String PRECONDITION_MSG_DISZIPLIN_DO = String.format(PRECONDITION_MSG_TEMPLATE, "DO");
    public static final String PRECONDITION_MSG_DISZIPLIN_NR = String.format(PRECONDITION_MSG_TEMPLATE, "nr");
    public static final String PRECONDITION_MSG_CURRENT_DISZIPLIN_ID = String.format(PRECONDITION_MSG_TEMPLATE,
            "disziplinID");
    public static final String PRECONDITION_MSG_CURRENT_DISZIPLIN_NAME = String.format(PRECONDITION_MSG_TEMPLATE,
            "disziplinName");

    /**
     * (Kay Scheerer)
     * this method would make the preconditions way easier to check before each SQL
     */
    private static final String PRECONDITION_MSG_TEMPLATE_NULL = "Passe: %s must not be null";
    private static final String PRECONDITION_MSG_TEMPLATE_NEGATIVE = "Passe: %s must not be negative";

    private final DisziplinDAO disziplinDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param disziplinDAO to access the database and return match representations
     */
    @Autowired
    public DisziplinComponentImpl(final DisziplinDAO disziplinDAO) {
        this.disziplinDAO = disziplinDAO;
    }


    public void checkPreconditions(final Long id, String iDIdentifier) {
        Preconditions.checkNotNull(id, String.format(PRECONDITION_MSG_TEMPLATE_NULL, iDIdentifier));
        Preconditions.checkArgument(id >= 0, String.format(PRECONDITION_MSG_TEMPLATE_NEGATIVE, iDIdentifier));
    }


    @Override
    public List<DisziplinDO> findAll() {
        final List<DisziplinBE> disziplinBEList = disziplinDAO.findAll();
        return disziplinBEList.stream().map(DisziplinMapper.toDisziplinDO).toList();
    }


    @Override
    public DisziplinDO findById(Long id) {
        checkPreconditions(id,PRECONDITION_MSG_DISZIPLIN_NR);

        final DisziplinBE disziplinBE = disziplinDAO.findById(id);

        if (disziplinBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found for ID '%s'", id));
        }

        return DisziplinMapper.toDisziplinDO.apply(disziplinBE);
    }


    @Override
    public DisziplinDO create(DisziplinDO disziplinDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_MSG_CURRENT_DISZIPLIN_ID);

        this.checkDisziplin(disziplinDO);

        DisziplinBE disziplinBE = disziplinDAO.create(DisziplinMapper.toDisziplinBE.apply(disziplinDO), currentUserId);
        return DisziplinMapper.toDisziplinDO.apply(disziplinBE);
    }


    @Override
    public DisziplinDO update(DisziplinDO disziplinDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_MSG_CURRENT_DISZIPLIN_ID);

        this.checkDisziplin(disziplinDO);

        DisziplinBE disziplinBE = disziplinDAO.update(DisziplinMapper.toDisziplinBE.apply(disziplinDO), currentUserId);
        return DisziplinMapper.toDisziplinDO.apply(disziplinBE);
    }


    private void checkDisziplin(DisziplinDO disziplinDO) {
        Preconditions.checkNotNull(disziplinDO, PRECONDITION_MSG_DISZIPLIN_DO);

        Preconditions.checkNotNull(disziplinDO.getDisziplinId(), PRECONDITION_MSG_CURRENT_DISZIPLIN_ID);
        Preconditions.checkArgument(disziplinDO.getDisziplinId() >= 0, PRECONDITION_MSG_CURRENT_DISZIPLIN_ID);

        Preconditions.checkNotNull(disziplinDO.getDisziplinName(), PRECONDITION_MSG_CURRENT_DISZIPLIN_NAME);
        Preconditions.checkArgument(!disziplinDO.getDisziplinName().equals(""), PRECONDITION_MSG_CURRENT_DISZIPLIN_NAME);

    }


    @Override
    public void delete(DisziplinDO disziplinDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_MSG_CURRENT_DISZIPLIN_ID);

        DisziplinBE disziplinBE = DisziplinMapper.toDisziplinBE.apply(disziplinDO);
        disziplinDAO.delete(disziplinBE, currentUserId);
    }
}
