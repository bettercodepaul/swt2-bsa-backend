package de.bogenliga.application.services.v1.kampfrichter.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

public class KampfrichterStrafpunkteRequestDTO implements DataTransferObject {

    private Long matchId;
    private Long strafPunkteSatz1;
    private Long strafPunkteSatz2;
    private Long strafPunkteSatz3;
    private Long strafPunkteSatz4;
    private Long strafPunkteSatz5;

    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public Long getStrafPunkteSatz1() { return strafPunkteSatz1; }
    public void setStrafPunkteSatz1(Long strafPunkteSatz1) { this.strafPunkteSatz1 = strafPunkteSatz1; }

    public Long getStrafPunkteSatz2() { return strafPunkteSatz2; }
    public void setStrafPunkteSatz2(Long strafPunkteSatz2) { this.strafPunkteSatz2 = strafPunkteSatz2; }

    public Long getStrafPunkteSatz3() { return strafPunkteSatz3; }
    public void setStrafPunkteSatz3(Long strafPunkteSatz3) { this.strafPunkteSatz3 = strafPunkteSatz3; }

    public Long getStrafPunkteSatz4() { return strafPunkteSatz4; }
    public void setStrafPunkteSatz4(Long strafPunkteSatz4) { this.strafPunkteSatz4 = strafPunkteSatz4; }

    public Long getStrafPunkteSatz5() { return strafPunkteSatz5; }
    public void setStrafPunkteSatz5(Long strafPunkteSatz5) { this.strafPunkteSatz5 = strafPunkteSatz5; }
}
