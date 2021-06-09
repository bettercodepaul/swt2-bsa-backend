package de.bogenliga.application.business.vereine.impl.business;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
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
    private final RegionenComponent regionenComponent;

    @Autowired
    public VereinComponentImpl(VereinDAO vereinDAO, @Lazy RegionenComponent regionenComponent) {
        this.vereinDAO = vereinDAO;
        this.regionenComponent = regionenComponent;
    }

    @Override
    public List<VereinDO> findAll() {
        final List<VereinBE> vereinBEList = vereinDAO.findAll();
        List<VereinDO> vereinDOList = vereinBEList.stream().map(VereinMapper.toVereinDO).collect(Collectors.toList());

        return alterDoByRegionName(vereinDOList);
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
        final VereinBE vereinBE = vereinDAO.findById(vereinId);
        final VereinDO vereinDO = VereinMapper.toVereinDO.apply(vereinBE);
        vereinDO.setRegionName(this.regionenComponent.findById(vereinBE.getVereinRegionId()).getRegionName());

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

    /**
     * Alters a {@VereinDO} with a regionName that matches to the regionId
     *
     * @param vereinDOList containing {@VereinDO} elements
     *
     * @return List of {@VereinDO} elements altered with a regionName
     */
    private List<VereinDO> alterDoByRegionName(List<VereinDO> vereinDOList) {
        final List<RegionenDO> regionenDOList = regionenComponent.findAll();
        for (int i = 0; i < vereinDOList.size(); i++) {
            VereinDO tmpVerein = vereinDOList.get(i);

            Optional<RegionenDO> regionenDOOptional = regionenDOList.stream()
                    .filter(region -> region.getId().equals(tmpVerein.getRegionId())).findFirst();

            if(regionenDOOptional.isPresent()) {
                tmpVerein.setRegionName(regionenDOOptional.get().getRegionName());
                vereinDOList.set(i, tmpVerein);
            }
        }

        return vereinDOList;
    }
}
