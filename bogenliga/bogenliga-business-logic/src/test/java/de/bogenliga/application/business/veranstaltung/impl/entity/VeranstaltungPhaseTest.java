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
    private final String PHASE_STRING = "Geplant";
    private final int PHASE_INTEGER = 1;
    @InjectMocks
    private VeranstaltungPhase underTest = new VeranstaltungPhase();


    public void testGetPhaseAsInt() {
        final int actual = underTest.getPhaseAsInt(PHASE);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual).isEqualTo(PHASE_INTEGER);
    }


    public void testGetPhaseAsString() {
        final String actual = underTest.getPhaseAsString(PHASE_INTEGER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual).isEqualTo(PHASE_STRING);
    }
}