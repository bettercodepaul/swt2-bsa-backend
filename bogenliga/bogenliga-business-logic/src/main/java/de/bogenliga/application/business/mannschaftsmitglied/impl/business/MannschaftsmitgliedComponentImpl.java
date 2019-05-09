package de.bogenliga.application.business.mannschaftsmitglied.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.mapper.MannschaftsmitgliedMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;


@Component
public class MannschaftsmitgliedComponentImpl implements MannschaftsmitgliedComponent {

    private static final String PRECONDITION_MANNSCHAFTSMITGLIED = "MannschaftsmitgliedDO must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID = "MannschaftsmitgliedDO_Mannschaft_ID must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID = "MannschaftsmitgliedDO_Mitglied_ID must not be null";

    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV = "MannschaftsmitgliedDO_Mannschaft_ID must not be negativ";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV = "MannschaftsmitgliedDO_Mitglied_ID must not be negativ";

    private final MannschaftsmitgliedDAO mannschaftsmitgliedDAO;

    private static final String PRECONDITION_MSG_TEMPLATE_NULL = "Mannschaftsmitglied: %s must not be null";
    private static final String PRECONDITION_MSG_TEMPLATE_NEGATIVE = "Mannschaftsmitglied: %s must not be negative";
    private static final String PRECONDITION_FIELD_MANNSCHAFT_ID = "mannschaftsId";
    private static final String PRECONDITION_FIELD_MITGLIED_ID = "mitgliedId";
    private static final String PRECONDITION_FIELD_USER_ID = "currentUserId";


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
        final List<MannschaftsmitgliedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findAll();
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).collect(
                Collectors.toList());

    }


    @Override
    public List<MannschaftsmitgliedDO> findAllSchuetzeInTeam(Long mannschaftsId) {
        checkPreconditions(mannschaftsId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        final List<MannschaftsmitgliedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findAllSchuetzeInTeam(
                mannschaftsId);
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).collect(
                Collectors.toList());
    }


    @Override
    public List<MannschaftsmitgliedDO> findByTeamId(Long mannschaftsId) {
        checkPreconditions(mannschaftsId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        final List<MannschaftsmitgliedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findByTeamId(
                mannschaftsId);
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).collect(
                Collectors.toList());
    }


    @Override
    public MannschaftsmitgliedDO findByMemberAndTeamId(Long mannschaftId, Long mitgliedId) {
        checkPreconditions(mannschaftId, PRECONDITION_FIELD_MANNSCHAFT_ID);
        checkPreconditions(mitgliedId, PRECONDITION_FIELD_MITGLIED_ID);
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mitgliedId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);

        final MannschaftsmitgliedBE result = mannschaftsmitgliedDAO.findByMemberAndTeamId(mannschaftId, mitgliedId);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for mannschaftId '%s' and mitgliedId '%s", mannschaftId,
                            mitgliedId));
        }

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(result);
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

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(persistedMannschaftsmitgliedBE);
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
        final MannschaftsmitgliedBE persistedMannschaftsmitgliederBE = mannschaftsmitgliedDAO.update(
                mannschaftsmitgliedBE, currentUserId);

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(persistedMannschaftsmitgliederBE);
    }


    @Override
    public void delete(MannschaftsmitgliedDO mannschaftsmitgliedDO, final Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_FIELD_USER_ID);

        Preconditions.checkNotNull(mannschaftsmitgliedDO, PRECONDITION_MANNSCHAFTSMITGLIED);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getMannschaftId() >= 0,
                PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getDsbMitgliedId() >= 0,
                PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID);

        final MannschaftsmitgliedBE mannschaftsmitgliedBE = MannschaftsmitgliedMapper.toMannschaftsmitgliedBE.apply(
                mannschaftsmitgliedDO);

        mannschaftsmitgliedDAO.delete(mannschaftsmitgliedBE, currentUserId);
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
