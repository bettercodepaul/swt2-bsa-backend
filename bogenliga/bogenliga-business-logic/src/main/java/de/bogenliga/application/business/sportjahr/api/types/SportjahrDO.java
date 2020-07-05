package de.bogenliga.application.business.sportjahr;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;
import java.util.List;
/**
 * TODO [AL] class documentation
 *
 * @author Philipp Schmidt,
 */
public class SportjahrDO extends CommonDataObject implements DataObject {

    private Long id;
    private Long sportjahr;
    private Long version;


    public SportjahrDO(){
        //empty
    }

    public SportjahrDO(final Long id, final Long sportjahr, final Long version){
        this.id = id;
        this.sportjahr = sportjahr;
        this.version = version;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

