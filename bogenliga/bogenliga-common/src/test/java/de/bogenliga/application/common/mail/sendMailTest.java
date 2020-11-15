package de.bogenliga.application.common.mail;

import org.junit.Test;

/**
 * TODO [AL] class documentation
 *
 * @author Fabio Care WS2020 SWT2
 */
public class sendMailTest {

    private MailSender mailSender = new MailSender();

    @Test
    public void testSendMail() {
        mailSender.sendMail();
    }
}
