package de.bogenliga.application.services.v1.kampfrichter.service;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.kampfrichter.api.KampfrichterComponent;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterExtendedDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * @author Emanuel-Inf
 *
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class KampfrichterServiceTest {

    private static final Long USER_ID = 1L;
    private static final String VORNAME ="ERWIN";
    private static final String NACHNAME = "SMITH";
    private static final String EMAIL ="ERWIN.SMITH@test.de";
    private static final Long WETTKAMPFID = 42L;
    private static final boolean LEITEND = false;
    private static final Long NEGATIVE_WETTKAMPFID = -42L;
    private static final String PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID_NEGATIVE = "Wettkampf-ID must not be negative.";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private KampfrichterComponent kampfrichterComponent;

    @InjectMocks
    private KampfrichterService underTest;

    public static KampfrichterDO getKampfrichterDO(){
        return new KampfrichterDO(
                USER_ID,
                VORNAME,
                NACHNAME,
                EMAIL,
                WETTKAMPFID,
                LEITEND
        );
    }

    @Test
    public void test_findByWettkampfidNotInWettkampftag() {

        KampfrichterDO kampfrichterDO = getKampfrichterDO();
        List<KampfrichterDO> kampfrichterDOList = Collections.singletonList(kampfrichterDO);

        // configure mocks
        when(kampfrichterComponent.findByWettkampfidNotInWettkampftag(anyLong())).thenReturn(kampfrichterDOList);

        // call test method
        List<KampfrichterExtendedDTO> actual = underTest.findByWettkampfidNotInWettkampftag(anyLong());

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        KampfrichterExtendedDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getUserID()).isEqualTo(kampfrichterDO.getUserId());
        assertThat(actualDTO.getKampfrichterVorname()).isEqualTo(kampfrichterDO.getVorname());
        assertThat(actualDTO.getKampfrichterNachname()).isEqualTo(kampfrichterDO.getNachname());
        assertThat(actualDTO.getEmail()).isEqualTo(kampfrichterDO.getKampfrichterEmail());
        assertThat(actualDTO.getWettkampfID()).isEqualTo(kampfrichterDO.getWettkampfId());
        assertThat(actualDTO.getLeitend()).isEqualTo(kampfrichterDO.isLeitend());

        // verify invocations
        verify(kampfrichterComponent).findByWettkampfidNotInWettkampftag(anyLong());
    }

    @Test
    public void test_findByWettkampfidInWettkampftag() {

        KampfrichterDO kampfrichterDO = getKampfrichterDO();
        List<KampfrichterDO> kampfrichterDOList = Collections.singletonList(kampfrichterDO);

        // configure mocks
        when(kampfrichterComponent.findByWettkampfidInWettkampftag(anyLong())).thenReturn(kampfrichterDOList);

        // call test method
        List<KampfrichterExtendedDTO> actual = underTest.findByWettkampfidInWettkampftag(anyLong());

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        KampfrichterExtendedDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getUserID()).isEqualTo(kampfrichterDO.getUserId());
        assertThat(actualDTO.getKampfrichterVorname()).isEqualTo(kampfrichterDO.getVorname());
        assertThat(actualDTO.getKampfrichterNachname()).isEqualTo(kampfrichterDO.getNachname());
        assertThat(actualDTO.getEmail()).isEqualTo(kampfrichterDO.getKampfrichterEmail());
        assertThat(actualDTO.getWettkampfID()).isEqualTo(kampfrichterDO.getWettkampfId());
        assertThat(actualDTO.getLeitend()).isEqualTo(kampfrichterDO.isLeitend());

        // verify invocations
        verify(kampfrichterComponent).findByWettkampfidInWettkampftag(anyLong());
    }
    @Test
    public void test_findByWettkampfidNotInWettkampftag_Precondition(){


        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByWettkampfidNotInWettkampftag(NEGATIVE_WETTKAMPFID))
                .withMessageContaining(PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID_NEGATIVE)
                .withNoCause();

    }

    @Test
    public void test_findByWettkampfidInWettkampftag_Precondition(){

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByWettkampfidInWettkampftag(NEGATIVE_WETTKAMPFID))
                .withMessageContaining(PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID_NEGATIVE)
                .withNoCause();
    }
}