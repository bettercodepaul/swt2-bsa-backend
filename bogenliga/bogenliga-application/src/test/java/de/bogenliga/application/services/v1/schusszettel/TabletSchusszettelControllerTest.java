package de.bogenliga.application.services.v1.schusszettel;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bogenliga.application.business.schusszettel.api.TabletSchusszettelComponent;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.services.v1.schusszettel.model.SatzEingabeDTO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenMeldungDTO;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSchusszettelDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testet die REST-Controller-Funktionalität des TabletSchusszettelController.
 *
 * Prüft sowohl die GET- als auch POST-Endpunkte mit Fokus auf korrekte Delegation an das Component-Interface
 * sowie auf die Einhaltung der Response-Formate und Statuscodes.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelControllerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TabletSchusszettelComponent component;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TabletSchusszettelController underTest;

    private final String token = "abc123";
    private final Long wettkampfId = 1L;
    private final Long teamId = 42L;

    @Before
    public void setup() {
        underTest = new TabletSchusszettelController(component, objectMapper);
    }

    @Test
    public void getSchusszettel_shouldReturnMappedDTO() {
        TabletSchusszettelDO mockDO = new TabletSchusszettelDO();
        mockDO.setStatus(TabletSchusszettelDO.TabletSchusszettelStatus.SATZEINGABE);

        when(component.getStatus(wettkampfId, teamId, token)).thenReturn(mockDO);

        ResponseEntity<TabletSchusszettelDTO> response = underTest.getSchusszettel(token, wettkampfId, teamId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(TabletSchusszettelDTO.TabletSchusszettelStatus.SATZEINGABE);
    }

    @Test
    public void postEingabe_withSatzEingabe_shouldDelegateToComponent() {
        Map<String, Object> payload = Map.of("typ", "SATZEINGABE");

        SatzEingabeDO dummyDO = new SatzEingabeDO();
        when(objectMapper.convertValue(payload, SatzEingabeDTO.class)).thenReturn(new SatzEingabeDTO());
        when(component.submitSatz(any(), any(), any(), any())).thenReturn(null);

        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(component).submitSatz(eq(wettkampfId), eq(teamId), eq(token), any(SatzEingabeDO.class));
    }

    @Test
    public void postEingabe_withSchuetzenmeldung_shouldDelegateToComponent() {
        Map<String, Object> payload = Map.of("typ", "SCHUETZENMELDUNG");

        SchuetzenMeldungDO dummyDO = new SchuetzenMeldungDO();
        when(objectMapper.convertValue(payload, SchuetzenMeldungDTO.class)).thenReturn(new SchuetzenMeldungDTO());
        when(component.submitSchuetzen(any(), any(), any(), any())).thenReturn(null);

        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(component).submitSchuetzen(eq(wettkampfId), eq(teamId), eq(token), any(SchuetzenMeldungDO.class));
    }

    @Test
    public void postEingabe_withUnknownType_shouldReturnBadRequest() {
        Map<String, Object> payload = Map.of("typ", "UNKNOWN_TYPE");

        ResponseEntity<?> response = underTest.postEingabe(token, wettkampfId, teamId, payload);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) response.getBody()).get("error")).isEqualTo("Unbekannter Eingabetyp: UNKNOWN_TYPE");
    }
}
