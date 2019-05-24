package de.bogenliga.application.business.veranstaltung.impl.business;

import java.sql.Date;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import static org.junit.Assert.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class VeranstaltungComponentImplTest {

    private static Long veranstaltungID = 123L;
    private static Long veranstaltungWettkampftypID = 234L;
    private static String veranstaltungName = "ReutlingenOpen";
    private static Long veranstaltungSportJahr = 2019L;
    private static String str="2019-05-31";
    private static Date date = Date.valueOf(str);
    private static Date veranstaltungMeldeDeadline = date;
    private static Long veranstaltungLigaleiterID = 10L;

    public static VeranstaltungDO veranstaltungDO() {
        return new VeranstaltungDO( veranstaltungID,
                veranstaltungWettkampftypID,
                veranstaltungName,
                veranstaltungSportJahr,
                veranstaltungMeldeDeadline,
                veranstaltungLigaleiterID
        );
    }
}