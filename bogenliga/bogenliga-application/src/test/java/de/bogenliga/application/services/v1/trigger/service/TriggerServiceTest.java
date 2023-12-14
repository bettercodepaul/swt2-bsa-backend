package de.bogenliga.application.services.v1.trigger.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerServiceTest {

	private LocalDateTime currentTime = LocalDateTime.now();

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	private BasicDAO basicDao;

	@Mock
	private TriggerService triggerService;

	@InjectMocks
	private TriggerService triggerTest;


	// To be changed
	@Test
	public void testSync(){

	}
}
