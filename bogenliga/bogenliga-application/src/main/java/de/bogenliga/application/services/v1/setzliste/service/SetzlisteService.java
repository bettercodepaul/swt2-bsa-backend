package de.bogenliga.application.services.v1.setzliste.service;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.business.Setzliste.api.SetzlisteComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * This is a rest resource that generates the matches.
 *
 * @author Robin Müller, Marcel Neumann
 * @see <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">Wikipedia - CRUD</a>
 * @see <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">Wikipedia - REST</a>
 * @see <a href="https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">Wikipedia - HTTP</a>
 * @see <a href="https://en.wikipedia.org/wiki/Design_by_contract">Wikipedia - Design by contract</a>
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @see <a href="https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-dsbMitglied">
 * Build a REST API with Spring 4 and Java Config</a>
 * @see <a href="https://www.baeldung.com/spring-autowire">Guide to Spring @Autowired</a>
 */
@RestController
@RequestMapping("v1/setzliste")
public class SetzlisteService implements ServiceFacade {

    private static final Logger LOG = LoggerFactory.getLogger(SetzlisteService.class);

    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final SetzlisteComponent setzlisteComponent;

    /**
     * Constructor with dependency injection
     */
    @Autowired
    public SetzlisteService(final SetzlisteComponent setzlisteComponent) {
        this.setzlisteComponent = setzlisteComponent;
    }

    @CrossOrigin(maxAge = 0)
    @RequestMapping(method = RequestMethod.GET,
            path = "/generate")
    public @ResponseBody
    List<MatchDTO> generateSetzliste(@RequestParam("wettkampfid") final long wettkampfid) {

        Preconditions.checkNotNull(wettkampfid, "wettkampfid must not be null");

        List<MatchDO>  matchDOList = this.setzlisteComponent.generateMatchesBySetzliste(wettkampfid);
        List<MatchDTO> matchDTOList = new List<MatchDTO>() {
            @Override
            public int size() {
                return 0;
            }


            @Override
            public boolean isEmpty() {
                return false;
            }


            @Override
            public boolean contains(Object o) {
                return false;
            }


            @Override
            public Iterator<MatchDTO> iterator() {
                return null;
            }


            @Override
            public Object[] toArray() {
                return new Object[0];
            }


            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }


            @Override
            public boolean add(MatchDTO matchDTO) {
                return false;
            }


            @Override
            public boolean remove(Object o) {
                return false;
            }


            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }


            @Override
            public boolean addAll(Collection<? extends MatchDTO> c) {
                return false;
            }


            @Override
            public boolean addAll(int index, Collection<? extends MatchDTO> c) {
                return false;
            }


            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }


            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }


            @Override
            public void clear() {

            }


            @Override
            public MatchDTO get(int index) {
                return null;
            }


            @Override
            public MatchDTO set(int index, MatchDTO element) {
                return null;
            }


            @Override
            public void add(int index, MatchDTO element) {

            }


            @Override
            public MatchDTO remove(int index) {
                return null;
            }


            @Override
            public int indexOf(Object o) {
                return 0;
            }


            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }


            @Override
            public ListIterator<MatchDTO> listIterator() {
                return null;
            }


            @Override
            public ListIterator<MatchDTO> listIterator(int index) {
                return null;
            }


            @Override
            public List<MatchDTO> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        for (MatchDO matchDO : matchDOList) {
            matchDTOList.add(MatchDTOMapper.toDTO.apply(matchDO));
        }
        return matchDTOList;
    }
}