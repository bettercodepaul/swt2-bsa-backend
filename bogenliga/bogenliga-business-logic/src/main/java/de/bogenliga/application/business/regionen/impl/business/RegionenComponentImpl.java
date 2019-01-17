package de.bogenliga.application.business.regionen.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.dao.RegionenDAO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.business.regionen.impl.mapper.RegionenMapper;

/**
 * Implementation of {@link RegionenComponent}
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
@Component
public class RegionenComponentImpl implements RegionenComponent {

    //no need for preconditions since we only need to implement FindAll()

    public final RegionenDAO regionenDAO;


    /**
     * Constructor Dependeny injection with {@link org.springframework.beans.factory.annotation.Autowired}
     *
     * @param regionenDAO
     */
    public RegionenComponentImpl(RegionenDAO regionenDAO) {
        this.regionenDAO = regionenDAO;
    }


    @Override
    public List<RegionenDO> findAll() {
        final List<RegionenBE> regionenBEList = regionenDAO.findAll();
        return regionenBEList.stream().map(RegionenMapper.toRegionDO).collect(Collectors.toList());
    }


    @Override
    public List<RegionenDO> findAllByType(final String type) {
        final List<RegionenBE> regionenBEList = regionenDAO.findAllByType(type);
        return regionenBEList.stream().map(RegionenMapper.toRegionDO).collect(Collectors.toList());
    }
}
