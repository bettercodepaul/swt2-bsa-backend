package de.bogenliga.application.business.mannschaftsmitglied.api;

import java.util.List;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface MannschaftsmitgliedComponent extends ComponentFacade {

    /**
     * Return all mannschaftsmitglied entries.
     *
     * @return list of all mannschaftsmitglied mannschaftsmitglied in the database;
     * empty list, if no mannschaftsmitglied is found
     */
    List<MannschaftsmitgliedDO> findAll();


    /**
     *
     * Die Funktion liefert alle einer Mannschaft zugeordneten Schützen,
     * für die in mindestens einem Wettkampf Pfeilwerte erfasst wurden,
     * d.h. sie haben aktiv geschossen
     */

    List<MannschaftsmitgliedDO> findAllSchuetzeInTeamEingesetzt(Long mannschaftsmitgliederMannschaftsId);


    /**
     *
     * Die Funktion liefert alle einer Mannschaft zugeordneten Schützen,
     * unabhängig ob sie schon aktiv geschossen haben. Z.B. für die Bogenkontrollliste.
     */

    List<MannschaftsmitgliedDO> findAllSchuetzeInTeam(Long mannschaftsmitgliederMannschaftsId);



    /**
     * Return all mannschaftsmitglied entries from one team.
     *
     * @return list of all mannschaftsmitglied mannschaftsmitglied in the database;
     * empty list, if no mannschaftsmitglied is found
     */

    List<MannschaftsmitgliedDO> findByTeamId(Long mannschaftsmitgliederMannschaftsId);



    /**
     * Return a mannschaftsmitglied entry with the given id.
     *
     * @param mannschaftsmitgliedMannschaftId of the mannschaftsmitglied,
     * @param mannschaftsmitgliedMitgliedId of the mannschaftsmitglied
     * @return single mannschaftsmitglied entry with the given id;
     * null, if no mannschaftsmitglied is found
     */
    MannschaftsmitgliedDO findByMemberAndTeamId(Long mannschaftsmitgliedMannschaftId, Long mannschaftsmitgliedMitgliedId);

    /**
     * Returns a mannschaftsmitglied entry with the given rueckennummer.
     * @param mannschaftId of the mannschaftsmitglied
     * @param rueckennummer of the mannschaftsmitglied
     * @return single mannschaftsmitglied entry with the given rueckennummer;
     * null, if no mannschaftsmitglied is found
     */
    MannschaftsmitgliedDO findByTeamIdAndRueckennummer(Long mannschaftId, Long rueckennummer);

    /**
     * return all mannschaftsmitglied entries from one dsbmitglied
     *
     * @param mannschaftsmitgliedMitgliedId of the mannschaftsmitglied
     * @return lkist of mannschaftsmitglied entries with the given mitgliedsId
     */
    List<MannschaftsmitgliedDO> findByMemberId(Long mannschaftsmitgliedMitgliedId);


    /**
     * Create a new mannschaftsmitglied in the database.
     *
     * @param mannschaftsmitgliedDO new mannschaftsmitglied
     * @param currentMemberId
     * @return persisted version of the mannschaftsmitglied
     */
    MannschaftsmitgliedDO create(MannschaftsmitgliedDO mannschaftsmitgliedDO, Long currentMemberId);



    /**
     * Update an existing mannschaftsmitglied. The mannschaftsmitglied is identified by the id.
     *
     * @param mannschaftsmitgliedDO existing mannschaftsmitgliedDO to update
     * @param currentMemberId
     * @return persisted version of the mannschaftsmitglied
     */
    MannschaftsmitgliedDO update(MannschaftsmitgliedDO mannschaftsmitgliedDO, Long currentMemberId);


    /**
     * Delete an existing mannschaftsmitglied. The mannschaftsmitglied is identified by the id.
     *
     * @param mannschaftsmitgliedDO mannschaftsmitglied to delete
     * @param currentMemberId
     */
    void delete(MannschaftsmitgliedDO mannschaftsmitgliedDO, Long currentMemberId);


    /**
     * Deletes an existing mannschaftsmitlgied, identified by the teamId and the dsbMitlgiedId
     * @param mannschaftsmitgliedDO mannschaftsmitglied to delete
     * @param currentUserId
     */
    void deleteByTeamIdAndMemberId(MannschaftsmitgliedDO mannschaftsmitgliedDO, Long currentUserId);

    boolean checkExistingSchuetze(Long teamId, Long memberId);

}
