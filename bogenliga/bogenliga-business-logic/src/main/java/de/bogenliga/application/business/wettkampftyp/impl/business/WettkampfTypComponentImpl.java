package de.bogenliga.application.business.wettkampftyp.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.wettkampftyp.impl.dao.WettkampfTypDAO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampfTypBE;
import de.bogenliga.application.business.wettkampftyp.impl.mapper.WettkampfTypMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link WettkampfTypComponent}
 * @Autor Marvin Holm, Daniel Schott
 */
@Component
public class WettkampfTypComponentImpl implements WettkampfTypComponent {

    private static final String PRECONDITION_MSG_WETTKAMPFTYP_ID = "wettkampftypID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPFTYP_NAME= "wettkampftypName must not be null";



    private final WettkampfTypDAO wettkampftypDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param wettkampftypDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public WettkampfTypComponentImpl(final WettkampfTypDAO wettkampftypDAO) { this.wettkampftypDAO = wettkampftypDAO;}


    @Override
    public List<WettkampfTypDO> findAll() {
        final List<WettkampfTypBE> wettkampftypBEList = wettkampftypDAO.findAll();
        return wettkampftypBEList.stream().map(WettkampfTypMapper.toWettkampfTypDO).collect(Collectors.toList());
    }


    @Override
    public WettkampfTypDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);

        final WettkampfTypBE result = wettkampftypDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return WettkampfTypMapper.toWettkampfTypDO.apply(result);
    }


    @Override
    public WettkampfTypDO create(final WettkampfTypDO wettkampftypDO, final long userId) {
        checkDsbMitgliedDO(wettkampftypDO, userId);

        final WettkampfTypBE wettkampftypBE = WettkampfTypMapper.toWettkampfTypBE.apply(wettkampftypDO);
        final WettkampfTypBE persistedWettkampftypBe = wettkampftypDAO.create(wettkampftypBE, userId);

        return WettkampfTypMapper.toWettkampfTypDO.apply(persistedWettkampftypBe);
    }


    @Override
    public WettkampfTypDO update(final WettkampfTypDO wettkampftypDO, final long userId) {
        checkDsbMitgliedDO(wettkampftypDO, userId);
        Preconditions.checkArgument(wettkampftypDO.getId() >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);

        final WettkampfTypBE wettkampftypBE = WettkampfTypMapper.toWettkampfTypBE.apply(wettkampftypDO);
        final WettkampfTypBE persistedWettkampftypBe = wettkampftypDAO.update(wettkampftypBE, userId);

        return WettkampfTypMapper.toWettkampfTypDO.apply(persistedWettkampftypBe);
    }


    @Override
    public void delete(final WettkampfTypDO wettkampftypDO, final long userId) {
        Preconditions.checkNotNull(wettkampftypDO, PRECONDITION_MSG_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(wettkampftypDO.getId() >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(userId >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);

        final WettkampfTypBE wettkampftypBE = WettkampfTypMapper.toWettkampfTypBE.apply(wettkampftypDO);

        wettkampftypDAO.delete(wettkampftypBE, userId);

    }

    private void checkDsbMitgliedDO(final WettkampfTypDO wettkampftypDO, final long currentWettkampftypID) {
        Preconditions.checkNotNull(wettkampftypDO, PRECONDITION_MSG_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(currentWettkampftypID >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);
        Preconditions.checkNotNull(wettkampftypDO.getName(), PRECONDITION_MSG_WETTKAMPFTYP_NAME);
    }

}
