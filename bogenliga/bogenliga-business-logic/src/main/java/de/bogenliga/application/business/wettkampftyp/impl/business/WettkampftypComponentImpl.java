package de.bogenliga.application.business.wettkampftyp.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampftypDO;
import de.bogenliga.application.business.wettkampftyp.api.WettkampftypComponent;
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

    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "wettkampfID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID= "wettkampfVeranstaltungsID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_DATUM= "wettkampfDatum must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_ORT= "wettkampfOrt must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_BEGINN = "wettkampfBeginn must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_TAG = "wettkampfTag must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID = "wettkampfDisziplinID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_WETTKAMPFTYP_ID= "wettkampfTypID must not be null and must not be negative";


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
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        final WettkampftypBE result = wettkampftypDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return WettkampftypMapper.toWettkampftypDO.apply(result);
    }


    @Override
    public WettkampftypDO create(final WettkampftypDO wettkampftypDO, final long currentWettkampfID) {
        checkDsbMitgliedDO(wettkampftypDO, currentWettkampfID);

        final WettkampftypBE wettkampftypBE = WettkampftypMapper.toWettkampftypBE.apply(wettkampftypDO);
        final WettkampftypBE persistedWettkampftypBe = wettkampftypDAO.create(wettkampftypBE, currentWettkampfID);

        return WettkampftypMapper.toWettkampftypDO.apply(persistedWettkampftypBe);
    }


    @Override
    public WettkampftypDO update(final WettkampftypDO wettkampftypDO, final long currentWettkampfID) {
        checkDsbMitgliedDO(wettkampftypDO, currentWettkampfID);
        Preconditions.checkArgument(wettkampftypDO.getWettkampftypId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        final WettkampftypBE wettkampftypBE = WettkampftypMapper.toWettkampftypBE.apply(wettkampftypDO);
        final WettkampftypBE persistedWettkampftypBe = wettkampftypDAO.update(wettkampftypBE, currentWettkampfID);

        return WettkampftypMapper.toWettkampftypDO.apply(persistedWettkampftypBe);
    }


    @Override
    public void delete(final WettkampftypDO wettkampftypDO, final long currentWettkampfID) {
        Preconditions.checkNotNull(wettkampftypDO, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(wettkampftypDO.getWettkampftypId() >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(currentWettkampfID >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        final WettkampftypBE wettkampftypBE = WettkampftypMapper.toWettkampftypBE.apply(wettkampftypDO);

        wettkampftypDAO.delete(wettkampftypBE, currentWettkampfID);

    }

    private void checkDsbMitgliedDO(final WettkampftypDO wettkampftypDO, final long currentWettkampfID) {
        Preconditions.checkNotNull(wettkampftypDO, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(currentWettkampfID >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(wettkampftypDO.getWettkampftypId() >= 0, PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);
        Preconditions.checkNotNull(wettkampftypDO.getWettkampftypName(), PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);


    }

}
