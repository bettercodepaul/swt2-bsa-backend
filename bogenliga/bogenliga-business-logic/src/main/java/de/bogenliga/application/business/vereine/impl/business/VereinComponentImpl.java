package de.bogenliga.application.business.vereine.impl.business;


import java.util.List;

import de.bogenliga.application.business.vereine.impl.dao.VereinDAOext;
import de.bogenliga.application.business.vereine.impl.entity.VereinBEext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.business.vereine.impl.mapper.VereinMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.validation.Preconditions;


/**
 * Implementation of {@link VereinComponent}
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
@Component
public class VereinComponentImpl implements VereinComponent {

    private static final String PRECONDITION_MSG_VEREIN = "VereinDO must not be null";
    private static final String PRECONDITION_MSG_VEREIN_ID = "VereinDO ID must not be negative";
    private static final String PRECONDITION_MSG_VEREIN_NAME = "VereinDO name must not be null";
    private static final String PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER = "VereinDO dsk identifier must not be null";
    private static final String PRECONDITION_MSG_VEREIN_REGION_ID = "VereinDO region id must not be null";
    private static final String PRECONDITION_MSG_VEREIN_REGION_ID_NOT_NEG = "VereinDO region id must not be negative";
    private static final String PRECONDITION_MSG_VEREIN_DSB_MITGLIED_NOT_NEG = "DsbMitglied id must not be negative";
    private static final String DUPLICATE_VEREIN_DSB_IDENTIFIER_CONSTRAINT = "uc_verein_dsb_identifier";
    private static final String DUPLICATE_VEREIN_NAME_CONSTRAINT = "uc_verein_name";
    private static final String EXCEPTION_DUPLICATE_VEREIN = "Duplicate Verein";

    private final VereinDAO vereinDAO;
    private final VereinDAOext vereinDAOext;

    @Autowired
    public VereinComponentImpl(VereinDAO vereinDAO,
                               VereinDAOext vereinDAOext) {
        this.vereinDAO = vereinDAO;
        this.vereinDAOext = vereinDAOext;
    }

    @Override
    public List<VereinDO> findAll() {
        final List<VereinBEext> vereinBEextList = vereinDAOext.findAll();
        List<VereinDO> vereinDOList = vereinBEextList.stream().map(VereinMapper.exttoVereinDO).toList();
         return List.copyOf(vereinDOList);
    }

    @Override
    public List<VereinDO> findBySearch(final String searchTerm) {
        final List<VereinBEext> vereinBEextList = vereinDAOext.findBySearch(searchTerm);
        List<VereinDO> vereinDOList = List.copyOf(vereinBEextList.stream().map(VereinMapper.exttoVereinDO).toList());

        return List.copyOf(vereinDOList);
    }

    @Override
    public VereinDO create(VereinDO vereinDO, long currentDsbMitglied) {
        checkVereinDO(vereinDO, currentDsbMitglied);

        final VereinBE vereinBE = VereinMapper.toVereinBE.apply(vereinDO);
        final VereinBE persistedVereinBE;
        try {
            persistedVereinBE = vereinDAO.create(vereinBE, currentDsbMitglied);
        } catch (TechnicalException e) {
            if (isDuplicateVereinConflict(e)) {
                throw new BusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, EXCEPTION_DUPLICATE_VEREIN);
            }
            throw e;
        }

        return VereinMapper.toVereinDO.apply(persistedVereinBE);
    }

    @Override
    public VereinDO findById(long vereinId) {
        final VereinBEext vereinBEext = vereinDAOext.findById(vereinId);
        return VereinMapper.exttoVereinDO.apply(vereinBEext);
    }

    @Override
    public VereinDO update(VereinDO vereinDO, long currentDsbMitglied) {
        checkVereinDO(vereinDO, currentDsbMitglied);
        Preconditions.checkArgument(vereinDO.getId() >= 0, PRECONDITION_MSG_VEREIN_ID);

        final VereinBE vereinBE = VereinMapper.toVereinBE.apply(vereinDO);
        final VereinBE persistedVereinBE;
        try {
            persistedVereinBE = vereinDAO.update(vereinBE, currentDsbMitglied);
        } catch (TechnicalException e) {
            if (isDuplicateVereinConflict(e)) {
                throw new BusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, EXCEPTION_DUPLICATE_VEREIN);
            }
            throw e;
        }
        return VereinMapper.toVereinDO.apply(persistedVereinBE);
    }

    @Override
    public void delete(VereinDO vereinDO, long currentDsbMitglied) {
        Preconditions.checkNotNull(vereinDO, PRECONDITION_MSG_VEREIN);
        Preconditions.checkArgument(vereinDO.getId() >= 0, PRECONDITION_MSG_VEREIN_ID);
        Preconditions.checkArgument(currentDsbMitglied >= 0, PRECONDITION_MSG_VEREIN_DSB_MITGLIED_NOT_NEG);

        final VereinBE vereinBE = VereinMapper.toVereinBE.apply(vereinDO);

        vereinDAO.delete(vereinBE, currentDsbMitglied);
    }

    private void checkVereinDO(final VereinDO vereinDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(vereinDO, PRECONDITION_MSG_VEREIN);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_VEREIN_DSB_MITGLIED_NOT_NEG);
        Preconditions.checkNotNull(vereinDO.getName(), PRECONDITION_MSG_VEREIN_NAME);
        Preconditions.checkNotNull(vereinDO.getDsbIdentifier(), PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER);
        Preconditions.checkNotNull(vereinDO.getRegionId(), PRECONDITION_MSG_VEREIN_REGION_ID);

        Preconditions.checkArgument(vereinDO.getRegionId() >= 0, PRECONDITION_MSG_VEREIN_REGION_ID_NOT_NEG);
    }

    private boolean isDuplicateVereinConflict(final TechnicalException e) {
        return e.getErrorCode() == ErrorCode.DATABASE_ERROR
                && e.getMessage() != null
                && (e.getMessage().contains(DUPLICATE_VEREIN_DSB_IDENTIFIER_CONSTRAINT)
                || e.getMessage().contains(DUPLICATE_VEREIN_NAME_CONSTRAINT));
    }

}
