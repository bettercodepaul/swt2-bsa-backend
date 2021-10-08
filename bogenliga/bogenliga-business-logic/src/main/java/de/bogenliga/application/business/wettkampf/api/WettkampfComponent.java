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
     * @param veranstaltungId id of the veranstaltung
     * @return a list with wettkampf entries
     */

    List<WettkampfDO> findAllByVeranstaltungId(long veranstaltungId);


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
     * @param veranstaltungId veranstaltung for which to create a new Day0 Entry
     * @return persisted version of the wettkampf
     */
     WettkampfDO createWT0(long veranstaltungId, long currentUserID);


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


    /**
     * Generates a list of id's of allowed contestants for the given contest
     * @param wettkampfid Id of a contest
     * @return List of Miglied id's allowed to participate
     */
    List<Long> getAllowedMitglieder(long wettkampfid);

    /**
     * Generates a pdf as binary document
     * @param veranstaltungsid ID for the competition
     * @return document
     */
    byte[] getPDFasByteArray(String name, long veranstaltungsid,long manschaftsid,int jahr);

    /**
     * return Wettkampf  (Wettkampftag 0) from given VeranstaltungsID
     * @param veranstaltungsId for the competition
     * @return WettkampfDO
     */
    WettkampfDO findWT0byVeranstaltungsId(long veranstaltungsId);

    byte[] getUebersichtPDFasByteArray(long veranstaltungsid,long wettkampftag);
}
