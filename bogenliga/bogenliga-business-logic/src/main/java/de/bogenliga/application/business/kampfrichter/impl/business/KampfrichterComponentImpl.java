package de.bogenliga.application.business.kampfrichter.impl.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.kampfrichter.api.KampfrichterComponent;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.dao.KampfrichterDAO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterExtendedBE;
import de.bogenliga.application.business.kampfrichter.impl.mapper.KampfrichterMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;


/**
 * Implementation of {@link KampfrichterComponent}
 */
@Component
public class KampfrichterComponentImpl implements KampfrichterComponent {

    private static final String PRECONDITION_MSG_KAMPFRICHTER = "KampfrichterDO must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_ID = "KampfrichterDO ID must not be negative";
    private static final String PRECONDITION_MSG_CURRENT_KAMPFRICHTER = "Current kampfrichter userId must not be negative";

    private final KampfrichterDAO kampfrichterDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param kampfrichterDAO to access the database and return kampfrichter representations
     */
    @Autowired
    public KampfrichterComponentImpl(final KampfrichterDAO kampfrichterDAO) {
        this.kampfrichterDAO = kampfrichterDAO;
    }


    @Override
    public List<KampfrichterDO> findAll() {
        final List<KampfrichterBE> kampfrichterBEList = kampfrichterDAO.findAll();
        return kampfrichterBEList.stream().map(KampfrichterMapper.toKampfrichterDO).toList();
    }

    @Override
    public List<KampfrichterDO> findByWettkampfidNotInWettkampftag(final long wettkampfId){
        final List<KampfrichterExtendedBE> kampfrichterExtendedBEList= kampfrichterDAO.findByWettkampfidNotInWettkampftag(wettkampfId);
        return kampfrichterExtendedBEList.stream().map(KampfrichterMapper.toKampfrichterDOExtended).toList();
    }

    @Override
    public List<KampfrichterDO> findByWettkampfidInWettkampftag(final long wettkampfId){
        final List<KampfrichterExtendedBE> kampfrichterExtendedBEList= kampfrichterDAO.findByWettkampfidInWettkampftag(wettkampfId);
        return kampfrichterExtendedBEList.stream().map(KampfrichterMapper.toKampfrichterDOExtended).toList();
    }

    @Override
    public KampfrichterDO findById(final long userId) {
        Preconditions.checkArgument(userId >= 0, PRECONDITION_MSG_KAMPFRICHTER_ID);

        final KampfrichterBE result = kampfrichterDAO.findById(userId);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", userId));
        }

        return KampfrichterMapper.toKampfrichterDO.apply(result);
    }


    @Override
    public KampfrichterDO create(final KampfrichterDO kampfrichterDO, final long currentKampfrichterUserId) {

        Preconditions.checkArgument(kampfrichterDO.getUserId() >= 0, PRECONDITION_MSG_KAMPFRICHTER_ID);

        final KampfrichterBE kampfrichterBE = KampfrichterMapper.toKampfrichterBE.apply(kampfrichterDO);
        final KampfrichterBE persistedKampfrichterBE = kampfrichterDAO.create(kampfrichterBE, currentKampfrichterUserId);

        return KampfrichterMapper.toKampfrichterDO.apply(persistedKampfrichterBE);
    }


    @Override
    public KampfrichterDO update(final KampfrichterDO kampfrichterDO, final long currentKampfrichterUserId) {

        Preconditions.checkArgument(kampfrichterDO.getUserId() >= 0, PRECONDITION_MSG_KAMPFRICHTER_ID);

        final KampfrichterBE kampfrichterBE = KampfrichterMapper.toKampfrichterBE.apply(kampfrichterDO);
        final KampfrichterBE persistedKampfrichterBE = kampfrichterDAO.update(kampfrichterBE, currentKampfrichterUserId);

        return KampfrichterMapper.toKampfrichterDO.apply(persistedKampfrichterBE);
    }


    @Override
    public void delete(final KampfrichterDO kampfrichterDO, final long currentKampfrichterUserId) {
        Preconditions.checkNotNull(kampfrichterDO, PRECONDITION_MSG_KAMPFRICHTER);
        Preconditions.checkArgument(kampfrichterDO.getUserId() >= 0, PRECONDITION_MSG_KAMPFRICHTER_ID);
        Preconditions.checkArgument(currentKampfrichterUserId >= 0, PRECONDITION_MSG_CURRENT_KAMPFRICHTER);

        final KampfrichterBE kampfrichterBE = KampfrichterMapper.toKampfrichterBE.apply(kampfrichterDO);

        kampfrichterDAO.delete(kampfrichterBE, currentKampfrichterUserId);

    }

}
