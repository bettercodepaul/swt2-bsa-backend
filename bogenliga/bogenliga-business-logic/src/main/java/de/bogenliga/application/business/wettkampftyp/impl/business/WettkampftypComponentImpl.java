package de.bogenliga.application.business.wettkampftyp.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.wettkampftyp.api.WettkampftypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampftypDO;
import de.bogenliga.application.business.wettkampftyp.impl.dao.WettkampftypDAO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampftypBE;
import de.bogenliga.application.business.wettkampftyp.impl.mapper.WettkampftypMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link WettkampftypComponent}
 * @Autor Marvin Holm, Daniel Schott
 */
@Component
public class WettkampftypComponentImpl implements WettkampftypComponent {

    private static final String PRECONDITION_MSG_WETTKAMPFTYP_ID = "wettkampftypID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPFTYP_NAME= "wettkampftypName must not be null";



    private final WettkampftypDAO wettkampftypDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param wettkampftypDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public WettkampftypComponentImpl(final WettkampftypDAO wettkampftypDAO) { this.wettkampftypDAO = wettkampftypDAO;System.out.println("created DAO object"); }


    @Override
    public List<WettkampftypDO> findAll() {
        final List<WettkampftypBE> wettkampftypBEList = wettkampftypDAO.findAll();
        return wettkampftypBEList.stream().map(WettkampftypMapper.toWettkampftypDO).collect(Collectors.toList());
    }


    @Override
    public WettkampftypDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);

        final WettkampftypBE result = wettkampftypDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return WettkampftypMapper.toWettkampftypDO.apply(result);
    }


    @Override
    public WettkampftypDO create(final WettkampftypDO wettkampftypDO, final long currentWettkampftypID) {
        checkDsbMitgliedDO(wettkampftypDO, currentWettkampftypID);

        final WettkampftypBE wettkampftypBE = WettkampftypMapper.toWettkampftypBE.apply(wettkampftypDO);
        final WettkampftypBE persistedWettkampftypBe = wettkampftypDAO.create(wettkampftypBE, currentWettkampftypID);

        return WettkampftypMapper.toWettkampftypDO.apply(persistedWettkampftypBe);
    }


    @Override
    public WettkampftypDO update(final WettkampftypDO wettkampftypDO, final long currentWettkampftypID) {
        checkDsbMitgliedDO(wettkampftypDO, currentWettkampftypID);
        Preconditions.checkArgument(wettkampftypDO.getId() >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);

        final WettkampftypBE wettkampftypBE = WettkampftypMapper.toWettkampftypBE.apply(wettkampftypDO);
        final WettkampftypBE persistedWettkampftypBe = wettkampftypDAO.update(wettkampftypBE, currentWettkampftypID);

        return WettkampftypMapper.toWettkampftypDO.apply(persistedWettkampftypBe);
    }


    @Override
    public void delete(final WettkampftypDO wettkampftypDO, final long currentWettkampftypID) {
        Preconditions.checkNotNull(wettkampftypDO, PRECONDITION_MSG_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(wettkampftypDO.getId() >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(currentWettkampftypID >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);

        final WettkampftypBE wettkampftypBE = WettkampftypMapper.toWettkampftypBE.apply(wettkampftypDO);

        wettkampftypDAO.delete(wettkampftypBE, currentWettkampftypID);

    }

    private void checkDsbMitgliedDO(final WettkampftypDO wettkampftypDO, final long currentWettkampftypID) {
        Preconditions.checkNotNull(wettkampftypDO, PRECONDITION_MSG_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(currentWettkampftypID >= 0, PRECONDITION_MSG_WETTKAMPFTYP_ID);
        Preconditions.checkNotNull(wettkampftypDO.getName(), PRECONDITION_MSG_WETTKAMPFTYP_NAME);
    }

}
