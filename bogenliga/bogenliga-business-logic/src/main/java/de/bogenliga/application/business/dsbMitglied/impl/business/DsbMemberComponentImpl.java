package de.bogenliga.application.business.dsbMember.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.dsbMember.api.DsbMemberComponent;
import de.bogenliga.application.business.dsbMember.api.types.DsbMemberDO;
import de.bogenliga.application.business.dsbMember.impl.dao.DsbMemberDAO;
import de.bogenliga.application.business.dsbMember.impl.entity.DsbMemberBE;
import de.bogenliga.application.business.dsbMember.impl.mapper.DsbMemberMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link DsbMemberComponent}
 */
@Component
public class DsbMemberComponentImpl implements DsbMemberComponent {

    private static final String PRECONDITION_MSG_DSBMITGLIED = "DsbMemberDO must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_ID = "DsbMemberDO ID must not be negative";
    private static final String PRECONDITION_MSG_DSBMITGLIED_FORENAME = "DsbMember forename must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_SURNAME  = "DsbMember surname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_BIRTHDATE  = "DsbMember birthdate must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NATIONALITY  = "DsbMember nationality must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_MEMBERNUMBER  = "DsbMember membernumber must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_CLUBID  = "DsbMember club id must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_CLUBID_NEGATIVE  = "DsbMember club id must not be negative";
    private static final String PRECONDITION_MSG_CURRENT_DSBMITGLIED = "Current dsbMember id must not be negative";

    private final DsbMemberDAO dsbMemberDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param dsbMemberDAO to access the database and return dsbMember representations
     * @param signInBA to sign in dsbMembers
     * @param technicalDsbMemberBA to handle all technical dsbMember operations
     */
    @Autowired
    public DsbMemberComponentImpl(final DsbMemberDAO dsbMemberDAO) {
        this.dsbMemberDAO = dsbMemberDAO;
    }


    @Override
    public List<DsbMemberDO> findAll() {
        final List<DsbMemberBE> dsbMemberBEList = dsbMemberDAO.findAll();
        return dsbMemberBEList.stream().map(DsbMemberMapper.toDsbMemberDO).collect(Collectors.toList());
    }


    @Override
    public DsbMemberDO findById(final int id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);

        final DsbMemberBE result = dsbMemberDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return DsbMemberMapper.toDsbMemberDO.apply(result);
    }


    @Override
    public DsbMemberDO create(final DsbMemberDO dsbMemberDO, long currentDsbMemberId) {
        checkDsbMemberDO(dsbMemberDO, currentDsbMemberId);

        final DsbMemberBE dsbMemberBE = DsbMemberMapper.toDsbMemberBE.apply(dsbMemberDO);
        DsbMemberBE persistedDsbMemberBE = dsbMemberDAO.create(dsbMemberBE, currentDsbMemberId);

        return DsbMemberMapper.toDsbMemberDO.apply(persistedDsbMemberBE);
    }


    @Override
    public DsbMemberDO update(final DsbMemberDO dsbMemberDO, long currentDsbMemberId) {
        checkDsbMemberDO(dsbMemberDO, currentDsbMemberId);

        final DsbMemberBE dsbMemberBE = DsbMemberMapper.toDsbMemberBE.apply(dsbMemberDO);
        DsbMemberBE persistedDsbMemberBE = dsbMemberDAO.update(dsbMemberBE, currentDsbMemberId);

        return DsbMemberMapper.toDsbMemberDO.apply(persistedDsbMemberBE);
    }


    @Override
    public void delete(final DsbMemberDO dsbMemberDO, long currentDsbMemberId) {
        Preconditions.checkNotNull(dsbMemberDO, PRECONDITION_MSG_DSBMITGLIED);
        Preconditions.checkArgument(dsbMemberDO.getId() >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);
        Preconditions.checkArgument(currentDsbMemberId >= 0, PRECONDITION_MSG_CURRENT_DSBMITGLIED);

        final DsbMemberBE dsbMemberBE = DsbMemberMapper.toDsbMemberBE.apply(dsbMemberDO);

        dsbMemberDAO.delete(dsbMemberBE, currentDsbMemberId);

    }


    private void checkDsbMemberDO(final DsbMemberDO dsbMemberDO, long currentDsbMemberId) {
        Preconditions.checkNotNull(dsbMemberDO, PRECONDITION_MSG_DSBMITGLIED);
        Preconditions.checkArgument(dsbMemberDO.getId() >= 0, PRECONDITION_MSG_DSBMITGLIED_ID);
        Preconditions.checkArgument(currentDsbMemberId >= 0, PRECONDITION_MSG_CURRENT_DSBMITGLIED);
        Preconditions.checkNotNull(dsbMemberDO.getForename(), PRECONDITION_MSG_DSBMITGLIED_FORENAME);
        Preconditions.checkNotNull(dsbMemberDO.getSurname(), PRECONDITION_MSG_DSBMITGLIED_SURNAME);
        Preconditions.checkNotNull(dsbMemberDO.getBirthdate(), PRECONDITION_MSG_DSBMITGLIED_BIRTHDATE);
        Preconditions.checkNotNull(dsbMemberDO.getNationality(), PRECONDITION_MSG_DSBMITGLIED_NATIONALITY);
        Preconditions.checkNotNull(dsbMemberDO.getMemberNumber(), PRECONDITION_MSG_DSBMITGLIED_MEMBERNUMBER);
        Preconditions.checkNotNull(dsbMemberDO.getClubId(), PRECONDITION_MSG_DSBMITGLIED_CLUBID);
        Preconditions.checkArgument(dsbMemberDO.getClubId() >= 0, PRECONDITION_MSG_DSBMITGLIED_CLUBID_NEGATIVE);
    }
}
