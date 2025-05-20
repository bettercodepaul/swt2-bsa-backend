package de.bogenliga.application.services.v1.schusszettel;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelAdminComponent;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSchusszettelDTO;
import de.bogenliga.application.services.v1.schusszettel.service.TabletSchusszettelService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Testet die REST-Controller-Funktionalität des TabletSchusszettelController.
 * @Marty Lauterbach
 */
public class TabletSchusszettelServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TabletSchusszettelComponent component;

    @Mock
    private TabletSchusszettelAdminComponent adminComponent;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TabletSchusszettelService underTest;

    private final String token      = "abc123";
    private final Long   wettkampfId = 1L;
    private final Long   teamId      = 42L;

    @Before
    public void setup() {

        // TODO get actual live database allowed parameters on a test entry (or create and delete an entry
        // manually at test start?)

        underTest = new TabletSchusszettelService(component, objectMapper, adminComponent);
    }

    @Test
    public void getSchusszettel_success() {
        // given
        TabletSchusszettelDO mockDO = new TabletSchusszettelDO();
        mockDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);
        when(component.getStatus(wettkampfId, teamId, token)).thenReturn(mockDO);

        // when
        ResponseEntity<?> response = underTest.getSchusszettel(token, wettkampfId, teamId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(TabletSchusszettelDTO.class);
    }

    @Test
    public void getSchusszettel_noPermission() {
        // given
        when(component.getStatus(wettkampfId, teamId, token))
                .thenThrow(new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "no access"));

        // when
        ResponseEntity<?> response = underTest.getSchusszettel(token, wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(((Map<?, ?>) response.getBody()).get("error")).isEqualTo("NO_PERMISSION_ERROR: no access");
    }
    @Test
    public void getSchusszettel_internalError() {
        // given
        when(component.getStatus(wettkampfId, teamId, token))
                .thenThrow(new TechnicalException(ErrorCode.INTERNAL_ERROR, "oops"));

        // when
        ResponseEntity<?> response = underTest.getSchusszettel(token, wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
    }

    /*

    @Test
    public void postEingabe_satzSuccess() {
        // given
        Map<String, Object> payload = Map.of("typ", "SATZEINGABE");
        SatzEingabeDTO dto = new SatzEingabeDTO();
        when(objectMapper.convertValue(payload, SatzEingabeDTO.class)).thenReturn(dto);

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(component).submitSatz(eq(wettkampfId), eq(teamId), eq(token), any(SatzEingabeDO.class));
    }

    @Test
    public void postEingabe_satzBusinessError() {
        // given
        Map<String, Object> payload = Map.of("typ", "SATZEINGABE");
        when(objectMapper.convertValue(payload, SatzEingabeDTO.class))
                .thenReturn(new SatzEingabeDTO());
        doThrow(new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "bad data"))
                .when(component).submitSatz(any(), any(), any(), any());

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(((Map<?, ?>) response.getBody()).get("error")).isEqualTo("bad data");
    }

    @Test
    public void postEingabe_satzTechnicalError() {
        // given
        Map<String, Object> payload = Map.of("typ", "SATZEINGABE");
        when(objectMapper.convertValue(payload, SatzEingabeDTO.class))
                .thenReturn(new SatzEingabeDTO());
        doThrow(new TechnicalException(ErrorCode.INTERNAL_ERROR, "oops"))
                .when(component).submitSatz(any(), any(), any(), any());

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
    }

    @Test
    public void postEingabe_meldungSuccess() {
        // given
        Map<String, Object> payload = Map.of("typ", "SCHUETZENMELDUNG");
        SchuetzenMeldungDTO dto = new SchuetzenMeldungDTO();
        when(objectMapper.convertValue(payload, SchuetzenMeldungDTO.class)).thenReturn(dto);

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(component).submitSchuetzen(eq(wettkampfId), eq(teamId), eq(token), any(SchuetzenMeldungDO.class));
    }

     */

    @Test
    public void postEingabe_unknownType() {
        // given
        Map<String, Object> payload = Map.of("typ", "UNKNOWN");

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).asString()
                .contains("Unbekannter Eingabetyp");
    }

    /*

    @Test
    public void postEingabe_missingTyp() {
        // given
        Map<String, Object> payload = Map.of();

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).asString()
                .contains("Eingabetyp fehlt");
    }

     */
}