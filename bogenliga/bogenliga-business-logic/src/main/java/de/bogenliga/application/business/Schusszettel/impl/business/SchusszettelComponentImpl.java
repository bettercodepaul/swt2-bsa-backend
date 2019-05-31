package de.bogenliga.application.business.Schusszettel.impl.business;

import de.bogenliga.application.business.Schusszettel.api.SchusszettelComponent;

/**
 *
 * @author Michael Hesse, michael_maximilian.hesse@student.reutlingen-university.de
 */
public class SchusszettelComponentImpl implements SchusszettelComponent {
    @Override
    public byte[] getAllSchusszettelPDFasByteArray(long wettkampfid) {
        return new byte[0];
    }
}
