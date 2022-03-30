package de.bogenliga.application.services.v1.sync.model;

public class LigaSyncMatchDTO {
    private Long id; //match-Id
    private Long version;
    private Long wettkampfId;
    private Integer matchNr;
    private Integer matchScheibennummer;
    private Long mannschaftId;
    private String mannschaftName;
    private String nameGegner;
    private Integer scheibennummerGegner;
    private Long matchIdGegner;
    private Long naechsteMatchId;
    private Long naechsteNaechsteMatchNrMatchId;
    private Integer strafpunkteSatz1;
    private Integer strafpunkteSatz2;
    private Integer strafpunkteSatz3;
    private Integer strafpunkteSatz4;
    private Integer strafpunkteSatz5;

    public LigaSyncMatchDTO(){

    }

    public LigaSyncMatchDTO(Long id, Long version, Long wettkampfId,
                            Integer matchNr, Integer matchScheibennummer,
                            Long mannschaftId, String mannschaftName,
                            String nameGegner, Integer scheibennummerGegner,
                            Long matchIdGegner, Long naechsteMatchId,
                            Long naechsteNaechsteMatchNrMatchId) {
        this.id = id;
        this.version = version;
        this.wettkampfId = wettkampfId;
        this.matchNr = matchNr;
        this.matchScheibennummer = matchScheibennummer;
        this.mannschaftId = mannschaftId;
        this.mannschaftName = mannschaftName;
        this.nameGegner = nameGegner;
        this.scheibennummerGegner = scheibennummerGegner;
        this.matchIdGegner = matchIdGegner;
        this.naechsteMatchId = naechsteMatchId;
        this.naechsteNaechsteMatchNrMatchId = naechsteNaechsteMatchNrMatchId;
    }

    // Konstruktor mit allen Attributen
    public LigaSyncMatchDTO(Long id, Long version, Long wettkampfId,
                            Integer matchNr, Integer matchScheibennummer,
                            Long mannschaftId, String mannschaftName,
                            String nameGegner, Integer scheibennummerGegner,
                            Long matchIdGegner, Long naechsteMatchId,
                            Long naechsteNaechsteMatchNrMatchId, Integer strafpunkteSatz1,
                            Integer strafpunkteSatz2, Integer strafpunkteSatz3,
                            Integer strafpunkteSatz4, Integer strafpunkteSatz5) {
        new LigaSyncMatchDTO(id,version, wettkampfId, matchNr, matchScheibennummer, mannschaftId, mannschaftName,
                nameGegner, scheibennummerGegner, matchIdGegner, naechsteMatchId, naechsteNaechsteMatchNrMatchId);
        this.strafpunkteSatz1 = strafpunkteSatz1;
        this.strafpunkteSatz2 = strafpunkteSatz2;
        this.strafpunkteSatz3 = strafpunkteSatz3;
        this.strafpunkteSatz4 = strafpunkteSatz4;
        this.strafpunkteSatz5 = strafpunkteSatz5;
    }
}
