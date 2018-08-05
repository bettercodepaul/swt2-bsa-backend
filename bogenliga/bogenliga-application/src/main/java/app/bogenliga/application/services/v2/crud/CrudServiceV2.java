package app.bogenliga.application.services.v2.crud;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import app.bogenliga.application.business.EntityNotFoundException;
import app.bogenliga.application.services.common.CrudServiceInterface;
import app.bogenliga.application.services.common.DataTransferObject;

@RestController
@RequestMapping("v2/crud")
public class CrudServiceV2 implements CrudServiceInterface {

    private static final String template = "Hi, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Override
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DataTransferObject> findAll() {
        throw new EntityNotFoundException();
    }


    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataTransferObject findById(@PathVariable(name="id") Long id) {
        return new CrudTO();
    }


    @Override
    @RequestMapping(method = RequestMethod.POST)
    public DataTransferObject create(@Valid @RequestBody DataTransferObject dataTransferObject) {
        return dataTransferObject;
    }


    @Override
    @RequestMapping(method = RequestMethod.PUT)
    public DataTransferObject update(@Valid @RequestBody DataTransferObject dataTransferObject) {
        return dataTransferObject;
    }


    @Override
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(name="id") Long id) {

    }
}
