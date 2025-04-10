package de.bogenliga.application.services.v1.Sync.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import de.bogenliga.application.services.v1.Sync.service.SyncServiceTest;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncPasseDTO;
import de.bogenliga.application.services.v1.sync.model.SyncWrapper;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jonas Sigloch
 */
public class SyncWrapperTest {
    private static final List<LigaSyncMannschaftsmitgliedDTO> mitgliedList = new ArrayList<>(Collections.singletonList(
            new LigaSyncMannschaftsmitgliedDTO(1000L, 2L, 1001L, 1202L, 3L)));
    private static final List<LigaSyncMatchDTO> matchList = new ArrayList<>(Collections.singletonList(SyncServiceTest.getLigaSyncMatchDTO()));
    private static final List<LigaSyncPasseDTO> passeList = new ArrayList<>(Collections.singletonList(SyncServiceTest.getLigaSyncPasseDTO()));
    private static final String OFFLINE_TOKEN = "testToken";
    private static final long WETTKAMPF_ID = 1009L;


    private SyncWrapper getSyncWrapper() {
        return new SyncWrapper(matchList, passeList, mitgliedList, OFFLINE_TOKEN, WETTKAMPF_ID);
    }

    @Test
    public void checkWrapper() {
        SyncWrapper undertest = getSyncWrapper();

        assertThat(undertest).isNotNull();
        assertThat(undertest.getWettkampfId()).isEqualTo(WETTKAMPF_ID);
        assertThat(undertest.getOfflineToken()).isEqualTo(OFFLINE_TOKEN);
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
                .contains(Long.toString(WETTKAMPF_ID))
                .contains(OFFLINE_TOKEN);
    }

    @Test
    public void testSyncWrapper(){
        final SyncWrapper syncWrapper = getSyncWrapper();

        syncWrapper.setMatch(matchList);
        Assert.assertEquals(matchList, syncWrapper.getMatch());

        syncWrapper.setPasse(passeList);
        Assert.assertEquals(passeList, syncWrapper.getPasse());

        syncWrapper.setOfflineToken(OFFLINE_TOKEN);
        Assert.assertEquals(OFFLINE_TOKEN, syncWrapper.getOfflineToken());

        syncWrapper.setWettkampfId(WETTKAMPF_ID);
        Assert.assertEquals(WETTKAMPF_ID, syncWrapper.getWettkampfId());

        syncWrapper.setMannschaftsmitglied(mitgliedList);
        Assert.assertEquals(mitgliedList, syncWrapper.getMannschaftsmitglied());
    }

}