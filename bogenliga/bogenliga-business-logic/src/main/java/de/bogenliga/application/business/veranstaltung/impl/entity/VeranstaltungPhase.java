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
                phaseAsString = "Geplant";
                break;
            case 2:
                phaseAsString = "Laufend";
                break;
            case 3:
                phaseAsString = "Abgeschlossen";
                break;
            default:
                phaseAsString = "Geplant";
                break;
        }
        return phaseAsString;
    }


    public int getPhaseFromStringToInt(String phase) {
        int phaseInt;
        switch (phase) {
            case "Geplant":
                phaseInt = 1;
                break;
            case "Laufend":
                phaseInt = 2;
                break;
            case "Abgeschlossen":
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