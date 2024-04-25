package de.bogenliga.application.business.mannschaftsmitglied.impl.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedExtendedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.mapper.MannschaftsmitgliedMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;


@Component
public class MannschaftsmitgliedComponentImpl implements MannschaftsmitgliedComponent {

    private static final String PRECONDITION_MANNSCHAFTSMITGLIED = "MannschaftsmitgliedDO must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID = "MannschaftsmitgliedDO_Mannschaft_ID must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID = "MannschaftsmitgliedDO_Mitglied_ID must not be null";
    private static final String PRECONDITION_FIELD_RUECKENNUMMER = "MannschaftsmitgliedDO_Rueckennummer must not be null";

    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV = "MannschaftsmitgliedDO_Mannschaft_ID must not be negativ";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV = "MannschaftsmitgliedDO_Mitglied_ID must not be negativ";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_ID = "Mannschaftsmitglied Id must not be negative";

    private final MannschaftsmitgliedDAO mannschaftsmitgliedDAO;

    private static final String PRECONDITION_MSG_TEMPLATE_NULL = "Mannschaftsmitglied: %s must not be null";
    private static final String PRECONDITION_MSG_TEMPLATE_NEGATIVE = "Mannschaftsmitglied: %s must not be negative";
    private static final String PRECONDITION_FIELD_MANNSCHAFT_ID = "mannschaftsId";
    private static final String PRECONDITION_FIELD_MITGLIED_ID = "mitgliedId";
    private static final String PRECONDITION_FIELD_USER_ID = "currentUserId";
    private static final String PRECONDITION_FIELD_WETTKAMPF_ID ="wettkampfId";


    public void checkPreconditions(final Long id, String iDIdentifier) {
        Preconditions.checkNotNull(id, String.format(PRECONDITION_MSG_TEMPLATE_NULL, iDIdentifier));
        Preconditions.checkArgument(id >= 0, String.format(PRECONDITION_MSG_TEMPLATE_NEGATIVE, iDIdentifier));
    }


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param mannschaftsmitgliedDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public MannschaftsmitgliedComponentImpl(final MannschaftsmitgliedDAO mannschaftsmitgliedDAO) {
        this.mannschaftsmitgliedDAO = mannschaftsmitgliedDAO;
    }

    @Override
    public List<MannschaftsmitgliedDO> findAll() {
        final List<MannschaftsmitgliedExtendedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findAll();
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).toList();
    }

    @Override
    public List<MannschaftsmitgliedDO> findAllSchuetzeInTeamEingesetzt(Long mannschaftsId) {
        checkPreconditions(mannschaftsId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        final List<MannschaftsmitgliedExtendedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findAllSchuetzeInTeamEingesetzt(
                mannschaftsId);
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).toList();
    }

    @Override
    public List<MannschaftsmitgliedDO> findAllSchuetzeInTeam(Long mannschaftsId) {
        checkPreconditions(mannschaftsId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        final List<MannschaftsmitgliedExtendedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findAllSchuetzeInTeam(
                mannschaftsId);
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).toList();
    }


    public List<MannschaftsmitgliedDO> findSchuetzenInUebergelegenerLiga(Long mannschaftsId, Long wettkampfId) {
        checkPreconditions(mannschaftsId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        checkPreconditions(wettkampfId, PRECONDITION_FIELD_WETTKAMPF_ID);
        final List<MannschaftsmitgliedExtendedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findSchuetzenInUebergelegenerLiga(
                 mannschaftsId, wettkampfId);
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).toList();
    }


    @Override
    public List<MannschaftsmitgliedDO> findByTeamId(Long mannschaftsId) {
        checkPreconditions(mannschaftsId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        final List<MannschaftsmitgliedExtendedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findByTeamId(
                mannschaftsId);
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).toList();
    }

    @Override
    public MannschaftsmitgliedDO findByMemberAndTeamId(Long mannschaftId, Long mitgliedId) {
        checkPreconditions(mannschaftId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        checkPreconditions(mitgliedId, PRECONDITION_FIELD_MITGLIED_ID);
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mitgliedId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);

        final MannschaftsmitgliedExtendedBE result = mannschaftsmitgliedDAO.findByMemberAndTeamId(mannschaftId, mitgliedId);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for mannschaftId '%s' and mitgliedId '%s", mannschaftId,
                            mitgliedId));
        }

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(result);
    }

    @Override
    public MannschaftsmitgliedDO findByTeamIdAndRueckennummer(Long mannschaftId, Long rueckennummer) {
        checkPreconditions(mannschaftId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        checkPreconditions(rueckennummer, PRECONDITION_FIELD_RUECKENNUMMER);
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(rueckennummer >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);

        final MannschaftsmitgliedExtendedBE result =
                mannschaftsmitgliedDAO.findByTeamIdAndRueckennummer(mannschaftId, rueckennummer);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for mannschaftId '%s' and rueckennummer '%s", mannschaftId,
                            rueckennummer));
        }

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(result);
    }

    @Override
    public List<MannschaftsmitgliedDO> findByMemberId(Long mitgliedId) {
        checkPreconditions(mitgliedId, PRECONDITION_FIELD_MITGLIED_ID);
        Preconditions.checkArgument(mitgliedId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);

        final List<MannschaftsmitgliedExtendedBE> result = mannschaftsmitgliedDAO.findByMemberId(mitgliedId);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for mitgliedId '%s", mitgliedId));
        }

        return result.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).toList();
    }

    @Override
    public MannschaftsmitgliedDO create(MannschaftsmitgliedDO mannschaftsmitgliedDO,
                                        final Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_USER_ID);
        checkMannschaftsmitgliedDO(mannschaftsmitgliedDO);

        final MannschaftsmitgliedBE mannschaftsmitgliedBE = MannschaftsmitgliedMapper.toMannschaftsmitgliedBE.apply(
                mannschaftsmitgliedDO);
        final MannschaftsmitgliedBE persistedMannschaftsmitgliedBE = mannschaftsmitgliedDAO.create(
                mannschaftsmitgliedBE, currentUserId);

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(
                mannschaftsmitgliedDAO.findByMemberAndTeamId(persistedMannschaftsmitgliedBE.getMannschaftId(),
                                                                persistedMannschaftsmitgliedBE.getDsbMitgliedId()));
    }

    @Override
    public MannschaftsmitgliedDO update(MannschaftsmitgliedDO mannschaftsmitgliedDO,
                                        final Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_USER_ID);

        checkMannschaftsmitgliedDO(mannschaftsmitgliedDO);

        Preconditions.checkArgument(mannschaftsmitgliedDO.getMannschaftId() >= 0,
                PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getDsbMitgliedId() >= 0,
                PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);

        final MannschaftsmitgliedBE mannschaftsmitgliedBE = MannschaftsmitgliedMapper.toMannschaftsmitgliedBE.apply(
                mannschaftsmitgliedDO);
        final MannschaftsmitgliedBE persistedMannschaftsmitgliedBE = mannschaftsmitgliedDAO.update(
                mannschaftsmitgliedBE, currentUserId);
        final MannschaftsmitgliedExtendedBE resultMannschaftsmitgliedBE =
                mannschaftsmitgliedDAO.findByMemberAndTeamId(persistedMannschaftsmitgliedBE.getMannschaftId(),
                        persistedMannschaftsmitgliedBE.getDsbMitgliedId());

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(resultMannschaftsmitgliedBE);
    }

    @Override
    public void deleteByTeamIdAndMemberId(MannschaftsmitgliedDO mannschaftsmitgliedDO, final Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_USER_ID);

        Preconditions.checkNotNull(mannschaftsmitgliedDO, PRECONDITION_MANNSCHAFTSMITGLIED);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getMannschaftId() >= 0,
                PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getDsbMitgliedId() >= 0,
                PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID);

        MannschaftsmitgliedDO tmp = this.findByMemberAndTeamId(mannschaftsmitgliedDO.getMannschaftId(), mannschaftsmitgliedDO.getDsbMitgliedId());

        final MannschaftsmitgliedBE mannschaftsmitgliedBE = MannschaftsmitgliedMapper.toMannschaftsmitgliedBE.apply(
                tmp);

        mannschaftsmitgliedDAO.delete(mannschaftsmitgliedBE, currentUserId);
    }

    @Override
    public void delete(MannschaftsmitgliedDO mannschaftsmitgliedDO, final Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_USER_ID);
        Preconditions.checkNotNull(mannschaftsmitgliedDO, PRECONDITION_MANNSCHAFTSMITGLIED);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getId() >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_ID);

        final MannschaftsmitgliedBE mannschaftsmitgliedBE = MannschaftsmitgliedMapper.toMannschaftsmitgliedBE.apply(mannschaftsmitgliedDO);

        mannschaftsmitgliedDAO.delete(mannschaftsmitgliedBE,currentUserId);
    }

    @Override
    public boolean checkExistingSchuetze(Long mannschaftId, final Long mitgliedId) {
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mitgliedId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);

        final MannschaftsmitgliedBE result = mannschaftsmitgliedDAO.findByMemberAndTeamId(mannschaftId, mitgliedId);

        return result.getDsbMitgliedEingesetzt() > 0;
    }

    private void checkMannschaftsmitgliedDO(final MannschaftsmitgliedDO mannschaftsmitgliedDO) {
        Preconditions.checkNotNull(mannschaftsmitgliedDO, PRECONDITION_MANNSCHAFTSMITGLIED);
        Preconditions.checkNotNull(mannschaftsmitgliedDO.getMannschaftId(),
                PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID);
        Preconditions.checkNotNull(mannschaftsmitgliedDO.getDsbMitgliedId(),
                PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID);

        Preconditions.checkArgument(mannschaftsmitgliedDO.getMannschaftId() >= 0,
                PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getDsbMitgliedId() >= 0,
                PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);
    }
}
