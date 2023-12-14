package de.bogenliga.application.business.altsystem.veranstaltung.entity;

import de.bogenliga.application.business.altsystem.veranstaltung.dataobject.AltsystemVeranstaltungDO;
import de.bogenliga.application.business.altsystem.veranstaltung.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemVeranstaltung implements AltsystemEntity<AltsystemVeranstaltungDO> {

    private final VeranstaltungComponent veranstaltungComponent;

    public AltsystemVeranstaltung(final VeranstaltungComponent veranstaltungComponent) { this.veranstaltungComponent = veranstaltungComponent; }

    @Override
    public void create(AltsystemVeranstaltungDO altsystemDataObject) {

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO = AltsystemVeranstaltungMapper.toDO(veranstaltungDO, altsystemDataObject);
        veranstaltungDO = AltsystemVeranstaltungMapper.addDefaultFields(veranstaltungDO);

        //Add data to table

        // Add to translation table
    }

    @Override
    public void update(AltsystemVeranstaltungDO altsystemDataObject) {
        // Get primary key from translation table

        // find data in table with id
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(1);

        // Map data to new object, don't add default fields
        AltsystemVeranstaltungMapper.toDO(veranstaltungDO, altsystemDataObject);

        // Update data in table with given primary key
    }
}

