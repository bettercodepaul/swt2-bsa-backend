package app.bogenliga.application.services.v1.hello;

import app.bogenliga.application.common.service.types.DataTransferObject;

@Deprecated
public class Greeting implements DataTransferObject {

    private final long id;
    private final String content;


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

}
