package de.bogenliga.application.services.v1.feedback.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I'm the data transfer object of the feedback.
 * <p>
 * I define the payload for the external REST interface of the feedback business entity.
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */
public class FeedbackClassDTO implements DataTransferObject {
    private String feedBack;
}
