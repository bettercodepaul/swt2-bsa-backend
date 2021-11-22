package de.bogenliga.application.services.v1.ligapasse.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Alexander Riexinger, alexander.riexinger@student.reutlingen-university.de
 */
public class LigapasseDTO implements DataTransferObject {

    private Long wettkampf_id;
    private Long match_id;
    private Long passe_id;
    private int passe_lfdnr;
    private Long passe_manschaft_id;
    private Long dsb_mitglied_id;
    private String dsb_mitglied_name;
    private int mannschaftsmitglied_rueckennummer;
    private int passe_ringzahl_pfeil1;
    private int passe_ringzahl_pfeil2;


    /**Constructor with all necessary parameters
     *
     * @param wettkampf_id
     * @param match_id
     * @param passe_id
     * @param passe_lfdnr
     * @param passe_manschaft_id
     * @param dsb_mitglied_id
     * @param dsb_mitglied_name
     * @param mannschaftsmitglied_rueckennummer
     * @param passe_ringzahl_pfeil1
     * @param passe_ringzahl_pfeil2
     */

    public LigapasseDTO(
            Long wettkampf_id,
            Long match_id,
            Long passe_id,
            int passe_lfdnr,
            Long passe_manschaft_id,
            Long dsb_mitglied_id,
            String dsb_mitglied_name,
            int mannschaftsmitglied_rueckennummer,
            int passe_ringzahl_pfeil1,
            int passe_ringzahl_pfeil2
    ) {
        this.wettkampf_id = wettkampf_id;
        this.match_id = match_id;
        this.passe_id = passe_id;
        this.passe_lfdnr = passe_lfdnr;
        this.passe_manschaft_id = passe_manschaft_id;
        this.dsb_mitglied_id = dsb_mitglied_id;
        this.dsb_mitglied_name = dsb_mitglied_name;
        this.mannschaftsmitglied_rueckennummer = mannschaftsmitglied_rueckennummer;
        this.passe_ringzahl_pfeil1 = passe_ringzahl_pfeil1;
        this.passe_ringzahl_pfeil2 = passe_ringzahl_pfeil2;
    }

    public LigapasseDTO() {

        //empty constructor
    }


    /**
     TODO implement GETTER and SETTER from LigapasseBE
     */


}
