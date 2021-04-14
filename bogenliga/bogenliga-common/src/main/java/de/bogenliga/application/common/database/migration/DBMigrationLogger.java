package de.bogenliga.application.common.database.migration;

import org.flywaydb.core.api.logging.Log;
import org.flywaydb.core.api.logging.LogCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBMigrationLogger implements LogCreator {
    private static final Logger LOG = LoggerFactory.getLogger(DBMigrationLogger.class);

    @Override
    public Log createLogger(Class<?> aClass) {
        return new Log() {
            @Override
            public boolean isDebugEnabled() {
                return false;
            }

            @Override
            public void debug(String s) {
                //To get detailed output enable this:
                LOG.debug(s);
            }

            @Override
            public void info(String s) {
                LOG.info(s);
            }

            @Override
            public void warn(String s) {
                LOG.warn(s);
            }

            @Override
            public void error(String s) {
                LOG.error(s);
            }

            @Override
            public void error(String s, Exception e) {
                LOG.error(s,e);
            }
        };
    }
}
