package de.bogenliga.application.services.v1.trigger.service;

import java.lang.reflect.Field;
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
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.mockito.Mockito.*;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerServiceTest {

    private LocalDateTime currentTime = LocalDateTime.now();

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


    @Test
    public void validateSyncDataCall() throws NoSuchFieldException, IllegalAccessException {
        Field syncTimestampField = TriggerService.class.getDeclaredField("syncTimestamp"); //Get field 'syncTimestamp'
        syncTimestampField.setAccessible(true); // Enables Access
        syncTimestampField.set(triggerService,currentTime); // Sets syncTimestamp to current time


        // Evil past-midnight coding snippets
        Class[] scheduleFields = TriggerService.class.getDeclaredClasses(); // Put classes of trigger service in array
        for(Class triggerSchedule: scheduleFields){ // Find inner class 'triggerSchedule'
            Field[] amountField = triggerSchedule.getDeclaredFields(); // Get fields of inner class 'triggerSchedule'
            for(Field amount: amountField){ // Find field named amount
                amount.setAccessible(true); // Enables access
                amount.set(triggerTest,MINUTES.addTo(currentTime, 10)); // Sets currentTime += 10 minutes
                verify(triggerTest, times(1)).syncData(); // Checks if syncData() gets executed
            }
        }

    }
}
