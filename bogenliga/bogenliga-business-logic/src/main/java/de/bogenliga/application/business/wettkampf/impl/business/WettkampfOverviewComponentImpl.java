package de.bogenliga.application.business.wettkampf.impl.business;

import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.UserProfileComponent;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.mapper.UserMapper;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.wettkampf.api.WettkampfOverviewComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfOverviewDO;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfOverviewDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfOverviewBE;
import de.bogenliga.application.business.wettkampf.impl.mapper.WettkampfMapper;
import de.bogenliga.application.business.wettkampf.impl.mapper.WettkampfOverviewMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserComponent}
 */
@Component
public class WettkampfOverviewComponentImpl implements WettkampfOverviewComponent {

    private final WettkampfOverviewDAO wettkampfOverviewDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     */
    @Autowired
    public WettkampfOverviewComponentImpl(final WettkampfOverviewDAO wettkampfOverviewDAO) {
        this.wettkampfOverviewDAO = wettkampfOverviewDAO;
    }

    @Override
    public List<WettkampfOverviewDO> getOverview() {
        final List<WettkampfOverviewBE> wettkampfOverviewBEList = wettkampfOverviewDAO.findAll();
        return wettkampfOverviewBEList.stream().map(WettkampfOverviewMapper.toWettkampfOverviewDO).collect(Collectors.toList());
    }
}
