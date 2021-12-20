package de.bogenliga.application.services.v1.ligamatch.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 *
 *
 * @author Alexander Riexinger, alexander.riexinger@student.reutlingen-university.de
 */
public class LigamatchDTO implements DataTransferObject {

    private Long wettkampf_id;
    private Long match_id;
    private int match_nr;
    private int scheibennummer;
    private Long mannschaft_id;
    private String mannschaft_name;
    private String mannschaft_name_gegner;
    private int scheibennummer_gegner;
    private Long id_gegner;
    private Long naechste_match_id;
    private Long naechste_naechste_match_nr_match_id;
    private int strafpunkte_satz_1;
    private int strafpunkte_satz_2;
    private int strafpunkte_satz_3;
    private int strafpunkte_satz_4;
    private int strafpunkte_satz_5;


    /**Constructor with all necessary parameter
     *
     * @param wettkampf_id
     * @param match_id
     * @param match_nr
     * @param scheibennummer
     * @param mannschaft_id
     * @param mannschaft_name
     * @param mannschaft_name_gegner
     * @param scheibennummer_gegner
     * @param id_gegner
     * @param naechste_match_id
     * @param naechste_naechste_match_nr_match_id
     * @param strafpunkte_satz_1
     * @param strafpunkte_satz_2
     * @param strafpunkte_satz_3
     * @param strafpunkte_satz_4
     * @param strafpunkte_satz_5
     */

    public LigamatchDTO(
            Long wettkampf_id,
            Long match_id,
            int match_nr,
            int scheibennummer,
            Long mannschaft_id,
            String mannschaft_name,
            String mannschaft_name_gegner,
            int scheibennummer_gegner,
            Long id_gegner,
            Long naechste_match_id,
            Long naechste_naechste_match_nr_match_id,
            int strafpunkte_satz_1,
            int strafpunkte_satz_2,
            int strafpunkte_satz_3,
            int strafpunkte_satz_4,
            int strafpunkte_satz_5
    ) {
        this.wettkampf_id = wettkampf_id;
        this.match_id = match_id;
        this.match_nr = match_nr;
        this.scheibennummer = scheibennummer;
        this.mannschaft_id = mannschaft_id;
        this.mannschaft_name = mannschaft_name;
        this.mannschaft_name_gegner = mannschaft_name_gegner;
        this.scheibennummer_gegner = scheibennummer_gegner;
        this.id_gegner = id_gegner;
        this.naechste_match_id = naechste_match_id;
        this.naechste_naechste_match_nr_match_id = naechste_naechste_match_nr_match_id;
        this.strafpunkte_satz_1 = strafpunkte_satz_1;
        this.strafpunkte_satz_2 = strafpunkte_satz_2;
        this.strafpunkte_satz_3 = strafpunkte_satz_3;
        this.strafpunkte_satz_4 = strafpunkte_satz_4;
        this.strafpunkte_satz_5 = strafpunkte_satz_5;
    }

    public LigamatchDTO(){

        //empty constructor
    }


    /**
     TODO implement GETTER and SETTER from LigamatchBE
     */


}
