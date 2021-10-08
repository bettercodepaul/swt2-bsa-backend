package de.bogenliga.application.business.disziplin.impl;

import java.util.HashMap;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.disziplin.impl.entity.DisziplinBE;

/**
 *
 * @author Nick Kerschagel, HSRT
 */
public abstract class BaseDisziplinTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    protected static final Long DISZIPLIN_ID = 0L;
    protected static final String DISZIPLIN_NAME = "testname";

    protected static final Long CURRENT_USER_ID = 1L;
    private HashMap<String, Object> valuesToMethodMap = new HashMap<>();

    protected DisziplinBE getDisziplinBE() {
        DisziplinBE disziplinBE = new DisziplinBE();
        disziplinBE.setName(DISZIPLIN_NAME);
        disziplinBE.setId(DISZIPLIN_ID);

        return disziplinBE;
    }

    public static DisziplinDO getDisziplinDO() {

        return new DisziplinDO(DISZIPLIN_ID,
                DISZIPLIN_NAME);

    }

    public BaseDisziplinTest() {
        valuesToMethodMap.put("getId", DISZIPLIN_ID);
        valuesToMethodMap.put("getName", DISZIPLIN_NAME);
    }

    public HashMap<String, Object> getValuesToMethodMap() {
        return valuesToMethodMap;
    }

}
