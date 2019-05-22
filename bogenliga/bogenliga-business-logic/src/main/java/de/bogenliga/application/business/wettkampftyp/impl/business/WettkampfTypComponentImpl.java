package de.bogenliga.application.business.wettkampftyp.impl.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.wettkampftyp.impl.dao.WettkampfTypDAO;
import de.bogenliga.application.business.wettkampftyp.impl.mapper.WettkampfTypMapper;
import de.bogenliga.application.common.validation.Preconditions;


/**
 * @author Kay Scheerer
 */
@Component
public class WettkampfTypComponentImpl implements WettkampfTypComponent {
    private static final String PRECONDITION_MSG_TEMPLATE_NULL = "Wettkampf: %s must not be null";
    private static final String PRECONDITION_MSG_TEMPLATE_NEGATIVE = "Wettkampf: %s must not be negative";

    private final WettkampfTypDAO wettkampfTypDAO;
    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param wettkampfTypDAO to access the database and return passe representations
     */
    @Autowired
    public WettkampfTypComponentImpl(final WettkampfTypDAO wettkampfTypDAO) {
        this.wettkampfTypDAO = wettkampfTypDAO;
    }

    /**
     * Checks the precondition of an ID given
     * @param id the ID to check
     * @param iDIdentifier the variable name which should appear in the error message
     */
    public void checkPreconditions(final Long id, String iDIdentifier) {
        Preconditions.checkNotNull(id, String.format(PRECONDITION_MSG_TEMPLATE_NULL, iDIdentifier));
        Preconditions.checkArgument(id >= 0, String.format(PRECONDITION_MSG_TEMPLATE_NEGATIVE, iDIdentifier));
    }

    /**
     * Return all passe from one Wettkampf
     *
     * @param wettkampfTypId of the mannschaft
     *
     * @return list of all passe from one Wettkampf in the database; empty list, if no passe are found
     */
    @Override
    public WettkampfTypDO findById(Long wettkampfTypId) {
        checkPreconditions(wettkampfTypId, "wettkampfTypId");
        return WettkampfTypMapper.toWettkampfTypDO.apply(wettkampfTypDAO.findByWettkampfId(wettkampfTypId));
    }


}
