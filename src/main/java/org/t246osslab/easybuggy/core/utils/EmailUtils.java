package org.t246osslab.easybuggy.core.utils;
 
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * A utility class for sending e-mail message with attachment.
 *
 */
public class EmailUtils {
    
    private static final Logger log = LoggerFactory.getLogger(EmailUtils.class);

    public static boolean isReadyToSendEmail() {
        if (ApplicationUtils.getSmtpHost() == null || "".equals(ApplicationUtils.getSmtpHost())
                || ApplicationUtils.getSmtpPort() == null || "".equals(ApplicationUtils.getSmtpPort())
                || ApplicationUtils.getAdminAddress() == null || "".equals(ApplicationUtils.getAdminAddress())) {
            return false;
        }
        return true;
    }
    
    /**
     * Sends an e-mail message from a SMTP host with a list of attached files.
     */
    public static void sendEmailWithAttachment(
            String subject, String message, List<File> attachedFiles)
                    throws AddressException, MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", ApplicationUtils.getSmtpHost());
        properties.put("mail.smtp.port", ApplicationUtils.getSmtpPort());
        properties.put("mail.smtp.auth", ApplicationUtils.getSmtpAuth());
        properties.put("mail.smtp.starttls.enable", ApplicationUtils.getSmtpStarttlsEnable());
        properties.put("mail.user", ApplicationUtils.getSmtpUser());
        properties.put("mail.password", ApplicationUtils.getSmtpPass());
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ApplicationUtils.getSmtpUser(), ApplicationUtils.getSmtpPass());
            }
        };
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress(ApplicationUtils.getSmtpUser()));
        InternetAddress[] toAddresses = { new InternetAddress(ApplicationUtils.getAdminAddress()) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
 
        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");
 
        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
 
        // adds attachments
        if (attachedFiles != null && attachedFiles.size() > 0) {
            for (File aFile : attachedFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();
 
                try {
                    attachPart.attachFile(aFile);
                } catch (IOException e) {
                    log.error("IOException occurs: ", e);
                }
 
                multipart.addBodyPart(attachPart);
            }
        }
 
        // sets the multi-part as e-mail's content
        msg.setContent(multipart);
 
        // sends the e-mail
        Transport.send(msg);
    }
}