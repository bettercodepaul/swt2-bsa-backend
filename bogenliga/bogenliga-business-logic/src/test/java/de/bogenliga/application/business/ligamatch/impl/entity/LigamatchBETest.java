package de.bogenliga.application.business.ligamatch.impl.entity;

import org.junit.Test;
import de.bogenliga.application.business.ligamatch.impl.BaseLigamatchTest;
import org.junit.Assert.*;
import static org.junit.Assert.*;

/**
 *
 * @author Christopher Luzzi
 */

public class LigamatchBETest extends BaseLigamatchTest {

    @Test
    public void toString_Test(){
        LigamatchBE ligamatchBE = getLigamatchBE();

        String s = ligamatchBE.toString();
        assertTrue(s.length() > 0);
        assertTrue(s.contains(ligamatchBE.getBegegnung().toString()));
        assertTrue(s.contains(ligamatchBE.getMatchNr().toString()));
        assertTrue(s.contains(ligamatchBE.getScheibennummer().toString()));
        assertTrue(s.contains(ligamatchBE.getClass().getSimpleName()));
        assertTrue(s.contains(ligamatchBE.getClass().getDeclaredFields()[2].getName()));
    }
}
