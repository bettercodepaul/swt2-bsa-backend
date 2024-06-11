package de.bogenliga.application.business.trigger.api.types;

import java.time.OffsetDateTime;
import org.junit.Test;
import junit.framework.TestCase;

/**
 * Tests the TriggerDOT class
 *
 * @author Lino Cortese, 99 problems here & now gmbh
 */
public class TriggerDOTest extends TestCase{

	//Test data
	private static final Long ID = 4L;
	private static final String KATEGORIE = "dumb";
	private static final Long ALTSYSTEM_ID = 5L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "beth";
	private static final OffsetDateTime CREATED_AT_UTC = OffsetDateTime.MIN;
	private static final OffsetDateTime RUN_AT_UTC = OffsetDateTime.MIN;
	private static final OffsetDateTime LAST_MODIFIED_AT_UTC = OffsetDateTime.MIN;



	//Test data for setters
	private static final Long newID = 8L;
	private static final String newKATEGORIE = "dumber";
	private static final Long newALTSYSTEM_ID = 10L;
	private static final TriggerChangeOperation newOPERATION = null;
	private static final TriggerChangeStatus newSTATUS = null;
	private static final String newNACHRICHT = "trueger";
	private static final OffsetDateTime newCREATED_AT_UTC = OffsetDateTime.MAX;
	private static final OffsetDateTime newRUN_AT_UTC = OffsetDateTime.MAX;
	private static final OffsetDateTime newLAST_MODIFIED_AT_UTC = OffsetDateTime.MAX;

	private TriggerDO getExpectedDTO(){
		return new TriggerDO(ID, KATEGORIE, ALTSYSTEM_ID, OPERATION, STATUS, NACHRICHT, CREATED_AT_UTC, RUN_AT_UTC,
                LAST_MODIFIED_AT_UTC);
	}

	@Test
	public void testGetId(){
		TriggerDO actual = getExpectedDTO();
		Long actualId = actual.getId();

		assertEquals(ID, actualId);
	}
	@Test
	public void testSetId(){
		TriggerDO actual = getExpectedDTO();
		actual.setId(newID);
		Long actualId = actual.getId();

		assertEquals(newID, actualId);
	}

	@Test
	public void testGetKategorie(){
		TriggerDO actual = getExpectedDTO();
		String actualKategorie = actual.getKategorie();

		assertEquals(KATEGORIE, actualKategorie);
	}
	@Test
	public void testSetKategorie(){
		TriggerDO actual = getExpectedDTO();
		actual.setKategorie(newKATEGORIE);
		String actualKategorie = actual.getKategorie();

		assertEquals(newKATEGORIE, actualKategorie);
	}

	@Test
	public void testGetAltsystemId(){
		TriggerDO actual = getExpectedDTO();
		Long actualAltsystemId = actual.getAltsystemId();

		assertEquals(ALTSYSTEM_ID, actualAltsystemId);
	}
	@Test
	public void testSetAltsystemId(){
		TriggerDO actual = getExpectedDTO();
		actual.setAltsystemId(newALTSYSTEM_ID);
		Long actualAltsystemId = actual.getAltsystemId();

		assertEquals(newALTSYSTEM_ID, actualAltsystemId);
	}

	@Test
	public void testGetOperation(){
		TriggerDO actual = getExpectedDTO();
		TriggerChangeOperation actualOperation = actual.getOperation();

		assertEquals(OPERATION, actualOperation);
	}
	@Test
	public void testSetOperation(){
		TriggerDO actual = getExpectedDTO();
		actual.setOperation(newOPERATION);
		TriggerChangeOperation actualOperation = actual.getOperation();

		assertEquals(newOPERATION, actualOperation);
	}

	@Test
	public void testGetStatus(){
		TriggerDO actual = getExpectedDTO();
		TriggerChangeStatus actualStatus = actual.getStatus();

		assertEquals(STATUS, actualStatus);
	}
	@Test
	public void testSetStatus(){
		TriggerDO actual = getExpectedDTO();
		actual.setStatus(newSTATUS);
		TriggerChangeStatus actualStatus = actual.getStatus();

		assertEquals(newSTATUS, actualStatus);
	}

	@Test
	public void testGetNachricht(){
		TriggerDO actual = getExpectedDTO();
		String actualNachricht = actual.getNachricht();

		assertEquals(NACHRICHT, actualNachricht);
	}
	@Test
	public void testSetNachricht(){
		TriggerDO actual = getExpectedDTO();
		actual.setNachricht(newNACHRICHT);
		String actualNachricht = actual.getNachricht();

		assertEquals(newNACHRICHT, actualNachricht);
	}

	@Test
	public void testGetCreatedAtUTC(){
		TriggerDO actual = getExpectedDTO();
		OffsetDateTime actualCreatedAtUTC = actual.getCreatedAtUtc();

		assertEquals(CREATED_AT_UTC, actualCreatedAtUTC);
	}
	@Test
	public void testSetCreatedAtUTC(){
		TriggerDO actual = getExpectedDTO();
		actual.setCreatedAtUtc(newCREATED_AT_UTC);
		OffsetDateTime actualCreatedAtUtc = actual.getCreatedAtUtc();

		assertEquals(newCREATED_AT_UTC, actualCreatedAtUtc);
	}

	@Test
	public void testGetRunAtUTC(){
		TriggerDO actual = getExpectedDTO();
		OffsetDateTime actualRunAtUTC = actual.getRunAtUtc();

		assertEquals(RUN_AT_UTC, actualRunAtUTC);
	}
	@Test
	public void testSetRunAtUTC(){
		TriggerDO actual = getExpectedDTO();
		actual.setRunAtUtc(newRUN_AT_UTC);
		OffsetDateTime actualRunAtUtc = actual.getRunAtUtc();

		assertEquals(newRUN_AT_UTC, actualRunAtUtc);
	}
	
}
