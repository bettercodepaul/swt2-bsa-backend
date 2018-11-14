package de.bogenliga.application.business.vereine.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.business.vereine.impl.mapper.VereinMapper;
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
    private static final String PRECONDITION_MSG_VEREIN_DSB_MITGLIED = "DsbMitglied id must not be null";
    private static final String PRECONDITION_MSG_VEREIN_DSB_MITGLIED_NOT_NEG = "DsbMitglied id must not be negative";


    private final VereinDAO vereinDAO;


    @Autowired
    public VereinComponentImpl(VereinDAO vereinDAO) {
        this.vereinDAO = vereinDAO;
    }


    @Override
    public List<VereinDO> findAll() {
        final List<VereinBE> vereinBEList = vereinDAO.findAll();
        return vereinBEList.stream().map(VereinMapper.toVereinDO).collect(Collectors.toList());
    }


    @Override
    public VereinDO create(VereinDO vereinDO, long currentDsbMitglied) {
        checkVereinDO(vereinDO, currentDsbMitglied);

        final VereinBE vereinBE = VereinMapper.toVereinBE.apply(vereinDO);
        final VereinBE persistedVereinBE = vereinDAO.create(vereinBE, currentDsbMitglied);

        return VereinMapper.toVereinDO.apply(persistedVereinBE);
    }

    private void checkVereinDO(final VereinDO vereinDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(vereinDO, PRECONDITION_MSG_VEREIN);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_VEREIN_DSB_MITGLIED_NOT_NEG);
        Preconditions.checkNotNull(vereinDO.getName(), PRECONDITION_MSG_VEREIN_NAME);
        Preconditions.checkNotNull(vereinDO.getDsbIdentifier(), PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER);
        Preconditions.checkNotNull(vereinDO.getRegionId(), PRECONDITION_MSG_VEREIN_REGION_ID);

        Preconditions.checkArgument(vereinDO.getRegionId() >= 0, PRECONDITION_MSG_VEREIN_REGION_ID_NOT_NEG);
    }
}
