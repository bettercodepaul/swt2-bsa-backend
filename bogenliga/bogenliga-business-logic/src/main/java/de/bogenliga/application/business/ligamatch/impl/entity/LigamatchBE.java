package de.bogenliga.application.business.ligamatch.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigamatchBE extends CommonBusinessEntity implements BusinessEntity {


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


    public LigamatchBE() {
        //empty Contructor
    }
}
