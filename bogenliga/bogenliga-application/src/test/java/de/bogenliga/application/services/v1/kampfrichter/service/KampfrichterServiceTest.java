package de.bogenliga.application.services.v1.kampfrichter.service;

import java.security.Principal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.kampfrichter.api.KampfrichterComponent;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterDTO;
import static org.assertj.core.api.Assertions.assertThat;
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
public class KampfrichterServiceTest {

    private static final long USER = 0;

    private static final long USERID = 1337;
    private static final long WETTKAMPFID = 9999;
    private static final boolean LEITEND = true;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private KampfrichterComponent kampfrichterComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private KampfrichterService underTest;

    @Captor
    private ArgumentCaptor<KampfrichterDO> kampfrichterVOArgumentCaptor;


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


    private static KampfrichterDTO getKampfrichterDTO() {
        final KampfrichterDTO kampfrichterDTO = new KampfrichterDTO();
        kampfrichterDTO.setUserId(USERID);
        kampfrichterDTO.setWettkampfId(WETTKAMPFID);
        kampfrichterDTO.setLeitend(LEITEND);

        return kampfrichterDTO;
    }


    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void create() {
        // prepare test data
        final KampfrichterDTO input = getKampfrichterDTO();

        final KampfrichterDO expected = getKampfrichterDO();

        // configure mocks
        when(kampfrichterComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final KampfrichterDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getUserId()).isEqualTo(input.getUserId());
        assertThat(actual.getWettkampfId()).isEqualTo(input.getWettkampfId());

        // verify invocations
        verify(kampfrichterComponent).create(kampfrichterVOArgumentCaptor.capture(), anyLong());

        final KampfrichterDO createdKampfrichter = kampfrichterVOArgumentCaptor.getValue();

        assertThat(createdKampfrichter).isNotNull();
        assertThat(createdKampfrichter.getUserId()).isEqualTo(input.getUserId());
    }


    @Test
    public void update() {
        // prepare test data
        final KampfrichterDTO input = getKampfrichterDTO();

        final KampfrichterDO expected = getKampfrichterDO();

        // configure mocks
        when(kampfrichterComponent.update(any(), anyLong())).thenReturn(expected);

        // call test method
        final KampfrichterDTO actual = underTest.update(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getUserId()).isEqualTo(input.getUserId());
        assertThat(actual.getWettkampfId()).isEqualTo(input.getWettkampfId());

        // verify invocations
        verify(kampfrichterComponent).update(kampfrichterVOArgumentCaptor.capture(), anyLong());

        final KampfrichterDO updatedKampfrichter = kampfrichterVOArgumentCaptor.getValue();

        assertThat(updatedKampfrichter).isNotNull();
        assertThat(updatedKampfrichter.getUserId()).isEqualTo(input.getUserId());
    }


    @Test
    public void delete() {
        // prepare test data
        final KampfrichterDO expected = getKampfrichterDO();

        // configure mocks

        // call test method
        underTest.delete(USERID, principal);

        // assert result

        // verify invocations
        verify(kampfrichterComponent).delete(kampfrichterVOArgumentCaptor.capture(), anyLong());

        final KampfrichterDO deletedKampfrichter = kampfrichterVOArgumentCaptor.getValue();

        assertThat(deletedKampfrichter).isNotNull();
        assertThat(deletedKampfrichter.getUserId()).isEqualTo(expected.getUserId());
    }
}