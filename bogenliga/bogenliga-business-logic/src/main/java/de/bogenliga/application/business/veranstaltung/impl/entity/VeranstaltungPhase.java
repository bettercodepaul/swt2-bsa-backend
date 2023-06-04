package de.bogenliga.application.business.veranstaltung.impl.entity;

/**
 * The class stored the Phase ENUM for backend and also convert the enum to integer, when a veranstaltung is called.
 *
 * @author Manuel Lange, student
 */


public class VeranstaltungPhase {

    /**
     * This method converts the phase from Enum to integer. The phase is a enum to specify what phases a veranstaltung
     * can have. In the database the phase is a number as Integer, so that new phases can be added.
     *
     * @param phase
     *
     * @return phaseAsInt
     */

    private final String strPlanned = "Geplant";
    private final String strRunning = "Laufend";
    private final String strCompleted = "Abgeschlossen";
    public int getPhaseAsInt(Phase phase) {
        int phaseAsInt;
        switch (phase) {
            case GEPLANT:
                phaseAsInt = 1;
                break;
            case LAUFEND:
                phaseAsInt = 2;
                break;
            case ABGESCHLOSSEN:
                phaseAsInt = 3;
                break;
            default:
                phaseAsInt = 1;
                break;
        }
        return phaseAsInt;
    }


    /**
     * This method convert the phase from the database from Integer to String, because in the database the phase is a
     * number as integer and in the frontend there should be phase displayed as word for example "Geplant".
     *
     * @param phase
     *
     * @return phaseAsString
     */
    public String getPhaseAsString(int phase) {
        String phaseAsString;
        switch (phase) {
            case 1:
                phaseAsString = strPlanned;
                break;
            case 2:
                phaseAsString = strRunning;
                break;
            case 3:
                phaseAsString = strCompleted;
                break;
            default:
                phaseAsString = strPlanned;
                break;
        }
        return phaseAsString;
    }


    public int getPhaseFromStringToInt(String phase) {
        int phaseInt;
        if (phase == null){
            return 1;
        }
        switch (phase) {
            case strPlanned:
                phaseInt = 1;
                break;
            case strRunning:
                phaseInt = 2;
                break;
            case strCompleted:
                phaseInt = 3;
                break;
            default:
                phaseInt = 1;
                break;
        }
        return phaseInt;
    }


    public enum Phase {
        GEPLANT, LAUFEND, ABGESCHLOSSEN
    }
}
