package online.bogenliga.application.common.component.types;

import online.bogenliga.application.common.types.TransferObject;

/**
 * Marker interface to transfer component value objects to the consumer.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.oracle.com/technetwork/java/transferobject-139757.html">
 * Core J2EE Patterns - Transfer Object</a>
 * @see <a href="https://stackoverflow.com/questions/17008416/java-transfer-object-what-is-it">
 * Stackoverflow - Java Transfer Object, what is it?</a>
 */
public interface ValueObject extends TransferObject {
}
