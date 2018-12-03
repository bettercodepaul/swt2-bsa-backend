package de.bogenliga.application.services.v1.mannschaftsmitglied.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

public class MannschaftsMitgliedDTO implements DataTransferObject {

    private long mannschaftsId;
    private long dsbMitgliedId;
    private boolean dsbMitgliedEingesetzt;

    public MannschaftsMitgliedDTO(){}


    public MannschaftsMitgliedDTO(final long mannschaftsId, final long dsbMitgliedId,
                                  final boolean dsbMitgliedEingesetzt){
        this.mannschaftsId=mannschaftsId;
        this.dsbMitgliedId=dsbMitgliedId;
        this.dsbMitgliedEingesetzt=dsbMitgliedEingesetzt;
    }

    public long getMannschaftsId() {
        return mannschaftsId;
    }

    public void setMannschaftsId(long mannschaftsId) {
        this.mannschaftsId = mannschaftsId;
    }

    public long getDsbMitgliedId() {
        return dsbMitgliedId;
    }

    public void setDsbMitgliedId(long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }

    public boolean isDsbMitgliedEingesetzt() {
        return dsbMitgliedEingesetzt;
    }

    public void setDsbMitgliedEingesetzt(boolean dsbMitgliedEingesetzt) {
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
    }
}
