package de.bogenliga.application.business.vereine.impl.mapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import de.bogenliga.application.business.datatransfer.mapper.BL_DBOMapper;
import de.bogenliga.application.business.datatransfer.model.VereinDBO;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import org.junit.Test;

import static de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest.getVereinBE;
import static de.bogenliga.application.business.vereine.impl.business.VereinComponentImplTest.getVereinDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class VereinMapperTest {
    private static final long VEREIN_ID= 0;
    private static final String VEREIN_NAME="";

    @Test
    public void toVO() throws Exception {
        final VereinBE VereinBE = getVereinBE();

        final VereinDO actual = VereinMapper.toVereinDO.apply(VereinBE);

        assertThat(actual.getId()).isEqualTo(VEREIN_ID);
        assertThat(actual.getName()).isEqualTo(VEREIN_NAME);

    }


    @Test
    public void toBE() throws Exception {
        final VereinDO VereinDO = getVereinDO();

        final VereinBE actual = VereinMapper.toVereinBE.apply(VereinDO);

        assertThat(actual.getVereinId()).isEqualTo(VEREIN_ID);
        assertThat(actual.getVereinName()).isEqualTo(VEREIN_NAME);
    }


    @Test
    public void testMapVerein() {
        String databaseURL = "jdbc:postgresql://localhost:5432/swt2";
        String query = null;
        ResultSet result = null;
        Connection connection = null;
        BL_DBOMapper dataAccessObj = new BL_DBOMapper();
        VereinDBO mappedVerein = null;

        try {
            mappedVerein = dataAccessObj.mapVerein();
            System.out.println(mappedVerein);
        } catch (SQLException e) {
            fail("SQL Exception aufgetreten: " + e.getMessage());
        }

    }
    
}