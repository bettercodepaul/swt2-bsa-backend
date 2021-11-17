package de.bogenliga.application.business.ligapasse.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigapasseBE extends CommonBusinessEntity implements BusinessEntity {

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


    public LigapasseBE() {
    }
}
