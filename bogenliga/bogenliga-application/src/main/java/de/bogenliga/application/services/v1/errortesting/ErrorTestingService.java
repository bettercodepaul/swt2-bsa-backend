package de.bogenliga.application.services.v1.errortesting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.errorhandling.ErrorCategory;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.service.ServiceFacade;

/**
 * I´m a REST resource and produce errors for the error handling tests
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">Wikipedia - CRUD</a>
 * @see <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">Wikipedia - REST</a>
 * @see <a href="https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">Wikipedia - HTTP</a>
 * @see <a href="https://en.wikipedia.org/wiki/Design_by_contract">Wikipedia - Design by contract</a>
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @see <a href="https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration">
 * Build a REST API with Spring 4 and Java Config</a>
 * @see <a href="https://www.baeldung.com/spring-autowire">Guide to Spring @Autowired</a>
 */
@RestController
@RequestMapping("v1/error-testing")
public class ErrorTestingService implements ServiceFacade {

    private static final Logger logger = LoggerFactory.getLogger(ErrorTestingService.class);


    @GetMapping(value = "/error-code/{errorCode}")
    public String generateErrorCode(@PathVariable("errorCode") final String errorCode) {
        logger.debug("Receive 'generateErrorCode' request with {}", errorCode);

        final ErrorCode error = ErrorCode.fromValue(errorCode);
        if (error == null) {
            throw new NullPointerException("NullPointerException: Error not found");
        } else if (error.getErrorCategory() == ErrorCategory.BUSINESS) {
            throw new BusinessException(error, "Message for " + error.getValue(), error.getErrorCategory(),
                    error.getValue());
        } else if (error.getErrorCategory() == ErrorCategory.TECHNICAL) {
            throw new TechnicalException(error, "Message for " + error.getValue(), error.getErrorCategory(),
                    error.getValue());
        } else {
            throw new RuntimeException("RuntimeException: Undefined error");
        }
    }
}
