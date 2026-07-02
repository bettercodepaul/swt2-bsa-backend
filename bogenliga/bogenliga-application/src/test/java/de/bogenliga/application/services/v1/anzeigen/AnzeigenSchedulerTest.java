package de.bogenliga.application.services.v1.anzeigen;

import de.bogenliga.application.business.wettkampf.api.AnzeigenComponent;
import de.bogenliga.application.services.v1.wettkampf.service.AnzeigenScheduler;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

public class AnzeigenSchedulerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private AnzeigenComponent anzeigenComponent;

    @InjectMocks
    private AnzeigenScheduler underTest;

    @Test
    public void testCleanUpAnzeigen() {
        // 1. Ausführung (Sollte in der Regel um 2 Uhr nachts stattfinden)
        underTest.cleanUpAnzeigen();

        // 2. Wurde die korrekte Funktion im anzeigenComponenet aufgerufen?
        verify(anzeigenComponent).deleteAll();
    }
}