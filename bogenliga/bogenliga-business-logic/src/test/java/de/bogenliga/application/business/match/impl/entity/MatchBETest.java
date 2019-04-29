package de.bogenliga.application.business.match.impl.entity;

import org.junit.Test;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import static org.junit.Assert.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchBETest extends BaseMatchTest {

    @Test
    public void toString_Test() {
        MatchBE matchBE = getMatchBE();

        String s = matchBE.toString();
        assertTrue(s.length() > 0);
        assertTrue(s.contains(matchBE.getBegegnung().toString()));
        assertTrue(s.contains(matchBE.getNr().toString()));
        assertTrue(s.contains(matchBE.getScheibenNummer().toString()));
        assertTrue(s.contains(matchBE.getClass().getSimpleName()));
        assertTrue(s.contains(matchBE.getClass().getDeclaredFields()[2].getName()));
    }
}