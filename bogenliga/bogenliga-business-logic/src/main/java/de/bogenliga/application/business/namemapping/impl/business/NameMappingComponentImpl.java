package de.bogenliga.application.business.namemapping.impl.business;


import de.bogenliga.application.business.disziplin.impl.dao.DisziplinDAO;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAOext;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBEext;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.liga.impl.dao.LigaDAO;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import de.bogenliga.application.business.namemapping.api.NameMappingComponent;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.wettkampftyp.impl.dao.WettkampfTypDAO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@code NameMappingComponent} is responsible for providing a set of simple methods to retrieve or resolve
 * names and designations (e.g., labels, descriptors) associated with entities or objects within the system.
 *
 * <p>Key Functionality:
 * <ul>
 *   <li>ONLY READING DATA</li>
 *   <li>Accessing Data via DAO-Layer</li>
 *   <li>Retrieve names or labels for given identifiers, objects, or keys.</li>
 *   <li>Perform basic mapping operations between entity identifiers and their corresponding names or labels.</li>
 * </ul>
 *
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>This class is designed as a lightweight utility within the Business Layer.</li>
 *   <li>It should only contain simple, non-business-critical methods for name and designation resolution.</li>
 *   <li>No domain-specific logic or checks (e.g., validation, authorization, or complex computations) must be implemented in this class.</li>
 *   <li>Any business-related processing should reside in higher-level components of the Business Layer.</li>
 * </ul>
 *
 * <p>By adhering to these guidelines, this component remains clean, focused on its core purpose, and easily reusable
 * across different parts of the system, without introducing unnecessary complexity or coupling.
 *
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * NameMappingComponent nameMappingComponent = new NameMappingComponent();
 * String userName = nameMappingComponent.getNameForUserId(123);
 * }</pre>
 *
 * @author Michael Dirksmoeller
 * @version 1.0
 */


@Component
public class NameMappingComponentImpl implements NameMappingComponent {

    private final VereinDAO vereinDAO;
    private final DsbMitgliedDAO dsbMitgliedDAO;
    private final DsbMannschaftDAOext dsbMannschaftDAOext;
    private final DisziplinDAO disziplinDAO;
    private final VeranstaltungDAO veranstaltungDAO;
    private final LigaDAO ligaDAO;
    private final UserDAO userDAO;
    private final WettkampfTypDAO wettkampfTypDAO;

    public NameMappingComponentImpl(VereinDAO vereinDAO,
                                    DsbMitgliedDAO dsbMitgliedDAO,
                                    DsbMannschaftDAOext dsbMannschaftDAOext,
                                    DisziplinDAO disziplinDAO,
                                    VeranstaltungDAO veranstaltungDAO,
                                    LigaDAO ligaDAO,
                                    UserDAO userDAO,
                                    WettkampfTypDAO wettkampfTypDAO) {
        this.veranstaltungDAO = veranstaltungDAO;
        this.vereinDAO = vereinDAO;
        this.dsbMitgliedDAO = dsbMitgliedDAO;
        this.dsbMannschaftDAOext = dsbMannschaftDAOext;
        this.disziplinDAO = disziplinDAO;
        this.ligaDAO = ligaDAO;
        this.userDAO = userDAO;
        this.wettkampfTypDAO = wettkampfTypDAO;
    }


    @Override
    public String getDisziplinNameForDisziplinId(Long disziplinId) {
        return disziplinDAO.findById(disziplinId).getName();
    }


    @Override
    public String getDsbMitgliedFullNameForDsbMitgliedId(Long dsbMitgliedId) {
        DsbMitgliedBE dsbMitgliedBE = dsbMitgliedDAO.findById(dsbMitgliedId);
        return (dsbMitgliedBE.getDsbMitgliedVorname() + " " + dsbMitgliedBE.getDsbMitgliedNachname());
    }

    @Override
    public String getLigaNameForLigaId(Long ligaId) {
        return (ligaDAO.findById(ligaId).getLigaName());
    }

    @Override
    public String getMannschaftsnameForVereinIDandMannschaftNr(Long vereinId, Long mannschaftNr) {
        return (vereinDAO.findById(vereinId).getVereinName() + " " +mannschaftNr.toString());
    }

    @Override
    public Long getSportjahrForVeranstaltungsId(Long veranstaltungsId) {
        return (veranstaltungDAO.findById(veranstaltungsId).getVeranstaltungSportjahr());
    }

    @Override
    public String getEmailForUserId(Long userId) {
        return(userDAO.findById(userId).getUserEmail());
    }
    @Override
    public String getVereinnameForVereinId(Long vereinId) {
        return vereinDAO.findById(vereinId).getVereinName();
    }

    @Override
    public String getVereinnameForDsbMitgliedId(Long dsbMitgliedId) {
        return(vereinDAO.findById(dsbMitgliedDAO.findById(dsbMitgliedId).getDsbMitgliedVereinsId()).getVereinName());
    }

  @Override
    public String getVeranstaltungsNameForDsbMannschaftId(Long dsbMannschaftId) {
        List<DsbMannschaftBEext> dsbMannschaftExtList = dsbMannschaftDAOext.findVeranstaltungAndWettkampfById(dsbMannschaftId);
        if (dsbMannschaftExtList.isEmpty()) {
            return null;
        }
        return dsbMannschaftExtList.get(0).getVeranstaltungName();
    }

    @Override
    public String getVeranstaltungsNameForVeranstaltungsId(Long veranstaltungsId) {
        return (veranstaltungDAO.findById(veranstaltungsId).getVeranstaltungName());
    }

    @Override
    public String getWettkampftypNameForWettkampftypId(Long wettkampftypId) {
        return (wettkampfTypDAO.findById(wettkampftypId).getwettkampftypname());
    }
}
