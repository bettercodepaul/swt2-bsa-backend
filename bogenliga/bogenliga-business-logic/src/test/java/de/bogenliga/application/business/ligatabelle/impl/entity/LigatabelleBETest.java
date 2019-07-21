package de.bogenliga.application.business.ligatabelle.impl.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import static de.bogenliga.application.business.ligatabelle.impl.business.LigatabelleComponentImplTest.getLigatabelleBE;


public class LigatabelleBETest {

    private static final long ID = 1337;
    private static final String VEREINNAME = "Test Verein";

    @Test
    public void assertToString() {
        final LigatabelleBE underTest = getLigatabelleBE();
        underTest.setVeranstaltungId(ID);
        underTest.setVereinName(VEREINNAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(VEREINNAME);
    }


    @Test
    public void assertToString_withoutName() {
        final LigatabelleBE underTest = getLigatabelleBE();
        underTest.setVeranstaltungId(ID);
        underTest.setVereinName(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains("null");
    }

}