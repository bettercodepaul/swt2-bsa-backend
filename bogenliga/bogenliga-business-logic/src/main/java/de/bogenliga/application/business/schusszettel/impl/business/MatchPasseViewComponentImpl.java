package de.bogenliga.application.business.schusszettel.impl.business;

import java.util.ArrayList;
import java.util.List;
import de.bogenliga.application.business.schusszettel.api.MatchPasseViewComponent;
import de.bogenliga.application.business.schusszettel.api.types.MatchPasseViewDO;

/**
 *
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public class MatchPasseViewComponentImpl implements MatchPasseViewComponent {
   //private final MatchPasseViewDAO;

    public MatchPasseViewComponentImpl() {
        //this.MatchPasseViewDAO = dao;
    }


    @Override
    public List<MatchPasseViewDO> findAll() {
        final ArrayList<MatchPasseViewDO> returnList = new ArrayList<>();
        //final List<MatchPasseBE> matchPasseBEList = MatchPasseDAO.findAll();
//        for (int i = 0; i < matchPasseBEList.size(); i++) {
//
//            returnList.add(i, matchPasseBEList.get(i));
//
//        }
    return returnList;
    }


    @Override
    public MatchPasseViewDO findByIdsMatchNr(long wettkampfId, long matchId, int matchNr) {
        return null;
    }


    @Override
    public MatchPasseViewDO findByIdsPasseId(long wettkampfId, long matchId, long passeId) {
        return null;
    }
}
