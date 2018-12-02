package de.bogenliga.application.business.regionen.impl.entity;

import org.junit.Test;

import static de.bogenliga.application.business.regionen.impl.business.RegionenComponentImplTest.getRegionenBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class RegionenBETest {

    private static final long REGION_ID=10;
    private static final String REGION_NAME="Test";
    private static final String REGION_KUERZEL="T";
    private static final String REGION_TYP="";
    private static final long REGION_UEBERGEORDNET=3;

    @Test
    public void assertToString() {
        final RegionenBE underTest = getRegionenBE();
        underTest.setRegionId(REGION_ID);
        underTest.setRegionName(REGION_NAME);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(REGION_ID))
                .contains(REGION_NAME);
    }


    @Test
    public void assertToString_withoutVorname() {
        final RegionenBE underTest = new RegionenBE();
        underTest.setRegionId(REGION_ID);
        underTest.setRegionName(null);

        final String actual = underTest.toString();

        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(REGION_ID))
                .contains("null");
    }
}