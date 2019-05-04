package de.bogenliga.application.business.wettkampftyp.api;


import java.util.List;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampftypDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the wettkampf database requests.
 * @Autor Marvin Holm, Daniel Schott
 */
public interface WettkampftypComponent extends ComponentFacade {

    /**
     * Return all wettkampfentries.
     *
     * @return list of all wettkampf in the database;
     * empty list, if no wettkampf is found
     */
    List<WettkampftypDO> findAll();


    /**
     * Return a wettkampf entry with the given id.
     *
     * @param id of the wettkampf
     * @return single wettkampf entry with the given id;
     * null, if no wettkampf is found
     */
    WettkampftypDO findById(long id);

    /**
     * Create a new wettkampf in the database.
     *
     * @param wettkampftypDO new wettkampf
     * @return persisted version of the dsbmitglied
     */
    WettkampftypDO create(WettkampftypDO wettkampftypDO, long currentWettkampftypID);


    /**
     * Update an existing wettkampf. The wettkampf is identified by the id.
     *
     * @param wettkampftypDO existing wettkampfDO to update
     * @return persisted version of the wettkampfDO
     */
    WettkampftypDO update(WettkampftypDO wettkampftypDO, long currentWettkampftypID);


    /**
     * Delete an existing wettkampf. The wettkampf is identified by the id.
     *
     * @param wettkampftypDO wettkampf to delete
     */
    void delete(WettkampftypDO wettkampftypDO, long currentWettkampftypID);

}
