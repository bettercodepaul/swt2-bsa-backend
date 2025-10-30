package de.bogenliga.application.business.namemapping.api;

import de.bogenliga.application.common.component.ComponentFacade;

/**
 * The {@code NameMappingComponent} is responsible for providing a set of simple methods to retrieve or resolve
 * names and designations (e.g., labels, descriptors) associated with entities or objects within the system.
 *
 * <p>Key Functionality:
 * <ul>
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

public interface NameMappingComponent extends ComponentFacade {

    /**
     * Return the Name of Verein.
     *
     * @return VereinName;
     * empty String when Verein not found.
     */
    String  getVereinnameForVereinId(Long vereinId);
    String  getDsbMitgliedFullNameForDsbMitgliedId(Long dsbMitgliedId);
    String  getVeranstaltungsNameForDsbMannschaftId(Long dsbMannschaftId);





}
