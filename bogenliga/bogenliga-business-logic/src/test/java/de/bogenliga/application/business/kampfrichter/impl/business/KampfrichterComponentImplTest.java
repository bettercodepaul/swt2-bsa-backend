package de.bogenliga.application.business.kampfrichter.impl.business;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.dao.KampfrichterDAO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Rahul Pöse
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class KampfrichterComponentImplTest {

    private static final Long USER = 0L;
    private static final Long VERSION = 0L;

    private static final Long USERID = 1337L;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = true;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private KampfrichterDAO kampfrichterDAO;
    @InjectMocks
   // private KampfrichterComponentImpl underTest;
    @Captor
    private ArgumentCaptor<KampfrichterBE> kampfrichterBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static KampfrichterBE getKampfrichterBE() {
        final KampfrichterBE expectedBE = new KampfrichterBE();
        expectedBE.setKampfrichterUserId(USERID);
        expectedBE.setKampfrichterWettkampfId(WETTKAMPFID);
        expectedBE.setKampfrichterLeitend(LEITEND);

        return expectedBE;
    }


    public static KampfrichterDO getKampfrichterDO() {
        return new KampfrichterDO(
                USERID,
                WETTKAMPFID,
                LEITEND);
    }
}