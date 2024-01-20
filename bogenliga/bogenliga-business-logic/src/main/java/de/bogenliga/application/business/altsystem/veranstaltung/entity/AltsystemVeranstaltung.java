package de.bogenliga.application.business.altsystem.veranstaltung.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.veranstaltung.dataobject.AltsystemVeranstaltungDO;
import de.bogenliga.application.business.altsystem.veranstaltung.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
/**
 * Component to handle the import of a "Veranstaltung" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

@Component
public class AltsystemVeranstaltung implements AltsystemEntity<AltsystemVeranstaltungDO> {

    private final VeranstaltungComponent veranstaltungComponent;
    private final AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;

    @Autowired
    public AltsystemVeranstaltung(final AltsystemVeranstaltungMapper altsystemVeranstaltungMapper, final VeranstaltungComponent veranstaltungComponent) {
        this.altsystemVeranstaltungMapper = altsystemVeranstaltungMapper;
        this.veranstaltungComponent = veranstaltungComponent;
    }

    @Override
    public void create(AltsystemVeranstaltungDO altsystemDataObject, long userId) {

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO = altsystemVeranstaltungMapper.toDO(veranstaltungDO, altsystemDataObject);
        veranstaltungDO = altsystemVeranstaltungMapper.addDefaultFields(veranstaltungDO);

        //Add data to table
        // veranstaltungComponent.create(veranstaltungDO, <UserID>);
        // Add to translation table
    }

    @Override
    public void update(AltsystemVeranstaltungDO altsystemDataObject, long userId) {
        // Get primary key from translation table

        // find data in table with id
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(1);

        // Map data to new object, don't add default fields
        altsystemVeranstaltungMapper.toDO(veranstaltungDO, altsystemDataObject);

        // Update data in table with given primary key
        // veranstaltungComponent.update(veranstaltungDO, <UserID>);
    }
}

