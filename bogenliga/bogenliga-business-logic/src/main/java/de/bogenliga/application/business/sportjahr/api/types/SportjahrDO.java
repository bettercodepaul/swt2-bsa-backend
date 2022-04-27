package de.bogenliga.application.business.sportjahr.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;
/**
 * Erstellt ein SportjahrDO Objekt.
 *
 * @author Philipp Schmidt,
 */
public class SportjahrDO extends CommonDataObject implements DataObject {

    private Long id;
    private Long sportjahr;



    public SportjahrDO(long sportjahrId, long sportjahrJahr){
        //empty
    }

    public SportjahrDO(final Long id, final Long sportjahr, final Long version){
        this.id = id;
        this.sportjahr = sportjahr;
        this.version = version;
    }

    public SportjahrDO() {

    }

    //getter setter
    public Long getId(){ return  this.id; }

    public void setId(Long id){ this.id = id; }

    public Long getSportjahr() {
        return sportjahr;
    }

    public void setSportjahr(Long sportjahr) {
        this.sportjahr = sportjahr;
    }

}

