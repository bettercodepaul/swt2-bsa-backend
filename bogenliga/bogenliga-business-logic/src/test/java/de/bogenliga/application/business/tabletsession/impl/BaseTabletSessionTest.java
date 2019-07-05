package de.bogenliga.application.business.tabletsession.impl;

import java.util.HashMap;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.tabletsession.api.types.TabletSessionDO;
import de.bogenliga.application.business.tabletsession.impl.entity.TabletSessionBE;

/**
 * TODO [AL] class documentation
 *
 * @author Dominik Halle & Kay Scheerer, HSRT MKI SS19 - SWT2
 */
public abstract class BaseTabletSessionTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    protected static final Long SCHEIBENNUMMER = 5L;
    protected static final Long WETTKAMPF_ID = 1234L;
    protected static final Long SATZNUMMER = 4L;
    protected static final Long MATCH_ID = 150L;
    protected static final Boolean IS_ACTIVE = Boolean.TRUE;


    protected static final Long CURRENT_USER_ID = 12L;
    private HashMap<String, Object> valuesToMethodMap = new HashMap<>();


    protected TabletSessionBE getTabletSessionBE() {
        TabletSessionBE tabBE = new TabletSessionBE();
        tabBE.setScheibennummer(SCHEIBENNUMMER);
        tabBE.setWettkampfId(WETTKAMPF_ID);
        tabBE.setMatchId(MATCH_ID);
        tabBE.setSatznummer(SATZNUMMER);
        tabBE.setActive(IS_ACTIVE);
        return tabBE;
    }


    public static TabletSessionDO getTabletSessionDO() {
        return new TabletSessionDO(WETTKAMPF_ID, SCHEIBENNUMMER, SATZNUMMER, MATCH_ID, IS_ACTIVE);
    }


    public BaseTabletSessionTest() {
        valuesToMethodMap.put("getWettkampfId", WETTKAMPF_ID);
        valuesToMethodMap.put("getScheibennummer", SCHEIBENNUMMER);
        valuesToMethodMap.put("getSatznummer", SATZNUMMER);
        valuesToMethodMap.put("getMatchId", MATCH_ID);
        valuesToMethodMap.put("getIsActive", IS_ACTIVE);
    }


    public HashMap<String, Object> getValuesToMethodMap() {
        return valuesToMethodMap;
    }
}
