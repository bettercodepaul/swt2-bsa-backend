package de.bogenliga.application.business.vereine.impl.business;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.bogenliga.application.business.namemapping.api.NameMappingComponent;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAOext;
import de.bogenliga.application.business.vereine.impl.entity.VereinBEext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
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
    private static final String PRECONDITION_MSG_VEREIN_DSB_MITGLIED_NOT_NEG = "DsbMitglied id must not be negative";

    private final VereinDAO vereinDAO;
    private final VereinDAOext vereinDAOext;
    private final NameMappingComponent nameMappingComponent;

    @Autowired
    public VereinComponentImpl(VereinDAO vereinDAO,
                               VereinDAOext vereinDAOext,
                               NameMappingComponent nameMappingComponent) {
        this.vereinDAO = vereinDAO;
        this.vereinDAOext = vereinDAOext;
        this.nameMappingComponent = nameMappingComponent;
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
        final VereinBE persistedVereinBE = vereinDAO.create(vereinBE, currentDsbMitglied);

        return VereinMapper.toVereinDO.apply(persistedVereinBE);
    }

    @Override
    public VereinDO findById(long vereinId) {
        final VereinBEext vereinBEext = vereinDAOext.findById(vereinId);
        final VereinDO vereinDO = VereinMapper.exttoVereinDO.apply(vereinBEext);
        return vereinDO;
    }

    @Override
    public VereinDO update(VereinDO vereinDO, long currentDsbMitglied) {
        checkVereinDO(vereinDO, currentDsbMitglied);
        Preconditions.checkArgument(vereinDO.getId() >= 0, PRECONDITION_MSG_VEREIN_ID);

        final VereinBE vereinBE = VereinMapper.toVereinBE.apply(vereinDO);
        final VereinBE persistedVereinBE = vereinDAO.update(vereinBE, currentDsbMitglied);
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

}