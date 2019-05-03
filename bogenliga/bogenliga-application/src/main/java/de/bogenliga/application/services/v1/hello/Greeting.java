package de.bogenliga.application.services.v1.hello;

import de.bogenliga.application.common.service.types.DataTransferObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * i´m a dummy {@link DataTransferObject} for the hello world example
 *
 * @deprecated Remove REST service version dummy
 */
@Deprecated
@ApiModel("Greeting")
public class Greeting implements DataTransferObject {

    private static final long serialVersionUID = 3918949409662120964L;
    @ApiModelProperty(notes = "Number of greeting", example = "1")
    private long id;
    @ApiModelProperty(notes = "Greeting content", example = "Stranger")
    private String content;


    /**
     * Default constructor
     */
    public Greeting() {

    }


    /**
     * Constructor with mandatory parameters
     */
    Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }
}
