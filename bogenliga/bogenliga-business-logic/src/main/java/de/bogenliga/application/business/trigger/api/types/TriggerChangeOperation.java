package de.bogenliga.application.business.trigger.api.types;

/**
 * The possible operations a migration change can execute.
 */
public enum TriggerChangeOperation {
    CREATE(1L),
    UPDATE(2L);

    private final Long id;

    TriggerChangeOperation(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public static TriggerChangeOperation parse(Long id) {
        for (TriggerChangeOperation operation : values()) {
            if (operation.id == id) {
                return operation;
            }
        }

        throw new IllegalArgumentException();
    }
}
