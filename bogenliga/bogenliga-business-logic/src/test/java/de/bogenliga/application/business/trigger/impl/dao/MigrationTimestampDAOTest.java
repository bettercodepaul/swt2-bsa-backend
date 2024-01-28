package de.bogenliga.application.business.trigger.impl.dao;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.trigger.impl.entity.MigrationTimestampBE;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getMigrationTimestampBE;
import static de.bogenliga.application.business.trigger.impl.business.TriggerComponentImplTest.getTriggerBE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class MigrationTimestampDAOTest {

	public static final MigrationTimestampBE migrationTimestampBE = null;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private BasicDAO basicDAO;
	@InjectMocks
	private MigrationTimestampDAO migrationTimestampDAO;

	@Test
	public void testFindAll() {
		// prepare test data
		final MigrationTimestampBE expectedBE = getMigrationTimestampBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final List<MigrationTimestampBE> actual = migrationTimestampDAO.findAll();

		// assert result
		assertThat(actual)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);

		assertThat(actual.get(0)).isNotNull();
	}

	@Test
	public void testCreate() {
		// prepare test data
		final MigrationTimestampBE expectedBE = getMigrationTimestampBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final MigrationTimestampBE actual = migrationTimestampDAO.create(migrationTimestampBE);

		// assert result
		assertThat(actual)
				.isNull();
	}

	@Test
	public void testUpdate() {
		// prepare test data
		final MigrationTimestampBE expectedBE = getMigrationTimestampBE();

		// configure mocks
		when(basicDAO.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

		// call test method
		final MigrationTimestampBE actual = migrationTimestampDAO.update(migrationTimestampBE);

		// assert result
		assertThat(actual)
				.isNull();
	}

}
