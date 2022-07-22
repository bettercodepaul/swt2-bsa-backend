package de.bogenliga.application.services.v1.Sync.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import org.junit.Test;
import de.bogenliga.application.services.v1.Sync.service.SyncServiceTest;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;
import de.bogenliga.application.services.v1.sync.model.SyncWrapper;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jonas Sigloch
 */
public class SyncWrapperTest {
    private static final List<MannschaftsMitgliedDTO> mitgliedList;

    static {
        mitgliedList = new ArrayList<>(singletonList(
                new MannschaftsMitgliedDTO(1000L, 2L, 1001L, 1, 3L)));
    }

    private static final List<MatchDTO> matchList = (List<MatchDTO>) singletonList(SyncServiceTest.getLigaSyncMatchDTO());

    private static final String offlineToken = "testToken";
    private static final long wettkampfId = 1009L;


    private SyncWrapper getSyncWrapper() {
        return new SyncWrapper(wettkampfId, offlineToken, matchList, mitgliedList);
    }

    @Test
    public void checkWrapper() {
        SyncWrapper undertest = getSyncWrapper();

        assertThat(undertest).isNotNull();
        assertThat(undertest.getWettkampfId()).isEqualTo(wettkampfId);
        assertThat(undertest.getOfflineToken()).isEqualTo(offlineToken);
        assertThat(undertest.getMannschaftsmitglied()).hasSize(1);
        assertThat(undertest.getMatch()).hasSize(1);
    }

    @Test
    public void assertToString() {
        final SyncWrapper underTest = getSyncWrapper();
        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(wettkampfId))
                .contains(offlineToken);
    }
}