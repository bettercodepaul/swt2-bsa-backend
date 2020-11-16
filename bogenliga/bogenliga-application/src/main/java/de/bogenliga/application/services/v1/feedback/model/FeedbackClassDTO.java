package de.bogenliga.application.services.v1.feedback.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class FeedbackClassDTO implements DataTransferObject {
    private String feedBack;


    private FeedbackClassDTO (String mfeedBack) {
        this.feedBack = mfeedBack;
    }
}
