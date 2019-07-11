package de.bogenliga.application.business.tabletsession.impl.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
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
    private static final Integer MAX_NUM_SCHEIBEN = 8;

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
    public List<TabletSessionDO> findByWettkampfId(Long wettkampfid) {
        checkPreconditions(wettkampfid, PRECONDITION_FIELD_WETTKAMPF_ID);

        final List<TabletSessionBE> tabBEList = tabletDAO.findByWettkampfId(wettkampfid);
        return tabBEList.stream().map(TabletSessionMapper.toTabletSessionDO).collect(Collectors.toList());
    }


    @Override
    public List<MatchDO> getRelatedMatches(TabletSessionDO tabletSessionDO, MatchComponent matchComponent) {
        checkPreconditions(tabletSessionDO.getScheibennummer(), "scheibenNr");
        MatchDO matchDO = null;
        if (tabletSessionDO.getMatchId() != null) {
            matchDO = matchComponent.findById(tabletSessionDO.getMatchId());
        }
        List<MatchDO> wettkampfMatches = matchComponent.findByWettkampfId(tabletSessionDO.getWettkampfId());
        Long scheibenNr = tabletSessionDO.getScheibennummer();
        // Scheibennummern: [(1,2),(3,4),(5,6),(7,8)]
        // Die gruppierten Nummern bilden eine Begegnung aus 2 Matches, die hier ermittelt werden
        // ist das gegebene Match an Scheibe nr 2 -> andere Scheibe ist nr 1, und andersherum, daher das überprüfen auf gerade/ungerade
        Long otherScheibeNr = scheibenNr % 2 == 0 ? scheibenNr - 1 : scheibenNr + 1;
        Long matchNr = (matchDO != null ? matchDO.getNr() : 1L);
        return wettkampfMatches.stream()
                .filter(mDO -> mDO.getNr().equals(matchNr))
                .filter(mDO -> (
                        scheibenNr.equals(mDO.getScheibenNummer())
                                || otherScheibeNr.equals(mDO.getScheibenNummer())
                ))
                .collect(Collectors.toList());
    }


    @Override
    public List<TabletSessionDO> createInitialForWettkampf(Long wettkampfId, final MatchComponent matchComponent, Long currentUserId) {
        List<TabletSessionDO> tabletSessionDOS = new ArrayList<>();
        for (int i = 0; i < MAX_NUM_SCHEIBEN; i++) {
            TabletSessionDO tabDO = this.addInitialData(wettkampfId, i + 1, matchComponent);
            if (tabDO == null) {
                throw new IllegalArgumentException("Keine matches in diesem Wettkampf");
            }
            tabDO = this.create(tabDO, currentUserId);
            tabletSessionDOS.add(tabDO);
        }
        return tabletSessionDOS;
    }


    @Override
    public TabletSessionDO addInitialData(Long wettkampfId, int scheibennummer, final MatchComponent matchComponent) {
        Long scheibe = (long) scheibennummer;
        List<MatchDO> matches = matchComponent.findByWettkampfId(wettkampfId);
        if (matches.isEmpty()) {
            return null;
        }
        TabletSessionDO tab = new TabletSessionDO();
        List<MatchDO> matchDOs = matches.stream()
                .filter(mDO -> mDO.getScheibenNummer().equals(scheibe))
                .filter(mDO -> mDO.getNr().equals(1L))
                .collect(Collectors.toList());

        tab.setMatchId(matchDOs.get(0).getId());
        tab.setSatznummer(1L);
        tab.setWettkampfId(wettkampfId);
        tab.setScheibennummer(scheibe);
        tab.setActive(false);
        return tab;
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
        checkBE(sessionDO);


        final TabletSessionBE tabBE = TabletSessionMapper.toTabletSessionBE.apply(sessionDO);
        TabletSessionBE tab2BE = tabletDAO.create(tabBE, currentUserId);
        return TabletSessionMapper.toTabletSessionDO.apply(tab2BE);
    }


    @Override
    public TabletSessionDO update(TabletSessionDO sessionDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_CURRENT_USER);
        checkBE(sessionDO);

        final TabletSessionBE passeBE = TabletSessionMapper.toTabletSessionBE.apply(sessionDO);
        TabletSessionBE tabBE = tabletDAO.update(passeBE, currentUserId);
        return TabletSessionMapper.toTabletSessionDO.apply(tabBE);
    }


    @Override
    public void delete(TabletSessionDO sessionDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_CURRENT_USER);
        checkBE(sessionDO);

        final TabletSessionBE tabBe = TabletSessionMapper.toTabletSessionBE.apply(sessionDO);
        tabletDAO.delete(tabBe, currentUserId);

    }


    private void checkBE(final TabletSessionDO sessionDO) {
        checkPreconditions(sessionDO.getScheibennummer(), PRECONDITION_FIELD_SCHEIBENNUMMER);
        checkPreconditions(sessionDO.getWettkampfId(), PRECONDITION_FIELD_WETTKAMPF_ID);
        checkPreconditions(sessionDO.getSatznummer(), "satznummer");
        checkPreconditions(sessionDO.getMatchId(), "matchId");
        Preconditions.checkNotNull(sessionDO.isActive(), String.format(PRECONDITION_MSG_TEMPLATE_NULL, "is active"));
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
