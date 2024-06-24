package de.bogenliga.application.business.mannschaftsmitglied.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedExtendedBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
@Repository
public class MannschaftsmitgliedDAO implements DataAccessObject {


    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(MannschaftsmitgliedDAO.class);

    // table name in the database
    private static final String TABLE = "mannschaftsmitglied";
    private static final String TABLE_ALIAS = "m";

    private static final String DSB_MITGLIED_TABLE = "dsb_mitglied";
    private static final String DSB_MITGLIED_TABLE_ALIAS = "d";
    private static final String DSB_MITGLIED_TABLE_MITGLIED_ID = "dsb_mitglied_id";
    // business entity parameter names

    private static final String MANNSCHAFTSMITGLIED_BE_ID = "id";
    private static final String MANNSCHAFTSMITGLIED_BE_TEAM_ID = "mannschaftId";
    private static final String MANNSCHAFTSMITGLIED_BE_DSB_MITGLIED_ID = "dsbMitgliedId";
    private static final String MANNSCHAFTSMITGLIED_BE_INSERT = "dsbMitgliedEingesetzt";
    private static final String MANNSCHAFTSMITGLIED_BE_RUECKENNUMMER = "rueckennummer";

    // new: important for the join with dsb_mitglied
    private static final String DSBMITGLIED_BE_FORENAME = "dsbMitgliedVorname";
    private static final String DSBMITGLIED_BE_SURNAME = "dsbMitgliedNachname";

    private static final String MANNSCHAFTSMITGLIED_TABLE_ID = "mannschaftsmitglied_id";
    private static final String MANNSCHAFTSMITGLIED_TABLE_TEAM_ID = "mannschaftsmitglied_mannschaft_id";
    private static final String MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID = "mannschaftsmitglied_dsb_mitglied_id";
    private static final String MANNSCHAFTSMITGLIED_TABLE_EMPLOYED = "mannschaftsmitglied_dsb_mitglied_eingesetzt";

    // new: important for the join with dsb_mitglied
    private static final String DSBMITGLIED_TABLE_FORENAME = "dsb_mitglied_vorname";
    private static final String DSBMITGLIED_TABLE_SURNAME = "dsb_mitglied_nachname";
    private static final String MANNSCHAFTSMITGLIED_TABLE_RUECKENNUMMER = "mannschaftsmitglied_rueckennummer";

    private static final String[] selectedFields = {
            MANNSCHAFTSMITGLIED_TABLE_ID, MANNSCHAFTSMITGLIED_TABLE_TEAM_ID, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID,
            MANNSCHAFTSMITGLIED_TABLE_EMPLOYED, DSBMITGLIED_TABLE_FORENAME, DSBMITGLIED_TABLE_SURNAME, MANNSCHAFTSMITGLIED_TABLE_RUECKENNUMMER
    };

    private static final String FIND_ALL = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .orderBy(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .compose().toString();

    private static final String FIND_BY_TEAM_ID = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .compose().toString();

    private static final String FIND_BY_MEMBER_AND_TEAM_ID = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID)
            .andEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .compose().toString();

    private static final String FIND_BY_TEAM_ID_AND_RUECKENNUMMER = new QueryBuilder()
            .selectFields(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID, MANNSCHAFTSMITGLIED_TABLE_RUECKENNUMMER, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID)
            .from(TABLE, TABLE_ALIAS)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_RUECKENNUMMER)
            .andEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .compose().toString();

    private static final String FIND_BY_MEMBER_ID = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID)
            .compose().toString();

    //hier suchen wir  alle Teammtiglieder, die eingesetzt wurden
    // d.h. nicht nur gemeldet, sondern sie haben auch Pfeilwerte erfasst
    private static final String FIND_ALL_SCHUETZE_TEAM_EINGESETZT = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereGteRaw(MANNSCHAFTSMITGLIED_TABLE_EMPLOYED, "1")
            .andEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .orderBy(MANNSCHAFTSMITGLIED_TABLE_ID)
            .compose().toString();

    //hier suchen wir  alle Teammtiglieder, die potentiell eingesetzt werden könnten
    // d.h. sie wurden für die Mannschaft gemeldet.
    private static final String FIND_ALL_SCHUETZE_TEAM = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .orderBy(MANNSCHAFTSMITGLIED_TABLE_ID)
            .compose().toString();

    // hier suchen wir die Anzahl aller Teammtiglieder, die potentiell eingesetzt werden könnten
    // d.h. sie wurden für die Mannschaft gemeldet.
    private static final String COUNT_ALL_SCHUETZE_TEAM = new QueryBuilder()
            .selectCount()
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .orderBy(MANNSCHAFTSMITGLIED_TABLE_ID)
            .compose().toString();




        /*The query gets a competition ID and team ID.
    In the first part of the query it is ensured that the shooters have not already participated in another competition on the same day.
    It is filtered by the competition ID and team ID. Then it is checked if the dsb_member has not already participated under another teamId.
    For this the results of a subquery are filtered out.
    The subquery checks by the competition_day and the team_id in the table passe, if the dsb_member has participated under another team in this year.


    In the second part of the query it is ensured that the shooters have participated in a competition in a higher league at most once (only on one day).
    For this purpose the results of the following subquery are filtered out.
    The subquery checks recursively if the dsb_member has participated in a higher league more than two times this year, using the attribute liga_overriding in the table liga.
    For this the event_id, competition_id are queried in advance.
    */
    private static final String FIND_SCHUETZE_IN_UEBERGELEGENER_LIGA =
            "SELECT DISTINCT m.* FROM mannschaftsmitglied m, wettkampf w, passe p, veranstaltung v, liga l\n" +
                    "WHERE m.mannschaftsmitglied_mannschaft_id = ?\n" +
                    "  AND w.wettkampf_id = ?\n" +
                    "  AND w.wettkampf_veranstaltung_id = v.veranstaltung_id\n" +
                    "\n" +
                    "  AND m.mannschaftsmitglied_dsb_mitglied_id NOT IN\n" +
                    "      (select DISTINCT passe.passe_dsb_mitglied_id\n" +
                    "        from passe, mannschaftsmitglied, wettkampf, veranstaltung\n" +
                    "        where veranstaltung.veranstaltung_sportjahr = v.veranstaltung_sportjahr\n" +
                    "          and wettkampf.wettkampf_tag = (select wettkampf.wettkampf_tag from wettkampf\n" +
                    "                                         where wettkampf.wettkampf_id = w.wettkampf_id)\n" +
                    "          and passe.passe_dsb_mitglied_id IN (select passe.passe_dsb_mitglied_id from passe\n" +
                    "                                              where passe_mannschaft_id != m.mannschaftsmitglied_mannschaft_id\n" +
                    "                                                and passe.passe_dsb_mitglied_id in\n" +
                    "                                                    (select mannschaftsmitglied_dsb_mitglied_id from mannschaftsmitglied\n" +
                    "                                                     where mannschaftsmitglied_mannschaft_id = m.mannschaftsmitglied_mannschaft_id)\n" +
                    "        )\n" +
                    "    )\n" +
                    "  AND m.mannschaftsmitglied_dsb_mitglied_id NOT IN(\n" +
                    "    SELECT passe_dsb_mitglied_id from (\n" +
                    "                                        select veranstaltung.veranstaltung_liga_id, passe.passe_dsb_mitglied_id, wettkampf.wettkampf_tag, wettkampf.wettkampf_id\n" +
                    "                                          from veranstaltung, wettkampf, passe\n" +
                    "                                          where veranstaltung.veranstaltung_id = wettkampf.wettkampf_veranstaltung_id\n" +
                    "                                            and passe.passe_wettkampf_id = wettkampf.wettkampf_id\n" +
                    "                                            and passe.passe_mannschaft_id = m.mannschaftsmitglied_mannschaft_id\n" +
                    "                                            and w.wettkampf_id != wettkampf.wettkampf_id\n" +
                    "                                            and v.veranstaltung_sportjahr = veranstaltung.veranstaltung_sportjahr\n" +
                    "                                            and veranstaltung.veranstaltung_liga_id in (\n" +
                    "                                              WITH RECURSIVE traverse AS (\n" +
                    "                                                  SELECT liga_uebergeordnet FROM liga\n" +
                    "                                                  WHERE liga.liga_id = v.veranstaltung_liga_id\n" +
                    "                                                  UNION ALL\n" +
                    "                                                  SELECT liga.liga_uebergeordnet FROM liga\n" +
                    "                                                                                      INNER JOIN traverse\n" +
                    "                                                                                                 ON liga.liga_id = traverse.liga_uebergeordnet\n" +
                    "                                              )\n" +
                    "                                              SELECT liga_uebergeordnet FROM traverse\n" +
                    "                                          )\n" +
                    "                                          group by veranstaltung.veranstaltung_liga_id, passe.passe_dsb_mitglied_id, wettkampf.wettkampf_tag, wettkampf.wettkampf_id\n" +
                    "                                      )AS Foo GROUP BY passe_dsb_mitglied_id HAVING ( COUNT(passe_dsb_mitglied_id) >= 2));";




    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<MannschaftsmitgliedBE> MANNSCHAFTSMITGLIED = new BusinessEntityConfiguration<>(
            MannschaftsmitgliedBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private static final BusinessEntityConfiguration<MannschaftsmitgliedExtendedBE> MANNSCHAFTSMITGLIED_EXTENDED = new BusinessEntityConfiguration<>(
            MannschaftsmitgliedExtendedBE.class, TABLE, getColumnsToFieldsMapExtended(), LOGGER);


    private final BasicDAO basicDao;



    @Autowired
    public MannschaftsmitgliedDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_ID, MANNSCHAFTSMITGLIED_BE_ID);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID, MANNSCHAFTSMITGLIED_BE_TEAM_ID);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, MANNSCHAFTSMITGLIED_BE_DSB_MITGLIED_ID);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_EMPLOYED, MANNSCHAFTSMITGLIED_BE_INSERT);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_RUECKENNUMMER, MANNSCHAFTSMITGLIED_BE_RUECKENNUMMER);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return columnsToFieldsMap;
    }

    private static Map<String, String> getColumnsToFieldsMapExtended() {
        final Map<String, String> columnsToFieldsMapExtended = new HashMap<>();

        columnsToFieldsMapExtended.put(MANNSCHAFTSMITGLIED_TABLE_ID, MANNSCHAFTSMITGLIED_BE_ID);
        columnsToFieldsMapExtended.put(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID, MANNSCHAFTSMITGLIED_BE_TEAM_ID);
        columnsToFieldsMapExtended.put(MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, MANNSCHAFTSMITGLIED_BE_DSB_MITGLIED_ID);
        columnsToFieldsMapExtended.put(MANNSCHAFTSMITGLIED_TABLE_EMPLOYED, MANNSCHAFTSMITGLIED_BE_INSERT);
        columnsToFieldsMapExtended.put(MANNSCHAFTSMITGLIED_TABLE_RUECKENNUMMER, MANNSCHAFTSMITGLIED_BE_RUECKENNUMMER);
        columnsToFieldsMapExtended.put(DSBMITGLIED_TABLE_FORENAME, DSBMITGLIED_BE_FORENAME);
        columnsToFieldsMapExtended.put(DSBMITGLIED_TABLE_SURNAME, DSBMITGLIED_BE_SURNAME);

        // add technical columns
        columnsToFieldsMapExtended.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return columnsToFieldsMapExtended;
    }



    /**
     * Return all mannschaftsmitglied entries
     */
    public List<MannschaftsmitgliedExtendedBE> findAll() {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED_EXTENDED, FIND_ALL);
    }

    /**
     * return all members in the team
     *
     * @param id
     *
     * @return
     */
    public List<MannschaftsmitgliedExtendedBE> findByTeamId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED_EXTENDED, FIND_BY_TEAM_ID, id);
    }


    public List<MannschaftsmitgliedExtendedBE> findAllSchuetzeInTeamEingesetzt(final long id) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED_EXTENDED, FIND_ALL_SCHUETZE_TEAM_EINGESETZT, id);
    }

    public List<MannschaftsmitgliedExtendedBE> findAllSchuetzeInTeam(final long id) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED_EXTENDED, FIND_ALL_SCHUETZE_TEAM, id);
    }

    public List<MannschaftsmitgliedExtendedBE> countAllSchuetzeInTeam(final long id) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED_EXTENDED, COUNT_ALL_SCHUETZE_TEAM, id);
    }

    public List<MannschaftsmitgliedExtendedBE> findSchuetzenInUebergelegenerLiga( final long mannschaftsId, final long wettkampfId) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED_EXTENDED, FIND_SCHUETZE_IN_UEBERGELEGENER_LIGA,  mannschaftsId, wettkampfId);
    }

    public MannschaftsmitgliedExtendedBE findByMemberAndTeamId(long teamId, final long memberId) {
       return basicDao.selectSingleEntity(MANNSCHAFTSMITGLIED_EXTENDED, FIND_BY_MEMBER_AND_TEAM_ID, memberId, teamId);
    }

    public MannschaftsmitgliedExtendedBE findByTeamIdAndRueckennummer(long teamId, final long rueckennummer) {
        return basicDao.selectSingleEntity(MANNSCHAFTSMITGLIED_EXTENDED, FIND_BY_TEAM_ID_AND_RUECKENNUMMER, rueckennummer, teamId);
    }

    public List<MannschaftsmitgliedExtendedBE> findByMemberId(final long memberId) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED_EXTENDED, FIND_BY_MEMBER_ID, memberId);
    }


    public MannschaftsmitgliedBE create(final MannschaftsmitgliedBE mannschaftsmitgliedBE, final long currentMemberId) {
        basicDao.setCreationAttributes(mannschaftsmitgliedBE, currentMemberId);

        return basicDao.insertEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE);
    }


    public MannschaftsmitgliedBE update(final MannschaftsmitgliedBE mannschaftsmitgliedBE, final long currentMemberId) {
        basicDao.setModificationAttributes(mannschaftsmitgliedBE, currentMemberId);

        return basicDao.updateEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE, MANNSCHAFTSMITGLIED_BE_ID);
    }


    public void delete(final MannschaftsmitgliedBE mannschaftsmitgliedBE, final long currentMemberId) {
        basicDao.setModificationAttributes(mannschaftsmitgliedBE, currentMemberId);
        basicDao.deleteEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE, MANNSCHAFTSMITGLIED_BE_ID);
    }


    public boolean checkExistingSchuetze(long teamId, final long memberId) {
        return findByMemberAndTeamId(teamId, memberId).getDsbMitgliedEingesetzt() > 0;
    }
}