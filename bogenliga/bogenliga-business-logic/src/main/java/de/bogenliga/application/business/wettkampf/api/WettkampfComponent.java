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

    List<WettkampfDO> findByAusrichter(long id);
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
     * retruns all wettkämpfe with the given veranstaltungId
     *
     * @param veranstaltung_id id of the veranstaltung
     * @return a list with wettkampf entries
     */

    List<WettkampfDO> findAllByVeranstaltungId(long veranstaltung_id);


    /**
     * Create a new wettkampf in the database.
     *
     * @param wettkampfDO new wettkampf
     * @return persisted version of the wettkampf
     */
    WettkampfDO create(WettkampfDO wettkampfDO, long currentUserID);


    /**
     * Create a new wettkampf in the database.
     *
     * @param veranstaltung_id veranstaltung for which to create a new Day0 Entry
     * @return persisted version of the wettkampf
     */
     WettkampfDO createWT0(long veranstaltung_id, long currentUserID);


    /**
     * Update an existing wettkampf. The user is identified by the id.
     *
     * @param wettkampfDO existing wettkampfDO to update
     * @return persisted version of the wettkampfDO
     */
    WettkampfDO update(WettkampfDO wettkampfDO, long currentUserID);


    /**
     * Delete an existing wettkampf. The user is identified by the id.
     *
     * @param wettkampfDO wettkampf to delete
     */
    void delete(WettkampfDO wettkampfDO, long currentUserID);

}
