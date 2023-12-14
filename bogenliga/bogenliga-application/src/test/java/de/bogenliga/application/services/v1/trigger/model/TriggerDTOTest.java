package de.bogenliga.application.services.v1.trigger.model;

import org.junit.Test;
import junit.framework.TestCase;

/**
 * Tests the TriggerDTO class
 *
 */
public class TriggerDTOTest extends TestCase{

	//Test data
	private static final long ID = 24;
	private static final long VERSION = 3;
	private static final String TIMESTAMP = "12.12";
	private static final String DESCRIPTION = "sampleData";
	private static final String STATUS = "bad";


	//Test data for setters
	private static final long newID = 25;
	private static final long newVERSION = 4;
	private static final String newTIMESTAMP = "13.12";
	private static final String newDESCRIPTION = "samplerData";
	private static final String newSTATUS = "good";


	private TriggerDTO getExpectedDTO(){
		return new TriggerDTO(ID, VERSION, TIMESTAMP, DESCRIPTION, STATUS);
	}

	@Test
	public void testGetId(){
		TriggerDTO actual = getExpectedDTO();
		long actualId = actual.getId();

		assertEquals(ID, actualId);
	}

	@Test
	public void testSetId(){
		TriggerDTO actual = getExpectedDTO();
		actual.setId(newID);
		long actualId = actual.getId();

		assertEquals(newID, actualId);
	}


	@Test
	public void testGetVersion(){
		TriggerDTO actual = getExpectedDTO();
		long actualVersion = actual.getVersion();

		assertEquals(VERSION, actualVersion);
	}

	@Test
	public void testSetVersion(){
		TriggerDTO actual = getExpectedDTO();
		actual.setVersion(newVERSION);
		long actualVersion = actual.getVersion();

		assertEquals(newVERSION, actualVersion);
	}


	@Test
	public void testGetTimestamp(){
		TriggerDTO actual = getExpectedDTO();
		String actualTimestamp = actual.getTimestamp();

		assertEquals(TIMESTAMP, actualTimestamp);
	}

	@Test
	public void testSetTimestamp(){
		TriggerDTO actual = getExpectedDTO();
		actual.setTimestamp(newTIMESTAMP);
		String actualTimestamp = actual.getTimestamp();

		assertEquals(newTIMESTAMP, actualTimestamp);
	}


	@Test
	public void testGetDescription(){
		TriggerDTO actual = getExpectedDTO();
		String actualDescription = actual.getDescription();

		assertEquals(DESCRIPTION, actualDescription);
	}

	@Test
	public void testSetDescription(){
		TriggerDTO actual = getExpectedDTO();
		actual.setDescription(newDESCRIPTION);
		String actualDescription = actual.getDescription();

		assertEquals(newDESCRIPTION, actualDescription);
	}


	@Test
	public void testGetStatus(){
		TriggerDTO actual = getExpectedDTO();
		String actualStatus = actual.getStatus();

		assertEquals(STATUS, actualStatus);
	}

	@Test
	public void testSetStatus(){
		TriggerDTO actual = getExpectedDTO();
		actual.setStatus(newSTATUS);
		String actualStatus = actual.getStatus();

		assertEquals(newSTATUS, actualStatus);
	}


}
