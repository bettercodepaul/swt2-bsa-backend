package de.bogenliga.application.business.Passe.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.Passe.api.PasseComponent;
import de.bogenliga.application.business.Passe.api.types.PasseDO;
import de.bogenliga.application.business.Passe.impl.dao.PasseDAO;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;
import de.bogenliga.application.business.Passe.impl.mapper.PasseMapper;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
@Component
public class PasseComponentImpl implements PasseComponent {


    private static final String PRECONDITION_PASSEDO = "PasseDO must not be null";
    private static final String PRECONDITION_PASSE_MANNSCHAFT_ID = "passe_mannschaft_id must not be null";
    private static final String PRECONDITION_DSBMITGLIED_MITGLIED_ID = "PasseDO_dsb_mitglied_id must not be null";

    private static final String PRECONDITION_PASSE_MANNSCHAFT_ID_NEGATIV = "MannschaftsmitgliedDO_Mannschaft_ID must not be negativ";
    private static final String PRECONDITION_PASSE_DSB_MITGLIED_ID_NEGATIV = "PasseDO_Dsb_Mitglied_ID must not be negativ";


    private final PasseDAO passeDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param passeDAO to access the database and return passe representations
     */
    @Autowired
    public PasseComponentImpl(final PasseDAO passeDAO) {
        this.passeDAO = passeDAO;
    }


    /**
     * Return all passe entries.
     *
     * @return list of all passe in the database; empty list, if no passe is found
     */
    @Override
    public List<PasseDO> findAll() {
        final List<PasseBE> passeBEList = passeDAO.findAll();
        return passeBEList.stream().map(PasseMapper.toPasseDO).collect(Collectors.toList());

    }


    /**
     * Return all passe from one Wettkampf
     *
     * @param wettkampfId
     *
     * @return list of all passe from one Wettkampf in the database; empty list, if no passe are found
     */
    @Override
    public List<PasseDO> findByWettkampfId(long wettkampfId) {
        final List<PasseBE> passeBEList = passeDAO.findByWettkampfId(wettkampfId);
        return passeBEList.stream().map(PasseMapper.toPasseDO).collect(Collectors.toList());
    }


    /**
     * Return all passe entries from one team.
     *
     * @param teamId
     *
     * @return list of all passe from one team in the database; empty list, if no passe are found
     */
    @Override
    public List<PasseDO> findByTeamId(long teamId) {
        final List<PasseBE> passeBEList = passeDAO.findByTeamId(teamId);
        return passeBEList.stream().map(PasseMapper.toPasseDO).collect(Collectors.toList());
    }


    /**
     * Return a passe entry with the given ids.
     *
     * @param dsbMitgliedId of the mannschaftsmitglied,
     * @param mannschaftId  of the mannschaft
     *
     * @return list of passe from one mitglied in one team; empty list, if no passe are found
     */
    @Override
    public List<PasseDO> findByMemberAndTeamId(long dsbMitgliedId, long mannschaftId) {
        final List<PasseBE> passeBEList = passeDAO.findByMemberAndTeamId(dsbMitgliedId, mannschaftId);
        return passeBEList.stream().map(PasseMapper.toPasseDO).collect(Collectors.toList());
    }


    /**
     * Return a passe entry with the given id.
     *
     * @param lfdNr counting number,
     *
     * @return passe by counting number; empty list, if no passe are found
     */
    @Override
    public PasseDO findByLfdNr(long lfdNr) {
        final PasseBE passeBE = passeDAO.findByLfdNr(lfdNr);
        return PasseMapper.toPasseDO.apply(passeBE);
    }


    /**
     * Create a new passe in the database.
     *
     * @param passeDO       new passeDO
     * @param currentUserId the current user creating
     *
     * @return persisted version of the passe
     */
    @Override
    public PasseDO create(PasseDO passeDO, final Long currentUserId) {
        final PasseBE passeBE = new PasseBE();
        passeBE.setCreatedByUserId(passeDO.getCreatedByUserId());

        passeBE.setPfeil1(passeDO.getPfeil1());
        passeBE.setPfeil2(passeDO.getPfeil2());
        passeBE.setPfeil3(passeDO.getPfeil3());
        passeBE.setPfeil4(passeDO.getPfeil4());
        passeBE.setPfeil5(passeDO.getPfeil5());
        passeBE.setPfeil6(passeDO.getPfeil6());
        final PasseBE persistedPasseBE = passeDAO.create(passeBE, currentUserId);
        return PasseMapper.toPasseDO.apply(persistedPasseBE);
    }


    /**
     * Update an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeDO         existing passeDO to update
     * @param currentMemberId id of the member currently updating the passe
     *
     * @return persisted version of the passe
     */
    @Override
    public PasseDO update(PasseDO passeDO, long currentMemberId) {
        return null;
    }


    /**
     * Delete an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeDO         passe to delete
     * @param currentMemberId id of the member currently updating the passe
     */
    @Override
    public void delete(PasseDO passeDO, long currentMemberId) {

    }
}
