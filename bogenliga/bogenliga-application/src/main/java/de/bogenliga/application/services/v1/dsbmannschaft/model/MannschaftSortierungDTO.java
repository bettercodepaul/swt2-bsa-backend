package de.bogenliga.application.services.v1.dsbmannschaft.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the dsbMannschaft_Sortierung.
 *
 * I define the payload for the external REST interface of the dsbMannschaft_Sortierung business entity.
 * I'm used to save the Mannschaft-Sortierung from the Ligatabelle or rather from the Veranstaltungs-Detail-Component.
 *
 * @see DataTransferObject
 */
public class MannschaftSortierungDTO implements DataTransferObject {

    private Long id;
    private Long sortierung;

    /**
     * Constructors
     */
    public MannschaftSortierungDTO(){
        // empty constructor
    }

    public MannschaftSortierungDTO(final Long id, final Long sortierung){
        this.id = id;
        this.sortierung = sortierung;
    }

    public Long getId(){ return id; }

    public void setId(Long id){this.id=id;}

    public Long getSortierung(){return sortierung;}

    public void setSortierung(Long sortierung){this.sortierung=sortierung;}

}
