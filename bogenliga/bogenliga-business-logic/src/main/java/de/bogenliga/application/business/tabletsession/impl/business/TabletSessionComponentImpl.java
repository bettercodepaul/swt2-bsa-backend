package de.bogenliga.application.business.tabletsession.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.tabletsession.api.TabletSessionComponent;
import de.bogenliga.application.business.tabletsession.api.types.TabletSessionDO;
import de.bogenliga.application.business.tabletsession.impl.dao.TabletSessionDAO;
import de.bogenliga.application.business.tabletsession.impl.entity.TabletSessionBE;
import de.bogenliga.application.business.tabletsession.impl.mapper.TabletSessionMapper;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * @author Kay Scheerer
 */
@Component
public class TabletSessionComponentImpl implements TabletSessionComponent {

    public static final String PRECONDITION_FIELD_SCHEIBENNUMMER = "scheibennummer";
    public static final String PRECONDITION_FIELD_WETTKAMPF_ID = "wettkampfId";
    public static final String PRECONDITION_FIELD_CURRENT_USER = "currentMemberId";
    private static final String PRECONDITION_MSG_TEMPLATE_NULL = "TabletSession: %s must not be null";
    private static final String PRECONDITION_MSG_TEMPLATE_NEGATIVE = "TabletSession: %s must not be negative";

    private final TabletSessionDAO tabletDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param tabletDAO to access the database and return passe representations
     */
    @Autowired
    public TabletSessionComponentImpl(final TabletSessionDAO tabletDAO) {
        this.tabletDAO = tabletDAO;
    }


    @Override
    public List<TabletSessionDO> findAll() {
        final List<TabletSessionBE> tabBEList = tabletDAO.findAll();
        return tabBEList.stream().map(TabletSessionMapper.toTabletSessionDO).collect(Collectors.toList());
    }


    @Override
    public List<TabletSessionDO> findById(Long wettkampfid) {
        checkPreconditions(wettkampfid, PRECONDITION_FIELD_WETTKAMPF_ID);

        final List<TabletSessionBE> tabBEList = tabletDAO.findById(wettkampfid);
        return tabBEList.stream().map(TabletSessionMapper.toTabletSessionDO).collect(Collectors.toList());
    }


    @Override
    public TabletSessionDO findByIdScheibennummer(Long wettkampfid, Long scheibenNr) {
        checkPreconditions(scheibenNr, PRECONDITION_FIELD_SCHEIBENNUMMER);
        checkPreconditions(wettkampfid, PRECONDITION_FIELD_WETTKAMPF_ID);

        final TabletSessionBE tabBE = tabletDAO.findByIdScheinebnummer(wettkampfid, scheibenNr);
        return TabletSessionMapper.toTabletSessionDO.apply(tabBE);
    }


    @Override
    public TabletSessionDO create(TabletSessionDO sessionDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_CURRENT_USER);

        checkPreconditions(sessionDO.getScheibennummer(), PRECONDITION_FIELD_SCHEIBENNUMMER);
        checkPreconditions(sessionDO.getWettkampfId(), PRECONDITION_FIELD_WETTKAMPF_ID);

        final TabletSessionBE passeBE = TabletSessionMapper.toTabletSessionBE.apply(sessionDO);
        TabletSessionBE tabBE = tabletDAO.create(passeBE, currentUserId);
        return TabletSessionMapper.toTabletSessionDO.apply(tabBE);
    }


    @Override
    public TabletSessionDO update(TabletSessionDO sessionDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_CURRENT_USER);
        checkPreconditions(sessionDO.getScheibennummer(), PRECONDITION_FIELD_SCHEIBENNUMMER);
        checkPreconditions(sessionDO.getWettkampfId(), PRECONDITION_FIELD_WETTKAMPF_ID);

        final TabletSessionBE passeBE = TabletSessionMapper.toTabletSessionBE.apply(sessionDO);
        TabletSessionBE tabBE = tabletDAO.update(passeBE, currentUserId);
        return TabletSessionMapper.toTabletSessionDO.apply(tabBE);
    }


    @Override
    public void delete(TabletSessionDO sessionDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_CURRENT_USER);
        checkPreconditions(sessionDO.getScheibennummer(), PRECONDITION_FIELD_SCHEIBENNUMMER);
        checkPreconditions(sessionDO.getWettkampfId(), PRECONDITION_FIELD_WETTKAMPF_ID);

        final TabletSessionBE tabBe = TabletSessionMapper.toTabletSessionBE.apply(sessionDO);
        tabletDAO.delete(tabBe, currentUserId);

    }


    /**
     * Checks the precondition of an ID given
     *
     * @param id           the ID to check
     * @param iDIdentifier the variable name which should appear in the error message
     */
    public void checkPreconditions(final Long id, String iDIdentifier) {
        Preconditions.checkNotNull(id, String.format(PRECONDITION_MSG_TEMPLATE_NULL, iDIdentifier));
        Preconditions.checkArgument(id >= 0, String.format(PRECONDITION_MSG_TEMPLATE_NEGATIVE, iDIdentifier));
    }
}
