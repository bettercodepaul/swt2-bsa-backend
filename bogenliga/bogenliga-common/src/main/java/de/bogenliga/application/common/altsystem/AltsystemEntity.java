package de.bogenliga.application.common.altsystem;

/**
 * Interface which has to be implemented for all relevant entities of Bogenliga.de
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface AltsystemEntity <T extends AltsystemDO>{
    void create(T altsystemDataObject, long userId);

    void update(T altsystemDataObject, long userId);
}
