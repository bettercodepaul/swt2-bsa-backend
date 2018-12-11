package de.bogenliga.application.services.common.errorhandling;

import de.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * I´m used to show backend errors on the user interface
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ErrorDTO {
    private final ErrorCode errorCode;
    private final String[] param;
    private final String errorMessage;


    /**
     * Constructor with all parameter
     *
     * @param errorCode    to identify the specific error and translate the error on the user interface
     * @param param        optional parameter to describe the error cause
     * @param errorMessage optional errorMessage of the exception (can be removed in a later version of the project)
     */
    public ErrorDTO(final ErrorCode errorCode, final String[] param, final String errorMessage) {
        this.errorCode = errorCode;
        this.param = param;
        this.errorMessage = errorMessage;
    }


    /**
     * Constructor with all parameter
     *
     * @param errorCode    to identify the specific error and translate the error on the user interface
     * @param errorMessage optional errorMessage of the exception (can be removed in a later version of the project)
     */
    public ErrorDTO(final ErrorCode errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        param = new String[0];
        this.errorMessage = errorMessage;
    }


    /**
     * Constructor with all parameter
     *
     * @param errorCode to identify the specific error and translate the error on the user interface
     */
    public ErrorDTO(final ErrorCode errorCode) {
        this.errorCode = errorCode;
        param = new String[0];
        errorMessage = null;
    }


    public ErrorCode getErrorCode() {
        return errorCode;
    }


    public String[] getParam() {
        return param;
    }


    public String getErrorMessage() {
        return errorMessage;
    }
}
