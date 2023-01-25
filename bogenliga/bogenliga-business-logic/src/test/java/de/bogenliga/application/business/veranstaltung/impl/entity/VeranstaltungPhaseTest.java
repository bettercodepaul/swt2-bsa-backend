package de.bogenliga.application.business.veranstaltung.impl.entity;

import org.mockito.InjectMocks;
import junit.framework.TestCase;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * TODO [AL] class documentation
 *
 * @author Manuel Lange, student
 */
public class VeranstaltungPhaseTest extends TestCase {
    private final VeranstaltungPhase.Phase PHASE = VeranstaltungPhase.Phase.GEPLANT;
    private final String PHASE_STRING_GEPLANT = "Geplant";
    private final String PHASE_STRING_LAUFEND = "Laufend";
    private final String PHASE_STRING_ABGESCHLOSSEN = "Abgeschlossen";
    private final int PHASE_INTEGER = 1;
    private final int PHASE_INTEGER_2 = 2;
    private final int PHASE_INTEGER_3 = 3;
    private final int PHASE_INTEGER_0 = 0;
    @InjectMocks
    private VeranstaltungPhase underTest = new VeranstaltungPhase();


    public void testGetPhaseAsInt() {
        final int actual = underTest.getPhaseAsInt(PHASE);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual).isEqualTo(PHASE_INTEGER);
    }


    /**
     * This is a test-method to test all possibilities in the method getPhaseAsString in the class VeranstaltungPhase.
     * Because the method has a switch(case) every case need to test.
     *
     * @Author: Manuel Lange
     */
    public void testGetPhaseAsString() {

        /**
         * Test 1: Case 1 Phase: Geplant
         */
        final String actual = underTest.getPhaseAsString(PHASE_INTEGER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual).isEqualTo(PHASE_STRING_GEPLANT);

        /**
         * Test 2: Case 2 Phase: Laufend
         */
        final String actual_2 = underTest.getPhaseAsString(PHASE_INTEGER_2);

        // assert result
        assertThat(actual_2).isNotNull();

        assertThat(actual_2).isEqualTo(PHASE_STRING_LAUFEND);

        /**
         * Test 3: Case 3 Phase: Abgeschlossen
         */
        final String actual_3 = underTest.getPhaseAsString(PHASE_INTEGER_3);

        // assert result
        assertThat(actual_3).isNotNull();

        assertThat(actual_3).isEqualTo(PHASE_STRING_ABGESCHLOSSEN);

        /**
         * Test 4: Case Default Phase: Geplant
         */
        final String actual_4 = underTest.getPhaseAsString(PHASE_INTEGER_0);

        // assert result
        assertThat(actual_4).isNotNull();

        assertThat(actual_4).isEqualTo(PHASE_STRING_GEPLANT);
    }
}