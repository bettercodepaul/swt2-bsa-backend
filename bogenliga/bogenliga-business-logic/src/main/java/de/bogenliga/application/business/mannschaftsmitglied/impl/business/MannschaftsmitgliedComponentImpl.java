package de.bogenliga.application.business.mannschaftsmitglied.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.dsbmitglied.impl.mapper.DsbMitgliedMapper;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.dao.MannschaftsmitgliedDAO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.mapper.MannschaftsmitgliedMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.stereotype.Component;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class MannschaftsmitgliedComponentImpl implements MannschaftsmitgliedComponent {


    private static final String PRECONDITION_MANNSCHAFTSMITGLIED = "MannschaftsmitgliedDO must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID = "MannschaftsmitgliedDO_Mannschaft_ID must not be null";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID = "MannschaftsmitgliedDO_Mitglied_ID must not be null";

    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV = "MannschaftsmitgliedDO_Mannschaft_ID must not be negativ";
    private static final String PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV = "MannschaftsmitgliedDO_Mitglied_ID must not be negativ";


    private final MannschaftsmitgliedDAO mannschaftsmitgliedDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param mannschaftsmitgliedDAO to access the database and return dsbmitglied representations
     */


    @Autowired
    public MannschaftsmitgliedComponentImpl(final MannschaftsmitgliedDAO mannschaftsmitgliedDAO) { this.mannschaftsmitgliedDAO = mannschaftsmitgliedDAO; }


    @Override
    public List<MannschaftsmitgliedDO> findAll() {
        final List<MannschaftsmitgliedBE> mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findAll();
        return mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).collect(Collectors.toList());

    }




    @Override
    public List<MannschaftsmitgliedDO> findAllSchuetzeInTeam(long mannschaftsId) {
        final List<MannschaftsmitgliedBE>  mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findAllSchuetzeInTeam(mannschaftsId);
        return  mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).collect(Collectors.toList());
    }



    @Override
    public List<MannschaftsmitgliedDO> findByTeamId(long mannschaftsId){
        final List<MannschaftsmitgliedBE>  mannschaftsmitgliedBEList = mannschaftsmitgliedDAO.findByTeamId(mannschaftsId);
        return  mannschaftsmitgliedBEList.stream().map(MannschaftsmitgliedMapper.toMannschaftsmitgliedDO).collect(Collectors.toList());
    }


    @Override
    public MannschaftsmitgliedDO findByMemberAndTeamId(long mannschaftId, long mitgliedId) {
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mitgliedId>=0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);

        final MannschaftsmitgliedBE result = mannschaftsmitgliedDAO.findByMemberAndTeamId(mannschaftId,mitgliedId);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for mannschaftId '%s' and mitgliedId '%s", mannschaftId, mitgliedId));
        }

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(result);
    }


    @Override
    public MannschaftsmitgliedDO create(MannschaftsmitgliedDO mannschaftsmitgliedDO,
                               final long currentMemberId) {
        checkMannschaftsmitgliedDO(mannschaftsmitgliedDO, currentMemberId);


        final MannschaftsmitgliedBE mannschaftsmitgliedBE = MannschaftsmitgliedMapper.toMannschaftsmitgliedBE.apply(mannschaftsmitgliedDO);
        final MannschaftsmitgliedBE persistedMannschaftsmitgliedBE = mannschaftsmitgliedDAO.create(mannschaftsmitgliedBE, currentMemberId);

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(persistedMannschaftsmitgliedBE);

    }


    @Override
    public MannschaftsmitgliedDO update(MannschaftsmitgliedDO mannschaftsmitgliedDO,
                                final long currentMemberId) {

        checkMannschaftsmitgliedDO(mannschaftsmitgliedDO, currentMemberId);


        Preconditions.checkArgument(mannschaftsmitgliedDO.getMannschaftId()>=0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getDsbMitgliedId() >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);


        final MannschaftsmitgliedBE mannschaftsmitgliedBE = MannschaftsmitgliedMapper.toMannschaftsmitgliedBE.apply(mannschaftsmitgliedDO);
        final MannschaftsmitgliedBE persistedMannschaftsmitgliederBE = mannschaftsmitgliedDAO.update(mannschaftsmitgliedBE, currentMemberId);

        return MannschaftsmitgliedMapper.toMannschaftsmitgliedDO.apply(persistedMannschaftsmitgliederBE);

    }


    @Override
    public void delete(MannschaftsmitgliedDO mannschaftsmitgliedDO, final long currentMemberId) {

        Preconditions.checkNotNull(mannschaftsmitgliedDO, PRECONDITION_MANNSCHAFTSMITGLIED);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getMannschaftId() >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getDsbMitgliedId() >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID);

        final MannschaftsmitgliedBE mannschaftsmitgliedBE = MannschaftsmitgliedMapper.toMannschaftsmitgliedBE.apply(mannschaftsmitgliedDO);

        mannschaftsmitgliedDAO.delete(mannschaftsmitgliedBE, currentMemberId);

    }

    @Override
    public boolean checkExistingSchuetze(long mannschaftId, final long mitgliedId){
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mitgliedId>=0, PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);

        final MannschaftsmitgliedBE result = mannschaftsmitgliedDAO.findByMemberAndTeamId(mannschaftId,mitgliedId);

        return result.isDsbMitgliedEingesetzt();
    }

    private void checkMannschaftsmitgliedDO(final MannschaftsmitgliedDO mannschaftsmitgliedDO, final long parameter) {
        Preconditions.checkNotNull(mannschaftsmitgliedDO, PRECONDITION_MANNSCHAFTSMITGLIED);
        Preconditions.checkNotNull(mannschaftsmitgliedDO.getMannschaftId(), PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID);
        Preconditions.checkNotNull(mannschaftsmitgliedDO.getDsbMitgliedId(), PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID);

        Preconditions.checkArgument(mannschaftsmitgliedDO.getMannschaftId() >= 0, PRECONDITION_MANNSCHAFTSMITGLIED_MANNSCHAFT_ID_NEGATIV);
        Preconditions.checkArgument(mannschaftsmitgliedDO.getDsbMitgliedId()>=0,PRECONDITION_MANNSCHAFTSMITGLIED_MITGLIED_ID_NEGATIV);
    }
}
