package de.bogenliga.application.business.einstellungen.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import static de.bogenliga.application.business.einstellungen.impl.business.EinstellungenComponentImplTest.getEinstellungenBE;
import static de.bogenliga.application.business.einstellungen.impl.business.EinstellungenComponentImplTest.getEinstellungenDO;
import static org.assertj.core.api.Assertions.assertThat;

public class EinstellungenMapperTest {

    private static final long ID = 1L;
    private static final String KEY = "testKey";
    private static final String VALUE = "testValue";


    @Test
    public void toDO() {
        final EinstellungenBE einstellungenBE =  getEinstellungenBE();

        final EinstellungenDO actual = EinstellungenMapper.toDO.apply(einstellungenBE);

        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getKey()).isEqualTo(KEY);
        assertThat(actual.getValue()).isEqualTo(VALUE);
    }

    @Test
    public void toBE() {
        final EinstellungenDO einstellungenDO = getEinstellungenDO();

        final EinstellungenBE actual = EinstellungenMapper.toBE.apply(einstellungenDO);

        assertThat(actual.getEinstellungenId()).isEqualTo(ID);
        assertThat(actual.getEinstellungenKey()).isEqualTo(KEY);
        assertThat(actual.getEinstellungenValue()).isEqualTo(VALUE);
    }

}
