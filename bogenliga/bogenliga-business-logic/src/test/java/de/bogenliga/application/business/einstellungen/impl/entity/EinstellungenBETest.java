package de.bogenliga.application.business.einstellungen.impl.entity;

import org.junit.Test;
import static de.bogenliga.application.business.einstellungen.impl.business.EinstellungenComponentImplTest.getEinstellungenBE;
import static org.assertj.core.api.Assertions.assertThat;

public class EinstellungenBETest {

    private static final long ID = 1L;
    private static final String KEY = "testkey";
    private static final String VALUE = "testValue";


    @Test
    public void assertToString() {
        final EinstellungenBE underTest = getEinstellungenBE();
        underTest.setEinstellungenId(ID);
        underTest.setEinstellungenKey(KEY);
        underTest.setEinstellungenValue(VALUE);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(KEY)
                .contains(VALUE);
    }

}
