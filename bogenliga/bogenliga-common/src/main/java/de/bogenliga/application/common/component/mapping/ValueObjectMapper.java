package de.bogenliga.application.common.component.mapping;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.types.DataObject;
import de.bogenliga.application.common.mapping.TransferObjectMapper;

/**
 * Interface to map implementations of the {@link DataObject} and {@link BusinessEntity}
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public interface ValueObjectMapper extends TransferObjectMapper {

}
