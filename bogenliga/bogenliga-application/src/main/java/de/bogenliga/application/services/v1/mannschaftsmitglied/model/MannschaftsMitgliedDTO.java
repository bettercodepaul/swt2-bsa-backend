package de.bogenliga.application.services.v1.mannschaftsmitglied.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

public class MannschaftsMitgliedDTO implements DataTransferObject {

    private Long mannschaftsId;
    private Long dsbMitgliedId;
    private boolean dsbMitgliedEingesetzt;
    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;


    public MannschaftsMitgliedDTO() {
    }


    public MannschaftsMitgliedDTO(final long mannschaftsId, final long dsbMitgliedId,
                                  final boolean dsbMitgliedEingesetzt,
                                  final String dsbMitgliedVorname, final String getDsbMitgliedNachname) {
        this.mannschaftsId = mannschaftsId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
        this.dsbMitgliedVorname = dsbMitgliedVorname;
        this.dsbMitgliedNachname = getDsbMitgliedNachname;
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

    public String getDsbMitgliedVorname(){
        return dsbMitgliedVorname;
    }

    public void setDsbMitgliedVorname(String dsbMitgliedVorname){
        this.dsbMitgliedVorname=dsbMitgliedVorname;
    }

    public String getDsbMitgliedNachname(){
        return dsbMitgliedNachname;
    }

    public void setDsbMitgliedNachname(String dsbMitgliedNachname){
        this.dsbMitgliedNachname=dsbMitgliedNachname;
    }

}
