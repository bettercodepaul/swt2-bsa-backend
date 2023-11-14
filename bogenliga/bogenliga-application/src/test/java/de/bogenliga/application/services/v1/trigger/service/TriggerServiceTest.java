package de.bogenliga.application.services.v1.trigger.service;

import java.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.services.v1.trigger.service.TriggerService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerServiceTest {

    // Unused as of now \\
    // private LocalDateTime currentTime = LocalDateTime.now();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TriggerService triggerService;

    @InjectMocks
    private TriggerService triggerTest;





    @Test
    public void getPong(){
        // Actual result(s) TBD \\
        assertThat(triggerTest.ping()).isEqualTo("pong");

    }


    /*
    public void validateSyncDataCall(){
        // TODO: Add elapsed time test and verify with below if succeeded
        // verify(triggerService, times(1)).syncData();
    }*/
}
