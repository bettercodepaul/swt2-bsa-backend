package de.bogenliga.application.business.regionen.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.dao.RegionenDAO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.business.regionen.impl.mapper.RegionenMapper;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link RegionenComponent}
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
@Component
public class RegionenComponentImpl implements RegionenComponent {

    private static final String PRECONDITION_MSG_REGION = "RegionDO must not be null";
    private static final String PRECONDITION_MSG_REGION_ID = "RegionDO ID must not be negative";
    private static final String PRECONDITION_MSG_REGION_NAME = "RegionDO name must not be null";
    private static final String PRECONDITION_MSG_REGION_DSB_MITGLIED_NOT_NEG = "DsbMitglied id must not be negative";

    //no need for preconditions since we only need to implement FindAll()

    public final RegionenDAO regionenDAO;


    /**
     * Constructor Dependeny injection with {@link org.springframework.beans.factory.annotation.Autowired}
     *
     * @param regionenDAO
     */
    public RegionenComponentImpl(RegionenDAO regionenDAO) {
        this.regionenDAO = regionenDAO;
    }


    @Override
    public List<RegionenDO> findAll() {
        final List<RegionenBE> regionenBEList = regionenDAO.findAll();
        return regionenBEList.stream().map(RegionenMapper.toRegionDO).collect(Collectors.toList());
    }


    @Override
    public List<RegionenDO> findAllByType(final String type) {
        final List<RegionenBE> regionenBEList = regionenDAO.findAllByType(type);
        return regionenBEList.stream().map(RegionenMapper.toRegionDO).collect(Collectors.toList());
    }

    @Override
    public RegionenDO findById(long vereinId) {
        final RegionenBE regionenBE = regionenDAO.findById(vereinId);
        return RegionenMapper.toRegionDO.apply(regionenBE);
    }

    @Override
    public RegionenDO create(RegionenDO regionenDO, long currentDsbMitglied) {
        checkRegionenDO(regionenDO, currentDsbMitglied);

        final RegionenBE regionenBE = RegionenMapper.toRegionBE.apply(regionenDO);
        final RegionenBE persistedRegionenBE = regionenDAO.create(regionenBE, currentDsbMitglied);

        return RegionenMapper.toRegionDO.apply(persistedRegionenBE);
    }

    @Override
    public RegionenDO update(RegionenDO regionenDO, long currentDsbMitglied) {
        checkRegionenDO(regionenDO, currentDsbMitglied);
        Preconditions.checkArgument(regionenDO.getId() >= 0, PRECONDITION_MSG_REGION_ID);

        final RegionenBE regionenBE = RegionenMapper.toRegionBE.apply(regionenDO);
        final RegionenBE persistedRegionenBE = regionenDAO.update(regionenBE, currentDsbMitglied);

        return RegionenMapper.toRegionDO.apply(persistedRegionenBE);
    }

    @Override
    public void delete(RegionenDO regionenDO, long currentDsbMitglied) {
        Preconditions.checkNotNull(regionenDO, PRECONDITION_MSG_REGION);
        Preconditions.checkArgument(regionenDO.getId() >= 0, PRECONDITION_MSG_REGION_ID);
        Preconditions.checkArgument(currentDsbMitglied >= 0, PRECONDITION_MSG_REGION_DSB_MITGLIED_NOT_NEG);

        final RegionenBE regionenBE = RegionenMapper.toRegionBE.apply(regionenDO);

        regionenDAO.delete(regionenBE, currentDsbMitglied);
    }

    private void checkRegionenDO(final RegionenDO regionenDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(regionenDO, PRECONDITION_MSG_REGION);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_REGION_DSB_MITGLIED_NOT_NEG);
        Preconditions.checkNotNull(regionenDO.getRegionName(), PRECONDITION_MSG_REGION_NAME);
    }
}
