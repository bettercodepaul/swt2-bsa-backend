package de.bogenliga.application.business.wettkampf.api;


import java.util.List;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.component.ComponentFacade;
/**
 * Responsible for the wettkampf database requests.
 * @Autor Marvin Holm, Daniel Schott
 */
public interface WettkampfComponent extends ComponentFacade {

    /**
     * Return all wettkampfentries.
     *
     * @return list of all wettkampf in the database;
     * empty list, if no wettkampf is found
     */
    List<WettkampfDO> findAll();


    /**
     * Return a wettkampf entry with the given id.
     *
     * @param id of the wettkampf
     * @return single wettkampf entry with the given id;
     * null, if no wettkampf is found
     */
    WettkampfDO findById(long id);

    /**
     * Return all wettkaempfe entries with the given mannschafts-id.
     *
     * @param id of the mannschaft
     * @return  wettkampf entries with the given mannschafts-id;
     * empty list, if no wettkampf is found
     */
    List<WettkampfDO> findAllWettkaempfeByMannschaftsId(long id);

    /**
     * Create a new wettkampf in the database.
     *
     * @param wettkampfDO new wettkampf
     * @return persisted version of the dsbmitglied
     */
    WettkampfDO create(WettkampfDO wettkampfDO, long currentWettkampfID);


    /**
     * Update an existing wettkampf. The wettkampf is identified by the id.
     *
     * @param wettkampfDO existing wettkampfDO to update
     * @return persisted version of the wettkampfDO
     */
    WettkampfDO update(WettkampfDO wettkampfDO, long currentWettkampfID);


    /**
     * Delete an existing wettkampf. The wettkampf is identified by the id.
     *
     * @param wettkampfDO wettkampf to delete
     */
    void delete(WettkampfDO wettkampfDO, long currentWettkampfID);

}
