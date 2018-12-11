package de.bogenliga.application.common.time;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class DateProviderTest {

    private static final ZoneId UTC = ZoneId.of("UTC");

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void currentDateTimeUtc() {
        // prepare test data
        final OffsetDateTime expected = OffsetDateTime.now(ZoneOffset.UTC);

        // configure mocks

        // call test method
        final OffsetDateTime actual = DateProvider.currentDateTimeUtc();

        // assert result
        assertThat(actual.getYear()).isEqualTo(expected.getYear());
        assertThat(actual.getMonth()).isEqualTo(expected.getMonth());
        assertThat(actual.getDayOfMonth()).isEqualTo(expected.getDayOfMonth());
        assertThat(actual.getHour()).isEqualTo(expected.getHour());
        assertThat(actual.getMinute()).isEqualTo(expected.getMinute());
        assertThat(actual.getOffset()).isEqualTo(expected.getOffset());

        // verify invocations
    }


    @Test
    public void currentTimestampUtc() {
        // prepare test data
        final OffsetDateTime expectedDateTime = OffsetDateTime.now(ZoneOffset.UTC);
        final Timestamp expected = Timestamp.valueOf(expectedDateTime.atZoneSameInstant(UTC).toLocalDateTime());

        // configure mocks

        // call test method
        final Timestamp actual = DateProvider.currentTimestampUtc();

        // assert result
        assertThat(actual.getYear()).isEqualTo(expected.getYear());
        assertThat(actual.getMonth()).isEqualTo(expected.getMonth());
        assertThat(actual.getDay()).isEqualTo(expected.getDay());
        assertThat(actual.getHours()).isEqualTo(expected.getHours());
        assertThat(actual.getMinutes()).isEqualTo(expected.getMinutes());
        assertThat(actual.getTimezoneOffset()).isEqualTo(expected.getTimezoneOffset());

        // verify invocations
    }


    @Test
    public void convertOffsetDateTime() {
        // prepare test data
        final OffsetDateTime expectedDateTime = OffsetDateTime.now(ZoneOffset.UTC);
        final Timestamp expected = Timestamp.valueOf(expectedDateTime.atZoneSameInstant(UTC).toLocalDateTime());

        // configure mocks

        // call test method
        final Timestamp actual = DateProvider.convertOffsetDateTime(expectedDateTime);

        // assert result
        assertThat(actual).isEqualTo(expected);

        // verify invocations
    }


    @Test
    public void convertTimestamp() {
        // prepare test data
        final OffsetDateTime expectedDateTime = OffsetDateTime.now(ZoneOffset.UTC);
        final Timestamp expectedTimestamp = Timestamp.valueOf(
                expectedDateTime.atZoneSameInstant(UTC).toLocalDateTime());

        // configure mocks

        // call test method
        final OffsetDateTime actual = DateProvider.convertTimestamp(expectedTimestamp);

        // assert result
        assertThat(actual).isEqualTo(expectedDateTime);

        // verify invocations
    }
}