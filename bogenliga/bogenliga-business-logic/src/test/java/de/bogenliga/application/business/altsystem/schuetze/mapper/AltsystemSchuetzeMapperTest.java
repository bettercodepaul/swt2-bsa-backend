package de.bogenliga.application.business.altsystem.schuetze.mapper;

import java.sql.SQLException;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.schuetze.entity.AltsystemSchuetze;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class AltsystemSchuetzeMapperTest {

    @Mock
    private AltsystemSchuetzeMapper altsystemSchuetzeMapper;

    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;

    @Mock
    private AltsystemUebersetzung altsystemUebersetzung;

    @InjectMocks
    private AltsystemSchuetze altsystemSchuetze;


    @Test
    public void testCreate_Successful() throws SQLException {
        // Mocking
        AltsystemSchuetzeDO altsystemSchuetzeDO = new AltsystemSchuetzeDO();
        when(altsystemUebersetzung.findByValue(any(), any())).thenReturn(null);



        // Verify
        verify(altsystemSchuetzeMapper, times(1)).toDO(any(), any());
    }
}
