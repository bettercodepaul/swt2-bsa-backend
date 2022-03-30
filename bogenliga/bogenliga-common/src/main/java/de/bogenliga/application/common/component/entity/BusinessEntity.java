package de.bogenliga.application.common.component.entity;

import java.io.Serializable;

/**
 * Marker interface for database entity representations.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
public interface BusinessEntity extends Serializable {

    @Override
    String toString();
}
