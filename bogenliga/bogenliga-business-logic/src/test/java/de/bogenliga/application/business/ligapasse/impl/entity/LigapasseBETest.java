package de.bogenliga.application.business.ligapasse.impl.entity;

import org.junit.Test;
import de.bogenliga.application.business.ligapasse.impl.BaseLigapasseTest;
import static org.junit.Assert.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigapasseBETest  extends BaseLigapasseTest {

    @Test
    public void toString_Test(){
        LigapasseBE ligapasseBE = getLigapasseBE();

        String s = ligapasseBE.toString();
        assertTrue(s.length() > 0);
        assertTrue(s.contains(ligapasseBE.getDsbMitgliedName().toString()));
        assertTrue(s.contains(ligapasseBE.getDsbMitgliedId().toString()));
        assertTrue(s.contains(ligapasseBE.getWettkampfId().toString()));
        assertTrue(s.contains(ligapasseBE.getClass().getSimpleName()));
    }
}
