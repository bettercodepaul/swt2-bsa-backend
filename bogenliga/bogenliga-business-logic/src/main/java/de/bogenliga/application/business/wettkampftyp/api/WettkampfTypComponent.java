package de.bogenliga.application.business.wettkampftyp.api;

import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface WettkampfTypComponent {
    WettkampfTypDO findById(Long wettkampfTypId);
}
