package de.bogenliga.application.common.time;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;

/**
 * I handle date and time operations.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class DateProvider {

    /**
     * Returns the current time in UTC.
     *
     * @return the current time in UTC.
     */
    public OffsetDateTime currentDateTimeUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

}
