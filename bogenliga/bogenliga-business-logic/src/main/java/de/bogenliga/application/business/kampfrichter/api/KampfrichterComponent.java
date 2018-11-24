package de.bogenliga.application.business.kampfrichter.api;

import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.common.component.ComponentFacade;


/**
 * Responsible for the kampfrichter database requests.
 */
public interface KampfrichterComponent extends ComponentFacade {

    /**
     *
     * @param userId
     * @return true if userId has the liscence to be Kampfrichter
     */
    boolean isKampfrichter(long userId);

/*
    /**
     * Return a kampfrichter entry with the given userId.
     *
     * @param userId of the kampfrichter
     * @return single kampfrichter entry with the given userId;
     * null, if no kampfrichter is found

    KampfrichterDO findById(long userId);*/

    /**
     * Create a new kampfrichter in the database.
     *
     * @param kampfrichterDO new kampfrichter
     * @return persisted version of the kampfrichter
     */
    KampfrichterDO create(KampfrichterDO kampfrichterDO, long currentKampfrichterUserId);


    /**
     * Update an existing kampfrichter. The kampfrichter is identified by the userId.
     *
     * @param kampfrichterDO existing kampfrichterDO to update
     * @return persisted version of the kampfrichter
     */
    KampfrichterDO update(KampfrichterDO kampfrichterDO, long currentKampfrichterUserId);


    /**
     * Delete an existing kampfrichter. The kampfrichter is identified by the userId.
     *
     * @param kampfrichterDO kampfrichter to delete
     */
    void delete(KampfrichterDO kampfrichterDO, long currentKampfrichterUserId);
}