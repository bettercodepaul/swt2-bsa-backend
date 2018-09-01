package online.bogenliga.application.common.component.mapping;

import online.bogenliga.application.common.component.entity.BusinessEntity;
import online.bogenliga.application.common.component.types.ValueObject;
import online.bogenliga.application.common.mapping.TransferObjectMapper;

/**
 * Interface to map implementations of the {@link ValueObject} and {@link BusinessEntity}
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public interface ValueObjectMapper extends TransferObjectMapper {

}
