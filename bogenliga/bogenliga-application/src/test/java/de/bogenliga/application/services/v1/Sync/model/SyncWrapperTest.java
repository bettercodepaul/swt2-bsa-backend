package de.bogenliga.application.services.v1.Sync.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import de.bogenliga.application.services.v1.Sync.service.SyncServiceTest;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;
import de.bogenliga.application.services.v1.sync.model.SyncWrapper;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @author Jonas Sigloch
 */
public class SyncWrapperTest {
    private static final List<LigaSyncMannschaftsmitgliedDTO> mitgliedList = new ArrayList<>(Collections.singletonList(
            new LigaSyncMannschaftsmitgliedDTO(1000L, 2L, 1001L, 1202L, 3L)));
    private static final List<LigaSyncMatchDTO> matchList = new ArrayList<>(Collections.singletonList(SyncServiceTest.getLigaSyncMatchDTO()));
    private static final List<LigaSyncPasseDTO> passeList = new ArrayList<>(Collections.singletonList(SyncServiceTest.getLigaSyncPasseDTO()));
    private static final String offlineToken = "testToken";
    private static final long wettkampfId = 1009L;


    private SyncWrapper getSyncWrapper() {
        return new SyncWrapper(matchList, passeList, mitgliedList, offlineToken, wettkampfId);
    }

    @Test
    public void checkWrapper() {
        SyncWrapper undertest = getSyncWrapper();

        assertThat(undertest).isNotNull();
        assertThat(undertest.getWettkampfId()).isEqualTo(wettkampfId);
        assertThat(undertest.getOfflineToken()).isEqualTo(offlineToken);
        assertThat(undertest.getMannschaftsmitglied()).hasSize(1);
        assertThat(undertest.getMatch()).hasSize(1);
        assertThat(undertest.getPasse()).hasSize(1);
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

    @Test
    public void testSyncWrapper(){
        final SyncWrapper syncWrapper = getSyncWrapper();

        syncWrapper.setMatch(matchList);
        Assert.assertEquals(matchList, syncWrapper.getMatch());

        syncWrapper.setPasse(passeList);
        Assert.assertEquals(passeList, syncWrapper.getPasse());

        syncWrapper.setOfflineToken(offlineToken);
        Assert.assertEquals(offlineToken, syncWrapper.getOfflineToken());

        syncWrapper.setWettkampfId(wettkampfId);
        Assert.assertEquals(wettkampfId, syncWrapper.getWettkampfId());

        syncWrapper.setMannschaftsmitglied(mitgliedList);
        Assert.assertEquals(mitgliedList, syncWrapper.getMannschaftsmitglied());
    }

}