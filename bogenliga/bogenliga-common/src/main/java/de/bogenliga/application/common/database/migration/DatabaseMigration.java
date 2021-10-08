package de.bogenliga.application.common.database.migration;

import de.bogenliga.application.common.configuration.DatabaseConfiguration;
import de.bogenliga.application.common.database.tx.PostgresqlTransactionManager;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * I am responsible for starting the auto migration of the database.
 */
@Component
public class DatabaseMigration {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseMigration.class);

    @Autowired
    private DatabaseConfiguration databaseConfig;

    @Autowired
    private PostgresqlTransactionManager pgTransactionManager;

    @Autowired
    private Environment environment;

    /**
     * This property will be read  from the .properties-File.
     * It can be set to 'false' if the flyway:clean order should be disabled.
     */
    @Value("${flyway.clean.enabled}")
    private boolean flywayCleanEnabled;

    @Value("${spring.flyway.locations}")
    private String[] locations;


    /**
     * I will be automatically called at spring startup to start the database migration.
     * I am setting up the Logger and call flyway.migrate().
     * I automatically run a flyway:clean when the active profile is not PROD.
     */
    @PostConstruct
    public void startDBMigration(){
        LOG.info("Starting Database Migration: ");
        try {
            //Set Logger for Output while Migation is running.
            LogFactory.setLogCreator(new DBMigrationLogger());

            LOG.info("Setting following locations/SqlScript-Folders: {}", Arrays.toString(locations));
            Flyway flyway = Flyway.configure().dataSource
                    (pgTransactionManager.getDataSource())
                    .locations(locations).load();

            LOG.info("Executing following locations/SqlScript-Folders: {}", Arrays.toString(flyway.getConfiguration().getLocations()));

            if(!Arrays.asList(environment.getActiveProfiles()).contains("PROD") && flywayCleanEnabled) {
                flyway.clean();
            }else{
                LOG.info("Skipping flyway: clean");
            }
            //starts the database-migration
            flyway.migrate();

        }catch(Exception e){
            LOG.error(e.getMessage());
        }
    }
}
