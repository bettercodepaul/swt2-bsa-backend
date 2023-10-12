package de.bogenliga.application.business.disziplin.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the "disziplin" business entity (BE).
 * <p>
 * A "disziplin" represents a specific category of the sports event.
 * It shows which types of activities are currently played within the "Bogenliga".
 * For example "Langbogen","Blankbogen" etc.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
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