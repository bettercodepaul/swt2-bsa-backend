package de.bogenliga.application.services.v1.sync.model;

// diese Liste soll alle Mannschaftsmitglieder aller teilnehmern Mannschaften enthalten.
//Daten können über die FUnktion GetAllowedMitglieder von WettkampfComponent ermittelt werden - allerdings sollte
// eine Alternative Implememntierung gewählt werden, die statt der DsbMitgliedsID die gewünschtne Daten der Schützen übermittelt
// damit die Liste nicht ein zweites mal aus der DB gelesen werden muss

public class LigaMannschaftsmitgliederSyncDTO {
    public Long LigaMannschaftsmitgliederDsbMitgliedId;
    public Integer LigaMannschaftsmitgliederRueckennummer;
    public Long LigaMannschaftsmitgliederMannschaftId;

    public LigaMannschaftsmitgliederSyncDTO(){

    }

    public LigaMannschaftsmitgliederSyncDTO(Long ligaMannschaftsmitgliederDsbMitgliedId,
                                            Integer ligaMannschaftsmitgliederRueckennummer,
                                            Long ligaMannschaftsmitgliederMannschaftId) {
        LigaMannschaftsmitgliederDsbMitgliedId = ligaMannschaftsmitgliederDsbMitgliedId;
        LigaMannschaftsmitgliederRueckennummer = ligaMannschaftsmitgliederRueckennummer;
        LigaMannschaftsmitgliederMannschaftId = ligaMannschaftsmitgliederMannschaftId;
    }
}
