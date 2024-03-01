package de.bogenliga.application.business.altsystem.sync;

import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * DataAccessObject for the liga entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Bruno Michael Cunha Teixeira, Bruno_Michael.Cunha_teixeira@Student.Reutlingen-University.de
 */
@Repository
public class SyncDAO implements DataAccessObject {

    private final BasicDAO basicDao;
    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public SyncDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    public void executeScript(String query) {

        if(!query.equals("") && query != null){
            this.basicDao.executeQuery(query);
        }
    }


}
