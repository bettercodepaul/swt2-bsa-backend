package de.bogenliga.application.business.wettkampf.impl.business;

import java.util.List;
import java.util.UUID;

import de.bogenliga.application.business.wettkampf.api.AnzeigenComponent;
import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;
import de.bogenliga.application.business.wettkampf.impl.dao.AnzeigenDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.AnzeigenBE;
import de.bogenliga.application.business.wettkampf.impl.mapper.AnzeigenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

@Component
public class AnzeigenComponentImpl implements AnzeigenComponent {

    private static final String PRECONDITION_MSG_TEMPLATE = "Anzeige: %s must not be null and must not be negative";
    public static final String PRECONDITION_MSG_ANZEIGEN_DO = String.format(PRECONDITION_MSG_TEMPLATE, "DO");
    public static final String PRECONDITION_MSG_CURRENT_ANZEIGEN_ID = String.format(PRECONDITION_MSG_TEMPLATE,
            "anzeigenID");
    public static final String PRECONDITION_MSG_CURRENT_ANZEIGEN_PHYSISCHE_BILDSCHIRM_ID = String.format(PRECONDITION_MSG_TEMPLATE,
            "anzeigenBildschirmID");
    public static final String PRECONDITION_MSG_CURRENT_TABLE_TYP = String.format(PRECONDITION_MSG_TEMPLATE,
            "anzeigenTableTyp");
    public static final String PRECONDITION_MSG_CURRENT_WETTKAMPF_ID = String.format(PRECONDITION_MSG_TEMPLATE,
            "anzeigenWettkampfID");
    public static final String PRECONDITION_MSG_CURRENT_AKTUELLES_MATCH = String.format(PRECONDITION_MSG_TEMPLATE,
            "aktuellesMatch");

    private final AnzeigenDAO anzeigenDAO;

    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param anzeigenDAO to access the database and return match representations
     */
    @Autowired
    public AnzeigenComponentImpl(final AnzeigenDAO anzeigenDAO) {
        this.anzeigenDAO = anzeigenDAO;
    }

    @Override
    public List<AnzeigenDO> findAll() {
        final List<AnzeigenBE> anzeigenBEList = anzeigenDAO.findAll();
        return anzeigenBEList.stream().map(AnzeigenMapper.toAnzeigenDO).toList();
    }


    @Override
    public AnzeigenDO findById(Long id) {
        final AnzeigenBE anzeigenBE = anzeigenDAO.findById(id);

        if (anzeigenBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found for ID '%s'", id));
        }

        return AnzeigenMapper.toAnzeigenDO.apply(anzeigenBE);
    }

    @Override
    public List<AnzeigenDO> findByWettkampfId(Long wettkampfId) {
        final List<AnzeigenBE> anzeigenBEList = anzeigenDAO.findByWettkampfId(wettkampfId);

        if (anzeigenBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found for ID '%s'", wettkampfId));
        }

        return anzeigenBEList.stream().map(AnzeigenMapper.toAnzeigenDO).toList();
    }


    @Override
    public AnzeigenDO findByPhysischeBildschirmId(String physischeBildschirmId) {
        final AnzeigenBE anzeigenBE = anzeigenDAO.findByPhysischeBildschirmId(physischeBildschirmId);

        if (anzeigenBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found for PhysischeBildschirmID '%s'", physischeBildschirmId));
        }

        return AnzeigenMapper.toAnzeigenDO.apply(anzeigenBE);
    }


    @Override
    public AnzeigenDO create(AnzeigenDO anzeigenDO, Long currentUserId) {
        this.checkAnzeigen(anzeigenDO);
        //Füge die wettkampfId direkt beim erstellen des Datenbankeintrags hinzu
        AnzeigenBE anzeigenBE = anzeigenDAO.create(AnzeigenMapper.toAnzeigenBE.apply(anzeigenDO), currentUserId);
        return AnzeigenMapper.toAnzeigenDO.apply(anzeigenBE);
    }


    @Override
    public AnzeigenDO update(AnzeigenDO anzeigenDO, Long currentUserId) {
        this.checkAnzeigenFull(anzeigenDO);

        AnzeigenBE anzeigenBE = anzeigenDAO.update(AnzeigenMapper.toAnzeigenBE.apply(anzeigenDO), currentUserId);
        return AnzeigenMapper.toAnzeigenDO.apply(anzeigenBE);
    }


    private void checkAnzeigenFull(AnzeigenDO anzeigenDO) {
        Preconditions.checkNotNull(anzeigenDO, PRECONDITION_MSG_ANZEIGEN_DO);

        Preconditions.checkNotNull(anzeigenDO.getId(), PRECONDITION_MSG_CURRENT_ANZEIGEN_ID);
        Preconditions.checkArgument(anzeigenDO.getId() >= 0, PRECONDITION_MSG_CURRENT_ANZEIGEN_ID);

        checkAnzeigen(anzeigenDO);
    }

    private void checkAnzeigen(AnzeigenDO anzeigenDO) {
        Preconditions.checkNotNull(anzeigenDO, PRECONDITION_MSG_ANZEIGEN_DO);

        if(anzeigenDO.getPhysischeBildschirmId() != null) {
            Preconditions.checkArgument(!anzeigenDO.getPhysischeBildschirmId().isEmpty(), PRECONDITION_MSG_CURRENT_ANZEIGEN_PHYSISCHE_BILDSCHIRM_ID);
        }

        Preconditions.checkNotNull(anzeigenDO.getTableTyp(), PRECONDITION_MSG_CURRENT_TABLE_TYP);
        Preconditions.checkArgument(!anzeigenDO.getTableTyp().isEmpty(), PRECONDITION_MSG_CURRENT_TABLE_TYP);

        Preconditions.checkArgument(anzeigenDO.getAktuellesMatch() > 0, PRECONDITION_MSG_CURRENT_AKTUELLES_MATCH);

        if(anzeigenDO.getWettkampfId() != null) {
            Preconditions.checkArgument(anzeigenDO.getWettkampfId() >= 0, PRECONDITION_MSG_CURRENT_WETTKAMPF_ID);
        }
    }


    @Override
    public void delete(AnzeigenDO anzeigenDO, Long currentUserId) {
        AnzeigenBE anzeigenBE = AnzeigenMapper.toAnzeigenBE.apply(anzeigenDO);
        anzeigenDAO.delete(anzeigenBE, currentUserId);
    }

    @Override
    public String generatePhysischeBildschirmId() {
        return UUID.randomUUID().toString().substring(0, 4);
    }
}

