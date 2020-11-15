package de.bogenliga.application.common.mail;

import javax.mail.*;
import javax.mail.internet.*;
/**
 * TODO [AL] class documentation
 *
 * @author Fabio Care WS2020 SWT2
 */
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSender {
    public void sendMail(String subject, String text) {
/*              // Recipient's email ID needs to be mentioned.
                String to = "f.care@gmx.de";

                // Sender's email ID needs to be mentioned
                String from = "fabio.care@gmx.de";

                // Assuming you are sending email from localhost
                String host = "localhost";

                // Get system properties
                Properties properties = System.getProperties();

                // Setup mail server
                properties.setProperty("mail.smtp.host", host);

                // Get the default Session object.
                Session session = Session.getDefaultInstance(properties);

                try {
                    // Create a default MimeMessage object.
                    MimeMessage message = new MimeMessage(session);

                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));

                    // Set To: header field of the header.
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                    // Set Subject: header field
                    message.setSubject("This is the Subject Line!");

                    // Now set the actual message
                    message.setText("This is actual message");

                    // Send message
                    Transport.send(message);
                    System.out.println("Sent message successfully....");
                } catch (MessagingException mex) {
                    mex.printStackTrace();
                }
            }*/

        private EmailGetterRobert emailGetterRobert = new EmailGetterRobert();

        String[] recipients = emailGetterRobert.getMails();

        final String username = "bogenligatest@gmail.com";
        final String password = "mki4bogenliga";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bogenligatest@gmail.com"));
            for (String recipient : recipients) {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipient));
            }
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}