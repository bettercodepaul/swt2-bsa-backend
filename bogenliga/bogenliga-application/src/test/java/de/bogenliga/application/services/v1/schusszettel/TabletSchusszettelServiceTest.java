package de.bogenliga.application.services.v1.schusszettel;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelAdminComponent;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.services.v1.schusszettel.model.SatzEingabeDTO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenMeldungDTO;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSchusszettelDTO;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSessionInfoDTO;
import de.bogenliga.application.services.v1.schusszettel.service.TabletSchusszettelService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test for TabletSchusszettelService REST controller.
 * Tests all endpoints and error scenarios to achieve full code coverage.
 *
 * @author Marty Lauterbach
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

    private final String token = "abc123";
    private final Long wettkampfId = 1L;
    private final Long teamId = 42L;

    @Before
    public void setup() {
        underTest = new TabletSchusszettelService(component, objectMapper, adminComponent);
    }

    // ================================
    // GET /v1/tablet-schusszettel Tests
    // ================================

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
    public void getSchusszettel_businessException() {
        // given
        when(component.getStatus(wettkampfId, teamId, token))
                .thenThrow(new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "no access"));

        // when
        ResponseEntity<?> response = underTest.getSchusszettel(token, wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(403);
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("NO_PERMISSION_ERROR: no access");
    }

    @Test
    public void getSchusszettel_technicalException() {
        // given
        when(component.getStatus(wettkampfId, teamId, token))
                .thenThrow(new TechnicalException(ErrorCode.INTERNAL_ERROR, "oops"));

        // when
        ResponseEntity<?> response = underTest.getSchusszettel(token, wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("INTERNAL_ERROR: oops");
    }

    @Test
    public void getSchusszettel_unexpectedException() {
        // given
        when(component.getStatus(wettkampfId, teamId, token))
                .thenThrow(new RuntimeException("Unexpected error"));

        // when
        ResponseEntity<?> response = underTest.getSchusszettel(token, wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("Ein unerwarteter Fehler ist aufgetreten");
    }

    // ================================
    // POST /v1/tablet-schusszettel Tests
    // ================================

    @Test
    public void postEingabe_satzeingabe_success() {
        // given
        Map<String, Object> payload = Map.of("typ", "SATZEINGABE");
        SatzEingabeDTO dto = new SatzEingabeDTO();
        dto.setSatzeingabe(Collections.emptyList()); // Prevent null list
        when(objectMapper.convertValue(payload, SatzEingabeDTO.class)).thenReturn(dto);

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Eingabe gespeichert");
        verify(component).submitSatz(eq(wettkampfId), eq(teamId), eq(token), any(SatzEingabeDO.class));
    }

    @Test
    public void postEingabe_schuetzenmeldung_success() {
        // given
        Map<String, Object> payload = Map.of("typ", "SCHUETZENMELDUNG");
        SchuetzenMeldungDTO dto = new SchuetzenMeldungDTO();
        dto.setGemeldeteSchuetzen(Collections.emptyList()); // Prevent null list
        when(objectMapper.convertValue(payload, SchuetzenMeldungDTO.class)).thenReturn(dto);

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Eingabe gespeichert");
        verify(component).submitSchuetzen(eq(wettkampfId), eq(teamId), eq(token), any(SchuetzenMeldungDO.class));
    }

    @Test
    public void postEingabe_missingTyp() {
        // given
        Map<String, Object> payload = Map.of();

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).asString().contains("Eingabetyp fehlt");
    }

    @Test
    public void postEingabe_unknownType() {
        // given
        Map<String, Object> payload = Map.of("typ", "UNKNOWN");

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).asString().contains("Unbekannter Eingabetyp: UNKNOWN");
    }

    @Test
    public void postEingabe_satzeingabe_businessException() {
        // given
        Map<String, Object> payload = Map.of("typ", "SATZEINGABE");
        SatzEingabeDTO dto = new SatzEingabeDTO();
        dto.setSatzeingabe(Collections.emptyList());
        when(objectMapper.convertValue(payload, SatzEingabeDTO.class)).thenReturn(dto);

        doThrow(new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "bad data"))
                .when(component)
                .submitSatz(
                        anyLong(),           // match primitive long
                        anyLong(),
                        anyString(),         // match the token
                        any(SatzEingabeDO.class)
                );

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("INVALID_ARGUMENT_ERROR: bad data");
    }


    @Test
    public void postEingabe_schuetzenmeldung_businessException() {
        // given
        Map<String, Object> payload = Map.of("typ", "SCHUETZENMELDUNG");
        SchuetzenMeldungDTO dto = new SchuetzenMeldungDTO();
        dto.setGemeldeteSchuetzen(Collections.emptyList());
        when(objectMapper.convertValue(payload, SchuetzenMeldungDTO.class)).thenReturn(dto);

        doThrow(new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, "invalid shooters"))
                .when(component)
                .submitSchuetzen(
                        anyLong(),
                        anyLong(),
                        anyString(),
                        any(SchuetzenMeldungDO.class)
                );

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("INVALID_ARGUMENT_ERROR: invalid shooters");
    }

    @Test
    public void postEingabe_satzeingabe_technicalException() {
        // given
        Map<String, Object> payload = Map.of("typ", "SATZEINGABE");
        SatzEingabeDTO dto = new SatzEingabeDTO();
        dto.setSatzeingabe(Collections.emptyList());
        when(objectMapper.convertValue(payload, SatzEingabeDTO.class)).thenReturn(dto);

        doThrow(new TechnicalException(ErrorCode.INTERNAL_ERROR, "database error"))
                .when(component)
                .submitSatz(
                        anyLong(),
                        anyLong(),
                        anyString(),
                        any(SatzEingabeDO.class)
                );

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("INTERNAL_ERROR: database error");
    }


    @Test
    public void postEingabe_schuetzenmeldung_technicalException() {
        // given
        Map<String, Object> payload = Map.of("typ", "SCHUETZENMELDUNG");
        SchuetzenMeldungDTO dto = new SchuetzenMeldungDTO();
        dto.setGemeldeteSchuetzen(Collections.emptyList());
        when(objectMapper.convertValue(payload, SchuetzenMeldungDTO.class)).thenReturn(dto);

        doThrow(new TechnicalException(ErrorCode.INTERNAL_ERROR, "database error"))
                .when(component)
                .submitSchuetzen(
                        anyLong(),
                        anyLong(),
                        anyString(),
                        any(SchuetzenMeldungDO.class)
                );

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("INTERNAL_ERROR: database error");
    }

    @Test
    public void postEingabe_unexpectedException() {
        // given
        Map<String, Object> payload = Map.of("typ", "SATZEINGABE");
        when(objectMapper.convertValue(payload, SatzEingabeDTO.class))
                .thenThrow(new RuntimeException("Unexpected error"));

        // when
        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("Ein unerwarteter Fehler ist aufgetreten");
    }

    // ================================
    // POST /v1/tablet-schusszettel/tokenize Tests
    // ================================

    @Test
    public void reTokenize_success() {
        // given - no additional setup needed

        // when
        ResponseEntity<?> response = underTest.reTokenize(wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("message")).isEqualTo("Schusszettel erfolgreich neu tokenisiert");
        verify(adminComponent).reTokenize(wettkampfId, teamId);
    }

    @Test
    public void reTokenize_businessException() {
        // given
        doThrow(new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "no permission"))
                .when(adminComponent).reTokenize(wettkampfId, teamId);

        // when
        ResponseEntity<?> response = underTest.reTokenize(wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(403);
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("NO_PERMISSION_ERROR: no permission");
    }

    @Test
    public void reTokenize_technicalException() {
        // given
        doThrow(new TechnicalException(ErrorCode.INTERNAL_ERROR, "database error"))
                .when(adminComponent).reTokenize(wettkampfId, teamId);

        // when
        ResponseEntity<?> response = underTest.reTokenize(wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("INTERNAL_ERROR: database error");
    }

    @Test
    public void reTokenize_unexpectedException() {
        // given
        doThrow(new RuntimeException("Unexpected error"))
                .when(adminComponent).reTokenize(wettkampfId, teamId);

        // when
        ResponseEntity<?> response = underTest.reTokenize(wettkampfId, teamId);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("Ein unerwarteter Fehler ist aufgetreten");
    }

    // ================================
    // GET /v1/tablet-schusszettel/sessions Tests
    // ================================

    @Test
    public void getTabletSessionInfo_success_existingSessions() {
        // given
        when(adminComponent.existsForWettkampf(wettkampfId)).thenReturn(true);
        TabletSessionInfoDO mockDO = new TabletSessionInfoDO();
        mockDO.setWettkampfId(wettkampfId);
        mockDO.setTabletSessionSingDOs(new de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO[0]); // Empty array, not null
        when(adminComponent.generateSchusszettelSessions(wettkampfId)).thenReturn(mockDO);

        // when
        ResponseEntity<?> response = underTest.getTabletSessionInfo(wettkampfId);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(TabletSessionInfoDTO.class);
        verify(adminComponent).existsForWettkampf(wettkampfId);
        verify(adminComponent).generateSchusszettelSessions(wettkampfId);
        verify(adminComponent, never()).initializeForWettkampf(wettkampfId);
    }

    @Test
    public void getTabletSessionInfo_success_initializeFirst() {
        // given
        when(adminComponent.existsForWettkampf(wettkampfId)).thenReturn(false);
        TabletSessionInfoDO mockDO = new TabletSessionInfoDO();
        mockDO.setWettkampfId(wettkampfId);
        mockDO.setTabletSessionSingDOs(new de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO[0]); // Empty array, not null
        when(adminComponent.generateSchusszettelSessions(wettkampfId)).thenReturn(mockDO);

        // when
        ResponseEntity<?> response = underTest.getTabletSessionInfo(wettkampfId);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(TabletSessionInfoDTO.class);
        verify(adminComponent).existsForWettkampf(wettkampfId);
        verify(adminComponent).initializeForWettkampf(wettkampfId);
        verify(adminComponent).generateSchusszettelSessions(wettkampfId);
    }

    @Test
    public void getTabletSessionInfo_businessException() {
        // given
        when(adminComponent.existsForWettkampf(wettkampfId)).thenReturn(true);
        when(adminComponent.generateSchusszettelSessions(wettkampfId))
                .thenThrow(new BusinessException(ErrorCode.NO_PERMISSION_ERROR, "no access"));

        // when
        ResponseEntity<?> response = underTest.getTabletSessionInfo(wettkampfId);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(403);
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("NO_PERMISSION_ERROR: no access");
    }

    @Test
    public void getTabletSessionInfo_technicalException() {
        // given
        when(adminComponent.existsForWettkampf(wettkampfId)).thenReturn(true);
        when(adminComponent.generateSchusszettelSessions(wettkampfId))
                .thenThrow(new TechnicalException(ErrorCode.INTERNAL_ERROR, "database error"));

        // when
        ResponseEntity<?> response = underTest.getTabletSessionInfo(wettkampfId);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("INTERNAL_ERROR: database error");
    }

    @Test
    public void getTabletSessionInfo_unexpectedException() {
        // given
        when(adminComponent.existsForWettkampf(wettkampfId)).thenReturn(true);
        when(adminComponent.generateSchusszettelSessions(wettkampfId))
                .thenThrow(new RuntimeException("Unexpected error"));

        // when
        ResponseEntity<?> response = underTest.getTabletSessionInfo(wettkampfId);

        // then
        assertThat(response.getStatusCode().is5xxServerError()).isTrue();
        assertThat(response.getBody()).isNotNull();
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body.get("error")).isEqualTo("Ein unerwarteter Fehler ist aufgetreten");
    }
}