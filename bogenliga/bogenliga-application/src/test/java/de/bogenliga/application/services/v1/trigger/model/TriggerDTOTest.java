package de.bogenliga.application.services.v1.trigger.model;

import java.time.OffsetDateTime;
import org.junit.Test;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import junit.framework.TestCase;

/**
 * Tests the TriggerDTO class
 *
 * @author Lino Cortese, Pineapple problems Pizza & Bakery gmbh
 */
public class TriggerDTOTest extends TestCase{

	//Test data
	private static final Long ID = 4L;
	private static final String KATEGORIE = "Pizza Hawaii"; //I did not choose this
	private static final Long ALTSYSTEM_ID = 5L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "horrible"; //I did choose this
	private static final OffsetDateTime CREATED_AT_UTC = OffsetDateTime.MIN;
	private static final OffsetDateTime RUN_AT_UTC = OffsetDateTime.MIN;
	private static final OffsetDateTime LAST_MODIFIED_AT_UTC = OffsetDateTime.MIN;



	//Test data for setters
	private static final Long newID = 8L;
	private static final String newKATEGORIE = "Pizza Margherita";
	private static final Long newALTSYSTEM_ID = 10L;
	private static final TriggerChangeOperation newOPERATION = null;
	private static final TriggerChangeStatus newSTATUS = null;
	private static final String newNACHRICHT = "perfect";
	private static final OffsetDateTime newCREATED_AT_UTC = OffsetDateTime.MAX;
	private static final OffsetDateTime newRUN_AT_UTC = OffsetDateTime.MAX;
	private static final OffsetDateTime newLAST_MODIFIED_AT_UTC = OffsetDateTime.MAX;



	private TriggerDTO getExpectedDTO(){
		return new TriggerDTO(ID, KATEGORIE, ALTSYSTEM_ID, OPERATION, STATUS, NACHRICHT, CREATED_AT_UTC, RUN_AT_UTC, LAST_MODIFIED_AT_UTC);
	}

	@Test
	public void testGetId(){
		TriggerDTO actual = getExpectedDTO();
		Long actualId = actual.getId();

		assertEquals(ID, actualId);
	}
	@Test
	public void testSetId(){
		TriggerDTO actual = getExpectedDTO();
		actual.setId(newID);
		Long actualId = actual.getId();

		assertEquals(newID, actualId);
	}

	@Test
	public void testGetKategorie(){
		TriggerDTO actual = getExpectedDTO();
		String actualKategorie = actual.getKategorie();

		assertEquals(KATEGORIE, actualKategorie);
	}
	@Test
	public void testSetKategorie(){
		TriggerDTO actual = getExpectedDTO();
		actual.setKategorie(newKATEGORIE);
		String actualKategorie = actual.getKategorie();

		assertEquals(newKATEGORIE, actualKategorie);
	}

	@Test
	public void testGetAltsystemId(){
		TriggerDTO actual = getExpectedDTO();
		Long actualAltsystemId = actual.getAltsystemId();

		assertEquals(ALTSYSTEM_ID, actualAltsystemId);
	}
	@Test
	public void testSetAltsystemId(){
		TriggerDTO actual = getExpectedDTO();
		actual.setAltsystemId(newALTSYSTEM_ID);
		Long actualAltsystemId = actual.getAltsystemId();

		assertEquals(newALTSYSTEM_ID, actualAltsystemId);
	}

	@Test
	public void testGetOperation(){
		TriggerDTO actual = getExpectedDTO();
		TriggerChangeOperation actualOperation = actual.getOperation();

		assertEquals(OPERATION, actualOperation);
	}
	@Test
	public void testSetOperation(){
		TriggerDTO actual = getExpectedDTO();
		actual.setOperation(newOPERATION);
		TriggerChangeOperation actualOperation = actual.getOperation();

		assertEquals(newOPERATION, actualOperation);
	}

	@Test
	public void testGetStatus(){
		TriggerDTO actual = getExpectedDTO();
		TriggerChangeStatus actualStatus = actual.getStatus();

		assertEquals(STATUS, actualStatus);
	}
	@Test
	public void testSetStatus(){
		TriggerDTO actual = getExpectedDTO();
		actual.setStatus(newSTATUS);
		TriggerChangeStatus actualStatus = actual.getStatus();

		assertEquals(newSTATUS, actualStatus);
	}

	@Test
	public void testGetNachricht(){
		TriggerDTO actual = getExpectedDTO();
		String actualNachricht = actual.getNachricht();

		assertEquals(NACHRICHT, actualNachricht);
	}
	@Test
	public void testSetNachricht(){
		TriggerDTO actual = getExpectedDTO();
		actual.setNachricht(newNACHRICHT);
		String actualNachricht = actual.getNachricht();

		assertEquals(newNACHRICHT, actualNachricht);
	}

	@Test
	public void testGetCreatedAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		OffsetDateTime actualCreatedAtUTC = actual.getCreatedAtUtc();

		assertEquals(CREATED_AT_UTC, actualCreatedAtUTC);
	}
	@Test
	public void testSetCreatedAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		actual.setCreatedAtUtc(newCREATED_AT_UTC);
		OffsetDateTime actualCreatedAtUtc = actual.getCreatedAtUtc();

		assertEquals(newCREATED_AT_UTC, actualCreatedAtUtc);
	}

	@Test
	public void testGetRunAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		OffsetDateTime actualRunAtUTC = actual.getRunAtUtc();

		assertEquals(RUN_AT_UTC, actualRunAtUTC);
	}
	@Test
	public void testSetRunAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		actual.setRunAtUtc(newRUN_AT_UTC);
		OffsetDateTime actualRunAtUtc = actual.getRunAtUtc();

		assertEquals(newRUN_AT_UTC, actualRunAtUtc);
	}

	@Test
	public void testGetLastModiefiedAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		OffsetDateTime actualLastModifiedAtUTC = actual.getlastModifiedAtUtc();

		assertEquals(LAST_MODIFIED_AT_UTC, actualLastModifiedAtUTC);
	}
	@Test
	public void testSetLastModifiedAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		actual.setLastModifiedAtUtc(newRUN_AT_UTC);
		OffsetDateTime actualLastModifiedAtUtc = actual.getlastModifiedAtUtc();

		assertEquals(newLAST_MODIFIED_AT_UTC, actualLastModifiedAtUtc);
	}
}