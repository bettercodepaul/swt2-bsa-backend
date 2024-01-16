package de.bogenliga.application.business.trigger.api.types;

/**
 * The possible states a migration change can be in at any given time.
 */
public enum MigrationChangeState {
    NEW,
    IN_PROGRESS,
    ERROR,
    SUCCESS,
}
