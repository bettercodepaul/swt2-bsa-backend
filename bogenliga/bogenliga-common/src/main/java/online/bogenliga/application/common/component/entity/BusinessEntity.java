package online.bogenliga.application.common.component.entity;

import java.io.Serializable;

/**
 * Marker interface for database entity representations.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface BusinessEntity extends Serializable {

    @Override
    String toString();
}
