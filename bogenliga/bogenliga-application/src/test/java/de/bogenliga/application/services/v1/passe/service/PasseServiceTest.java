package de.bogenliga.application.services.v1.passe.service;

import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Jonas Winkler, HSRT MKI SS20 - SWT2
 */
public class PasseServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private PasseComponent passeComponent;

    @InjectMocks
    private PasseService underTest;

    protected static final Long MATCH_ID = 1L;
    protected static final Long MATCH_NR = 1L;
    protected static final Long MATCH_BEGEGNUNG = 1L;
    protected static final Long MATCH_WETTKAMPF_ID = 1L;
    protected static final Long MATCH_MANNSCHAFT_ID = 1L;
    protected static final Long MATCH_SCHEIBENNUMMER = 3L;
    protected static final Long MATCH_MATCHPUNKTE = 6L;
    protected static final Long MATCH_SATZPUNKTE = 3L;

    private static final Long PASSE_ID = 1L;
    private static final Long PASSE_LFDR_NR = 2L;
    private static final Integer PASSE_SCHUETZE_NR_1 = 1;
    private static final Integer PASSE_SCHUETZE_NR_2 = 2;
    private static final Long PASSE_DSB_MITGLIED_ID1 = 1L;
    private static final Long PASSE_DSB_MITGLIED_ID2 = 2L;
    private static final Integer PASSE_PFEIL_1 = 10;
    private static final Integer PASSE_PFEIL_2 = 8;

    protected PasseDO getPasseDO() {
        return new PasseDO(
                PASSE_ID,
                MATCH_MANNSCHAFT_ID,
                MATCH_WETTKAMPF_ID,
                MATCH_NR,
                MATCH_ID,
                PASSE_LFDR_NR,
                123L,
                PASSE_PFEIL_1,
                PASSE_PFEIL_2,
                null, null, null, null
        );
    }


    @Test
    public void findAll(){
        final PasseDO passeDo = getPasseDO();
        final List<PasseDO> passeDoList = Collections.singletonList(passeDo);
        //configure Mocks
        when(passeComponent.findAll()).thenReturn(passeDoList);
        // call test method
        final List<PasseDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final PasseDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(passeDo.getId());

        // verify invocations
        verify(passeComponent).findAll();

    }


}
