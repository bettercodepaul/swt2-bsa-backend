package de.bogenliga.application.common.time;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * I handle date and time operations.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public final class DateProvider {


    private static final ZoneId UTC = ZoneId.of("UTC");

    private DateProvider() {
        // empty
    }

    /**
     * Returns the current time in UTC.
     *
     * @return the current time in UTC.
     */
    public static OffsetDateTime currentDateTimeUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

    /**
     * Returns the current timestamp in UTC.
     *
     * @return the current timestamp in UTC.
     */
    public static Timestamp currentTimestampUtc() {
        return convertOffsetDateTime(currentDateTimeUtc());
    }


    /**
     * Converts an OffsetDateTime with UTC timezone to a Timestamp
     *
     * @param offsetDateTime to be converted
     * @return Timestamp with UTC timezone
     */
    public static Timestamp convertOffsetDateTime(final OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : Timestamp.valueOf(offsetDateTime.atZoneSameInstant(UTC)
                .toLocalDateTime());
    }


    /**
     * Converts a Timestamp with UTC timezone to an OffsetDateTime
     *
     * @param timestamp to be converted
     * @return OffsetDateTime with UTC timezone
     */
    public static OffsetDateTime convertTimestamp(final Timestamp timestamp) {
        return timestamp == null ? null : OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.UTC);
    }
}
