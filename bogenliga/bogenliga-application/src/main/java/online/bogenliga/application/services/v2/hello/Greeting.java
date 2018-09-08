package online.bogenliga.application.services.v2.hello;

import online.bogenliga.application.common.service.types.DataTransferObject;

/**
 * @deprecated Remove REST service version dummy
 */
@Deprecated
public class Greeting implements DataTransferObject {

    private long id;
    private String content;


    public Greeting() {

    }


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
