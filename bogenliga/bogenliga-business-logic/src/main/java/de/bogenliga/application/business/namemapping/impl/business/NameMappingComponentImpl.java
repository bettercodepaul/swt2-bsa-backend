package de.bogenliga.application.business.namemapping.impl.business;


import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAOext;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBEext;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.namemapping.api.NameMappingComponent;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
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

    public NameMappingComponentImpl(VereinDAO vereinDAO, DsbMitgliedDAO dsbMitgliedDAO, DsbMannschaftDAOext dsbMannschaftDAOext) {
        this.vereinDAO = vereinDAO;
        this.dsbMitgliedDAO = dsbMitgliedDAO;
        this.dsbMannschaftDAOext = dsbMannschaftDAOext;
    }

    @Override
    public String getVereinnameForVereinId(Long vereinId) {
        return vereinDAO.findById(vereinId).getVereinName();
    }

    @Override
    public String getDsbMitgliedFullNameForDsbMitgliedId(Long dsbMitgliedId) {
        DsbMitgliedBE dsbMitgliedBE = dsbMitgliedDAO.findById(dsbMitgliedId);
        return dsbMitgliedBE.getDsbMitgliedVorname() + " " + dsbMitgliedBE.getDsbMitgliedNachname();
    }

    @Override
    public String getVeranstaltungsNameForDsbMannschaftId(Long dsbMannschaftId) {
        // TODO Auto-generated method stub
        List<DsbMannschaftBEext> dsbMannschaftExtList = dsbMannschaftDAOext.findVeranstaltungAndWettkampfById(dsbMannschaftId);
        if (dsbMannschaftExtList.isEmpty()) {
            return null;
        }
        return dsbMannschaftExtList.get(0).getVeranstaltungName();
    }
}
