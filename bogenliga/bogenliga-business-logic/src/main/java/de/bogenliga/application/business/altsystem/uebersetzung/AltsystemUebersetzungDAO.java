package de.bogenliga.application.business.altsystem.uebersetzung;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * DataAccessObject für die Übersetzungstabelle
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemUebersetzungDAO {

    private static final String ALTSYSTEMUEBERSETZUNG_TABLE = "altsystem_uebersetzung";
    private static final String ALTSYSTEMUEBERSETZUNG_TABLE_ID = "uebersetzung_id";
    private static final String ALTSYSTEMUEBERSETZUNG_TABLE_KATEGORIE = "kategorie";
    private static final String ALTSYSTEMUEBERSETZUNG_TABLE_ALTSYSTEM_ID = "altsystem_id";
    private static final String ALTSYSTEMUEBERSETZUNG_TABLE_BOGENLIGA_ID = "bogenliga_id";
    private static final String ALTSYSTEMUEBERSETZUNG_TABLE_WERT = "wert";

    private static final String ALTSYSTEMUEBERSETZUNG_DO_ID = "uebersetzungId";
    private static final String ALTSYSTEMUEBERSETZUNG_DO_KATEGORIE = "kategorie";
    private static final String ALTSYSTEMUEBERSETZUNG_DO_ALTSYSTEM_ID = "altsystemId";
    private static final String ALTSYSTEMUEBERSETZUNG_DO_BOGENLIGA_ID = "bogenligaId";
    private static final String ALTSYSTEMUEBERSETZUNG_DO_WERT = "wert";

    private static final Logger LOGGER = LoggerFactory.getLogger(AltsystemUebersetzungDAO.class);
    private final BasicDAO basicDAO;
    private final BusinessEntityConfiguration<AltsystemUebersetzungDO> UEBERSETZUNG = new BusinessEntityConfiguration<>(
            AltsystemUebersetzungDO.class,
            ALTSYSTEMUEBERSETZUNG_TABLE,
            getColumnsToFieldsMap(),
            LOGGER
    );

    @Autowired
    public AltsystemUebersetzungDAO(BasicDAO basicDAO) {
        this.basicDAO = basicDAO;
    }


    public static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldMap = new HashMap<>();

        columnsToFieldMap.put(ALTSYSTEMUEBERSETZUNG_TABLE_ID, ALTSYSTEMUEBERSETZUNG_DO_ID);
        columnsToFieldMap.put(ALTSYSTEMUEBERSETZUNG_TABLE_KATEGORIE, ALTSYSTEMUEBERSETZUNG_DO_KATEGORIE);
        columnsToFieldMap.put(ALTSYSTEMUEBERSETZUNG_TABLE_ALTSYSTEM_ID, ALTSYSTEMUEBERSETZUNG_DO_ALTSYSTEM_ID);
        columnsToFieldMap.put(ALTSYSTEMUEBERSETZUNG_TABLE_BOGENLIGA_ID, ALTSYSTEMUEBERSETZUNG_DO_BOGENLIGA_ID);
        columnsToFieldMap.put(ALTSYSTEMUEBERSETZUNG_TABLE_WERT, ALTSYSTEMUEBERSETZUNG_DO_WERT);

        return columnsToFieldMap;
    }

    private final String SELECT_BY_KATEGORIE_AND_ALTSYSTEM_ID = new QueryBuilder()
                .selectAll()
                .from(ALTSYSTEMUEBERSETZUNG_TABLE)
                .whereEquals(ALTSYSTEMUEBERSETZUNG_TABLE_KATEGORIE)
                .andEquals(ALTSYSTEMUEBERSETZUNG_TABLE_ALTSYSTEM_ID)
                .compose()
                .toString();

    private final String SELECT_BY_KATEGORIE_AND_VALUE = new QueryBuilder()
            .selectAll()
            .from(ALTSYSTEMUEBERSETZUNG_TABLE)
            .whereEquals(ALTSYSTEMUEBERSETZUNG_TABLE_KATEGORIE)
            .andEquals(ALTSYSTEMUEBERSETZUNG_TABLE_WERT)
            .compose()
            .toString();

    public void updateOrInsertUebersetzung(AltsystemUebersetzungKategorie kategorie, Long altsystemID, Long bogenligaID, String wert) {
        AltsystemUebersetzungDO uebersetzungDO = null;
        try{
            uebersetzungDO = findByAltsystemID(kategorie, altsystemID);
        }catch(Exception e){
            LOGGER.debug(String.valueOf(e));
        }
        if (uebersetzungDO == null){
            String kategorieLabel = kategorie.label;
            uebersetzungDO = new AltsystemUebersetzungDO();
            uebersetzungDO.setKategorie(kategorieLabel);
            uebersetzungDO.setAltsystemId(altsystemID);
            uebersetzungDO.setBogenligaId(bogenligaID);
            uebersetzungDO.setWert(wert);

            basicDAO.insertEntity(UEBERSETZUNG, uebersetzungDO);
        } else{
            uebersetzungDO.setBogenligaId(bogenligaID);
            uebersetzungDO.setWert(wert);

            basicDAO.updateEntity(UEBERSETZUNG, uebersetzungDO, ALTSYSTEMUEBERSETZUNG_DO_ID);
        }
    }


    public AltsystemUebersetzungDO findByAltsystemID(AltsystemUebersetzungKategorie kategorie, Long altsystemID) {
        String kategorieLabel = kategorie.label;

        return basicDAO.selectSingleEntity(UEBERSETZUNG, SELECT_BY_KATEGORIE_AND_ALTSYSTEM_ID, kategorieLabel, altsystemID);
    }

    public AltsystemUebersetzungDO findByWert(AltsystemUebersetzungKategorie kategorie, String wert) {
        String kategorieLabel = kategorie.label;

        return basicDAO.selectSingleEntity(UEBERSETZUNG, SELECT_BY_KATEGORIE_AND_VALUE, kategorieLabel, wert);

    }

}
