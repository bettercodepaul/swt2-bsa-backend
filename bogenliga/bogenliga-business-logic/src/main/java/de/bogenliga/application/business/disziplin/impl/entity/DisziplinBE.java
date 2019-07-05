package de.bogenliga.application.business.disziplin.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Marcel Neumann
 * @author Robin Mueller
 */
public class DisziplinBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 1896226667926009349L;

    private Long id;
    private String name;

    @Override
    public String toString() {
        return "MatchBE{" +
                "id=" + id +
                "name=" + name +
                '}';
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}