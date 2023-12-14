package de.bogenliga.application.services.v1.trigger.model;

/**
 * The possible states a migration change can be in at any given time.
 */
public enum MigrationChangeState {
    NEW,
    IN_PROGRESS,
    ERROR,
    SUCCESS,
}
