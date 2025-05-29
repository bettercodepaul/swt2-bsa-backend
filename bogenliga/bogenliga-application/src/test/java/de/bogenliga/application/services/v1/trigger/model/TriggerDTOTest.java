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
	private static final Long ALTSYSTEMID = 5L;
	private static final TriggerChangeOperation OPERATION = null;
	private static final TriggerChangeStatus STATUS = null;
	private static final String NACHRICHT = "horrible"; //I did choose this
	private static final OffsetDateTime CREATEDATUTC = OffsetDateTime.MIN;
	private static final OffsetDateTime RUNATUTC = OffsetDateTime.MIN;
	private static final OffsetDateTime LASTMODIFIEDATUTC = OffsetDateTime.MIN;



	//Test data for setters
	private static final Long NEWID = 8L;
	private static final String NEWKATEGORIE = "Pizza Margherita";
	private static final Long NEWALTSYSTEMID = 10L;
	private static final TriggerChangeOperation NEWOPERATION = null;
	private static final TriggerChangeStatus NEWSTATUS = null;
	private static final String NEWNACHRICHT = "perfect";
	private static final OffsetDateTime NEWCREATEDATUTC = OffsetDateTime.MAX;
	private static final OffsetDateTime NEWRUNATUTC = OffsetDateTime.MAX;
	private static final OffsetDateTime NEWLASTMODIFIEDATUTC = OffsetDateTime.MAX;



	private TriggerDTO getExpectedDTO(){
		return new TriggerDTO(ID, KATEGORIE, ALTSYSTEMID, OPERATION, STATUS, NACHRICHT, CREATEDATUTC, RUNATUTC, LASTMODIFIEDATUTC);
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
		actual.setId(NEWID);
		Long actualId = actual.getId();

		assertEquals(NEWID, actualId);
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
		actual.setKategorie(NEWKATEGORIE);
		String actualKategorie = actual.getKategorie();

		assertEquals(NEWKATEGORIE, actualKategorie);
	}

	@Test
	public void testGetAltsystemId(){
		TriggerDTO actual = getExpectedDTO();
		Long actualAltsystemId = actual.getAltsystemId();

		assertEquals(ALTSYSTEMID, actualAltsystemId);
	}
	@Test
	public void testSetAltsystemId(){
		TriggerDTO actual = getExpectedDTO();
		actual.setAltsystemId(NEWALTSYSTEMID);
		Long actualAltsystemId = actual.getAltsystemId();

		assertEquals(NEWALTSYSTEMID, actualAltsystemId);
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
		actual.setOperation(NEWOPERATION);
		TriggerChangeOperation actualOperation = actual.getOperation();

		assertEquals(NEWOPERATION, actualOperation);
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
		actual.setStatus(NEWSTATUS);
		TriggerChangeStatus actualStatus = actual.getStatus();

		assertEquals(NEWSTATUS, actualStatus);
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
		actual.setNachricht(NEWNACHRICHT);
		String actualNachricht = actual.getNachricht();

		assertEquals(NEWNACHRICHT, actualNachricht);
	}

	@Test
	public void testGetCreatedAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		OffsetDateTime actualCreatedAtUTC = actual.getCreatedAtUtc();

		assertEquals(CREATEDATUTC, actualCreatedAtUTC);
	}
	@Test
	public void testSetCreatedAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		actual.setCreatedAtUtc(NEWCREATEDATUTC);
		OffsetDateTime actualCreatedAtUtc = actual.getCreatedAtUtc();

		assertEquals(NEWCREATEDATUTC, actualCreatedAtUtc);
	}

	@Test
	public void testGetRunAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		OffsetDateTime actualRunAtUTC = actual.getRunAtUtc();

		assertEquals(RUNATUTC, actualRunAtUTC);
	}
	@Test
	public void testSetRunAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		actual.setRunAtUtc(NEWRUNATUTC);
		OffsetDateTime actualRunAtUtc = actual.getRunAtUtc();

		assertEquals(NEWRUNATUTC, actualRunAtUtc);
	}

	@Test
	public void testGetLastModiefiedAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		OffsetDateTime actualLastModifiedAtUTC = actual.getlastModifiedAtUtc();

		assertEquals(LASTMODIFIEDATUTC, actualLastModifiedAtUTC);
	}
	@Test
	public void testSetLastModifiedAtUTC(){
		TriggerDTO actual = getExpectedDTO();
		actual.setLastModifiedAtUtc(NEWRUNATUTC);
		OffsetDateTime actualLastModifiedAtUtc = actual.getlastModifiedAtUtc();

		assertEquals(NEWLASTMODIFIEDATUTC, actualLastModifiedAtUtc);
	}
}
