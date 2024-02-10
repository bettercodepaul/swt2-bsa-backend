package de.bogenliga.application.business.altsystem.schuetze.entity;

import java.sql.SQLException;
import org.junit.Test;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemSchuetzeTest {


//    @Test
//    public void testCreate() throws SQLException {
//        // Mock-Objekte für die Abhängigkeiten erstellen
//        AltsystemSchuetzeMapper altsystemSchuetzeMapper = mock(AltsystemSchuetzeMapper.class);
//        DsbMitgliedComponent dsbMitgliedComponent = mock(DsbMitgliedComponent.class);
//        AltsystemUebersetzung altsystemUebersetzung = mock(AltsystemUebersetzung.class);
//        AltsystemSchuetzeDO altsystemSchuetzeDO = mock(AltsystemSchuetzeDO.class);
//
//        // call test method
//        DsbMitgliedDO actual = new DsbMitgliedDO();
//        altsystemSchuetzeMapper.toDO(actual, altsystemSchuetzeDO);
//
//        // Mocks konfigurieren
//        when(altsystemSchuetzeMapper.toDO(any(), any())).thenReturn(actual);
//        when(altsystemSchuetzeMapper.addDefaultFields(actual, CURRENTUSERID)).thenReturn(actual);
//        when(dsbMitgliedComponent.create(actual, CURRENTUSERID)).thenReturn(actual);
//
//        // Testaufruf
//        altsystemSchuetze.create(mock(AltsystemSchuetzeDO.class), CURRENTUSERID);
//
//        // Test dass alle Methoden aufgerufen wurden
//        verify(altsystemSchuetzeMapper).toDO(new DsbMitgliedDO(), altsystemSchuetzeDO);
//        verify(altsystemSchuetzeMapper).addDefaultFields(actual, CURRENTUSERID);
//        verify(dsbMitgliedComponent).create(actual, CURRENTUSERID);
//    }

}
