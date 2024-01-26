package de.bogenliga.application.business.trigger.api.types;

/**
 * The possible states a migration change can be in at any given time.
 */
public enum TriggerChangeStatus {
    NEW(1L),
    IN_PROGRESS(2L),
    ERROR(3L),
    SUCCESS(4L);

    private final Long id;

    TriggerChangeStatus(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public static TriggerChangeStatus parse(Long id) {
        for (TriggerChangeStatus operation : values()) {
            if (operation.id.equals(id)) {
                return operation;
            }
        }

        throw new IllegalArgumentException();
    }
}
