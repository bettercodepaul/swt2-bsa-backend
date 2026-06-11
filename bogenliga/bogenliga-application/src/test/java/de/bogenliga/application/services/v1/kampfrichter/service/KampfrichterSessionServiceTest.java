package de.bogenliga.application.services.v1.kampfrichter.service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.ResponseEntity;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.kampfrichter.impl.dao.KampfrichterSessionDAO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterSessionEntity;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.schusszettel.impl.dao.TabletSchusszettelDAO;
import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterMatchDTO;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterStrafpunkteRequestDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class KampfrichterSessionServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private KampfrichterSessionDAO     sessionDAO;
    @Mock private MatchComponent             matchComponent;
    @Mock private DsbMannschaftComponent     mannschaftComponent;
    @Mock private TabletSchusszettelDAO      tabletSessionDAO;
    @Mock private MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    @Mock private DsbMitgliedComponent       dsbMitgliedComponent;
    @Mock private Principal                  principal;

    private KampfrichterSessionService underTest;

    private static final Long   WETTKAMPF_ID  = 42L;
    private static final Long   MATCH_ID      = 7L;
    private static final Long   MANNSCHAFT_ID = 10L;
    private static final String VALID_TOKEN   = "test-token-abc";

    // ── factories ─────────────────────────────────────────────────────────────

    private KampfrichterSessionEntity sessionEntity(String token) {
        KampfrichterSessionEntity e = new KampfrichterSessionEntity();
        e.setId(1L);
        e.setWettkampfId(WETTKAMPF_ID);
        e.setToken(token);
        return e;
    }

    private MatchDO matchDO() {
        MatchDO m = new MatchDO();
        m.setId(MATCH_ID);
        m.setNr(1L);
        m.setBegegnung(1L);
        m.setMatchScheibennummer(3L);
        m.setMannschaftId(MANNSCHAFT_ID);
        m.setWettkampfId(WETTKAMPF_ID);
        m.setStrafPunkteSatz1(0L);
        m.setStrafPunkteSatz2(0L);
        m.setStrafPunkteSatz3(0L);
        m.setStrafPunkteSatz4(0L);
        m.setStrafPunkteSatz5(0L);
        return m;
    }

    private TabletSchusszettelEntity sessionForTeam(Long teamId, String status) {
        TabletSchusszettelEntity e = new TabletSchusszettelEntity();
        e.setTeamId(teamId);
        e.setStatus(status);
        return e;
    }

    private DsbMannschaftDO mannschaft(String name) {
        DsbMannschaftDO d = new DsbMannschaftDO();
        d.setName(name);
        return d;
    }

    @Before
    public void setUp() {
        when(principal.getName()).thenReturn("1");
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(any())).thenReturn(Collections.emptyList());
        underTest = new KampfrichterSessionService(
                sessionDAO, matchComponent, mannschaftComponent, tabletSessionDAO,
                mannschaftsmitgliedComponent, dsbMitgliedComponent);
    }

    // ── getOrCreateToken ──────────────────────────────────────────────────────

    @Test
    public void getOrCreateToken_returnsExistingToken() {
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));

        ResponseEntity<Map<String, String>> resp =
                underTest.getOrCreateToken(WETTKAMPF_ID, principal);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody()).containsKey("token");
        assertThat(resp.getBody().get("token")).isEqualTo(VALID_TOKEN);
        verify(sessionDAO, never()).create(any(), anyLong());
    }

    @Test
    public void getOrCreateToken_createsNewTokenWhenNoneExists() {
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Optional.empty());
        KampfrichterSessionEntity created = sessionEntity("new-token");
        when(sessionDAO.create(any(), anyLong())).thenReturn(created);

        ResponseEntity<Map<String, String>> resp =
                underTest.getOrCreateToken(WETTKAMPF_ID, principal);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody().get("token")).isEqualTo("new-token");
        verify(sessionDAO).create(any(KampfrichterSessionEntity.class), anyLong());
    }

    // ── regenerateToken ───────────────────────────────────────────────────────

    @Test
    public void regenerateToken_updatesExistingSession() {
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));

        ResponseEntity<Map<String, String>> resp =
                underTest.regenerateToken(WETTKAMPF_ID, principal);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody()).containsKey("token");
        verify(sessionDAO).update(any(KampfrichterSessionEntity.class), anyLong());
    }

    @Test
    public void regenerateToken_createsNewSessionWhenNoneExists() {
        when(sessionDAO.findByWettkampfId(WETTKAMPF_ID)).thenReturn(Optional.empty());
        when(sessionDAO.create(any(), anyLong())).thenReturn(sessionEntity("brand-new"));

        ResponseEntity<Map<String, String>> resp =
                underTest.regenerateToken(WETTKAMPF_ID, principal);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        verify(sessionDAO).create(any(), anyLong());
    }

    // ── getMatches ────────────────────────────────────────────────────────────

    @Test
    public void getMatches_returnsMatchesWithSessionStatus() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(matchDO()));
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(sessionForTeam(MANNSCHAFT_ID, "SATZEINGABE")));
        when(mannschaftComponent.findById(MANNSCHAFT_ID))
                .thenReturn(mannschaft("Team Alpha"));

        ResponseEntity<List<KampfrichterMatchDTO>> resp =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody()).hasSize(1);
        KampfrichterMatchDTO dto = resp.getBody().get(0);
        assertThat(dto.getMatchId()).isEqualTo(MATCH_ID);
        assertThat(dto.getMatchScheibennummer()).isEqualTo(3L);
        assertThat(dto.getMannschaftName()).isEqualTo("Team Alpha");
        assertThat(dto.getSessionStatus()).isEqualTo("SATZEINGABE");
    }

    @Test
    public void getMatches_setsSchuetzenmeldungWhenTeamNotRegistered() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(matchDO()));
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(sessionForTeam(MANNSCHAFT_ID, "SCHUETZENMELDUNG")));
        when(mannschaftComponent.findById(MANNSCHAFT_ID))
                .thenReturn(mannschaft("Team Beta"));

        ResponseEntity<List<KampfrichterMatchDTO>> resp =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN);

        assertThat(resp.getBody().get(0).getSessionStatus()).isEqualTo("SCHUETZENMELDUNG");
    }

    @Test
    public void getMatches_setsUnbekanntWhenNoSessionForTeam() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(matchDO()));
        // no tablet session for this team
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.emptyList());
        when(mannschaftComponent.findById(MANNSCHAFT_ID))
                .thenReturn(mannschaft("Team C"));

        ResponseEntity<List<KampfrichterMatchDTO>> resp =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN);

        assertThat(resp.getBody().get(0).getSessionStatus()).isEqualTo("UNBEKANNT");
    }

    @Test
    public void getMatches_returnsEmptyListWhenNoMatchesExist() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.emptyList());
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<KampfrichterMatchDTO>> resp =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN);

        assertThat(resp.getBody()).isEmpty();
    }

    @Test
    public void getMatches_throwsBusinessExceptionOnInvalidToken() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, "bad-token"))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getMatches(WETTKAMPF_ID, "bad-token"))
                .withMessageContaining("Ungültiger Token");
    }

    @Test
    public void getMatches_handlesMissingMannschaftNameGracefully() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(matchDO()));
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(sessionForTeam(MANNSCHAFT_ID, "SATZEINGABE")));
        when(mannschaftComponent.findById(MANNSCHAFT_ID))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<List<KampfrichterMatchDTO>> resp =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody().get(0).getMannschaftName()).isEqualTo("");
    }

    // ── updateStrafpunkte ─────────────────────────────────────────────────────

    @Test
    public void updateStrafpunkte_savesAllFiveSaetze() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        MatchDO match = matchDO();
        when(matchComponent.findById(MATCH_ID)).thenReturn(match);

        KampfrichterStrafpunkteRequestDTO request = new KampfrichterStrafpunkteRequestDTO();
        request.setMatchId(MATCH_ID);
        request.setStrafPunkteSatz1(1L);
        request.setStrafPunkteSatz2(2L);
        request.setStrafPunkteSatz3(3L);
        request.setStrafPunkteSatz4(4L);
        request.setStrafPunkteSatz5(5L);

        ResponseEntity<Map<String, String>> resp =
                underTest.updateStrafpunkte(WETTKAMPF_ID, VALID_TOKEN, request);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody().get("message")).isEqualTo("Strafpunkte gespeichert");
        verify(matchComponent).update(argThat(m ->
                m.getStrafPunkteSatz1() == 1L &&
                m.getStrafPunkteSatz2() == 2L &&
                m.getStrafPunkteSatz3() == 3L &&
                m.getStrafPunkteSatz4() == 4L &&
                m.getStrafPunkteSatz5() == 5L
        ), eq(0L));
    }

    @Test
    public void updateStrafpunkte_throwsOnInvalidToken() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, "bad"))
                .thenReturn(Optional.empty());

        KampfrichterStrafpunkteRequestDTO req = new KampfrichterStrafpunkteRequestDTO();
        req.setMatchId(MATCH_ID);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateStrafpunkte(WETTKAMPF_ID, "bad", req));
    }

    @Test
    public void updateStrafpunkte_throwsWhenMatchBelongsToDifferentWettkampf() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        MatchDO match = matchDO();
        match.setWettkampfId(99L); // different wettkampf
        when(matchComponent.findById(MATCH_ID)).thenReturn(match);

        KampfrichterStrafpunkteRequestDTO req = new KampfrichterStrafpunkteRequestDTO();
        req.setMatchId(MATCH_ID);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateStrafpunkte(WETTKAMPF_ID, VALID_TOKEN, req))
                .withMessageContaining("Match nicht gefunden");
    }

    @Test
    public void updateStrafpunkte_throwsWhenMatchNotFound() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        when(matchComponent.findById(MATCH_ID)).thenReturn(null);

        KampfrichterStrafpunkteRequestDTO req = new KampfrichterStrafpunkteRequestDTO();
        req.setMatchId(MATCH_ID);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateStrafpunkte(WETTKAMPF_ID, VALID_TOKEN, req));
    }

    // ── loadSchuetzen ─────────────────────────────────────────────────────────

    @Test
    public void getMatches_includesSchuetzenWithRueckennummerAndName() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(matchDO()));
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(sessionForTeam(MANNSCHAFT_ID, "SCHUETZENMELDUNG")));
        when(mannschaftComponent.findById(MANNSCHAFT_ID)).thenReturn(mannschaft("Team X"));

        MannschaftsmitgliedDO mitglied = new MannschaftsmitgliedDO(
                1L, MANNSCHAFT_ID, 99L, 1, null, null, null, null, null, null, null, 5L);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(MANNSCHAFT_ID))
                .thenReturn(List.of(mitglied));

        DsbMitgliedDO dsbMitglied = new DsbMitgliedDO();
        dsbMitglied.setId(99L);
        dsbMitglied.setVorname("Max");
        dsbMitglied.setNachname("Mustermann");
        when(dsbMitgliedComponent.findById(99L)).thenReturn(dsbMitglied);

        KampfrichterMatchDTO dto =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN).getBody().get(0);

        assertThat(dto.getSchuetzen()).hasSize(1);
        assertThat(dto.getSchuetzen().get(0).getRueckennummer()).isEqualTo(5);
        assertThat(dto.getSchuetzen().get(0).getVorname()).isEqualTo("Max");
        assertThat(dto.getSchuetzen().get(0).getNachname()).isEqualTo("Mustermann");
    }

    @Test
    public void getMatches_returnsEmptySchuetzenListWhenMannschaftIdIsNull() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));

        MatchDO matchWithNullMannschaft = matchDO();
        matchWithNullMannschaft.setMannschaftId(null);
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(matchWithNullMannschaft));
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(Collections.emptyList());

        KampfrichterMatchDTO dto =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN).getBody().get(0);

        assertThat(dto.getSchuetzen()).isEmpty();
    }

    @Test
    public void getMatches_returnsEmptySchuetzenListWhenDsbMitgliedThrows() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(matchDO()));
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(sessionForTeam(MANNSCHAFT_ID, "SCHUETZENMELDUNG")));
        when(mannschaftComponent.findById(MANNSCHAFT_ID)).thenReturn(mannschaft("Team Y"));

        MannschaftsmitgliedDO mitglied = new MannschaftsmitgliedDO(
                1L, MANNSCHAFT_ID, 99L, 1, null, null, null, null, null, null, null, 2L);
        when(mannschaftsmitgliedComponent.findAllSchuetzeInTeamEingesetzt(MANNSCHAFT_ID))
                .thenReturn(List.of(mitglied));
        when(dsbMitgliedComponent.findById(99L))
                .thenThrow(new RuntimeException("DB unavailable"));

        KampfrichterMatchDTO dto =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN).getBody().get(0);

        assertThat(dto.getSchuetzen()).isEmpty();
    }

    // ── KampfrichterMatchDTO field coverage ───────────────────────────────────

    @Test
    public void getMatches_mapsAllDtoFieldsCorrectly() {
        when(sessionDAO.findByWettkampfIdAndToken(WETTKAMPF_ID, VALID_TOKEN))
                .thenReturn(Optional.of(sessionEntity(VALID_TOKEN)));

        MatchDO m = matchDO();
        m.setNr(3L);
        m.setBegegnung(2L);
        m.setMatchScheibennummer(5L);
        m.setStrafPunkteSatz1(10L);
        m.setStrafPunkteSatz2(20L);
        m.setStrafPunkteSatz3(30L);
        m.setStrafPunkteSatz4(40L);
        m.setStrafPunkteSatz5(50L);
        when(matchComponent.findByWettkampfId(WETTKAMPF_ID)).thenReturn(List.of(m));
        when(tabletSessionDAO.findByWettkampfId(WETTKAMPF_ID))
                .thenReturn(List.of(sessionForTeam(MANNSCHAFT_ID, "WARTE")));
        when(mannschaftComponent.findById(MANNSCHAFT_ID)).thenReturn(mannschaft("SV Test"));

        KampfrichterMatchDTO dto =
                underTest.getMatches(WETTKAMPF_ID, VALID_TOKEN).getBody().get(0);

        assertThat(dto.getNr()).isEqualTo(3L);
        assertThat(dto.getBegegnung()).isEqualTo(2L);
        assertThat(dto.getMatchScheibennummer()).isEqualTo(5L);
        assertThat(dto.getMannschaftId()).isEqualTo(MANNSCHAFT_ID);
        assertThat(dto.getMannschaftName()).isEqualTo("SV Test");
        assertThat(dto.getStrafPunkteSatz1()).isEqualTo(10L);
        assertThat(dto.getStrafPunkteSatz2()).isEqualTo(20L);
        assertThat(dto.getStrafPunkteSatz3()).isEqualTo(30L);
        assertThat(dto.getStrafPunkteSatz4()).isEqualTo(40L);
        assertThat(dto.getStrafPunkteSatz5()).isEqualTo(50L);
        assertThat(dto.getSessionStatus()).isEqualTo("WARTE");
    }
}
