package de.bogenliga.application.business.mannschaftsmitglied.impl.entity;

import org.junit.Test;
import static de.bogenliga.application.business.mannschaftsmitglied.impl.business.MannschaftsmitgliedComponentImplTest.getMannschatfsmitgliedBE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Philip Dengler,
 */
public class MannschaftsmitgliedBETest {

    private static final long  USER =0;
    private static final long MANNSCHHAFT_ID = 1111;
    private static final long DSB_MITGLIED_ID = 22222;
    private static final boolean DSB_MITGLIED_EINGESETZT = true;


    @Test
    public void assertToString() {
        final MannschaftsmitgliedBE underTest = getMannschatfsmitgliedBE();
        underTest.setMannschaftId(MANNSCHHAFT_ID);
        underTest.setDsbMitgliedId(DSB_MITGLIED_ID);
        underTest.setDsbMitgliedEingesetzt(DSB_MITGLIED_EINGESETZT);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(MANNSCHHAFT_ID))
                .contains(Long.toString(DSB_MITGLIED_ID))
                .contains(Boolean.toString(DSB_MITGLIED_EINGESETZT));
    }


    @Test
    public void assertToString_withoutMitgliedEingesetzt(){
        final MannschaftsmitgliedBE underTest = getMannschatfsmitgliedBE();
        underTest.setMannschaftId(MANNSCHHAFT_ID);
        underTest.setDsbMitgliedId(DSB_MITGLIED_ID);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(MANNSCHHAFT_ID))
                .contains(Long.toString(DSB_MITGLIED_ID));
    }


}
