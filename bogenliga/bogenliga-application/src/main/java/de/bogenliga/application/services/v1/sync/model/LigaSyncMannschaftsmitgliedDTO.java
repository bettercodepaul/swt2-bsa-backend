package de.bogenliga.application.services.v1.sync.model;

// diese Liste soll alle Mannschaftsmitglieder aller teilnehmern Mannschaften enthalten.
// Daten können über die Funktion GetAllowedMitglieder von WettkampfComponent ermittelt werden - allerdings sollte
// eine Alternative Implementierung gewählt werden, die statt der DsbMitgliedsID die gewünschtne Daten der Schützen übermittelt
// damit die Liste nicht ein zweites mal aus der DB gelesen werden muss

//bei der Rücksnychornisation kann die ID und die DSB-MitlgiedsID leer sein - dann ist zu prüfen
// ob zwichenzeitlich ein neues Mannschaftsmitgliled angelegt wurde - und die
// Zuordnung der Datensätze (Passe) ist über die Rückennummer vorzunehmen.

import de.bogenliga.application.common.service.types.DataTransferObject;

public class LigaSyncMannschaftsmitgliedDTO implements DataTransferObject {
    private Long id; //ID aus mannscjaftsmitglied_id
    private Long version;
    private Long mannschaftId;
    private Long dsbMitgliedId;
    private Long rueckennummer;

    public LigaSyncMannschaftsmitgliedDTO(){

    }

    public LigaSyncMannschaftsmitgliedDTO(Long id, Long version,
                                          Long mannschaftId, Long dsbMitgliedId,
                                          Long rueckennummer) {
        this.id = id;
        this.version = version;
        this.mannschaftId = mannschaftId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.rueckennummer = rueckennummer;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setVersion(Long version) {
        this.version = version;
    }


    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    public void setDsbMitgliedId(Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    public void setRueckennummer(Long rueckennummer) {
        this.rueckennummer = rueckennummer;
    }


    public Long getId() {return id;}

    public Long getVersion() {return version;}

    public Long getMannschaftId() {return mannschaftId;}

    public Long getDsbMitgliedId() {return dsbMitgliedId;}

    public Long getRueckennummer() {return rueckennummer;}
}
