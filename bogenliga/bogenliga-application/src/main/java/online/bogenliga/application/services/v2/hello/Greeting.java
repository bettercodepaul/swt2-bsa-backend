package online.bogenliga.application.services.v2.hello;

import online.bogenliga.application.common.service.types.DataTransferObject;

/**
 * i´m a dummy {@link DataTransferObject} for the hello world example
 *
 * @deprecated Remove REST service version dummy
 */
@Deprecated
public class Greeting implements DataTransferObject {

    private long id;
    private String content;


    /**
     * Default constructor
     */
    public Greeting() {

    }


    /**
     * Constructor with mandatory parameters
     */
    public Greeting(final long id, final String content) {
        this.id = id;
        this.content = content;
    }


    public long getId() {
        return id;
    }


    public String getContent() {
        return content;
    }


    public void setId(final long id) {
        this.id = id;
    }


    public void setContent(final String content) {
        this.content = content;
    }
}
