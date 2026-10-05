package murach.email;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for sending emails via SMTP.
 *
 * Configuration is read from environment variables (best practice for Docker):
 *   MAIL_HOST     - SMTP host       (default: smtp.gmail.com)
 *   MAIL_PORT     - SMTP port       (default: 587)
 *   MAIL_USERNAME - Sender address  (e.g. yourapp@gmail.com)
 *   MAIL_PASSWORD - App password    (Gmail App Password, NOT your login password)
 *   MAIL_FROM     - Display name + address (optional, defaults to MAIL_USERNAME)
 */
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    // Read config from environment variables (works with Docker -e flags)
    private static final String SMTP_HOST =
            getEnv("MAIL_HOST", "smtp.gmail.com");
    private static final int SMTP_PORT =
            Integer.parseInt(getEnv("MAIL_PORT", "587"));
    private static final String USERNAME =
            getEnv("MAIL_USERNAME", "");
    private static final String PASSWORD =
            getEnv("MAIL_PASSWORD", "");
    private static final String FROM_ADDRESS =
            getEnv("MAIL_FROM", USERNAME);

    /**
     * Send a welcome / confirmation email to the registered user.
     *
     * @param toEmail    recipient email address
     * @param firstName  recipient first name (used in greeting)
     * @param lastName   recipient last name
     * @throws MessagingException if sending fails
     */
    public static void sendWelcomeEmail(String toEmail,
                                        String firstName,
                                        String lastName)
            throws MessagingException {

        if (USERNAME.isBlank() || PASSWORD.isBlank()) {
            String msg = "Mail credentials not configured. Set MAIL_USERNAME and MAIL_PASSWORD environment variables.";
            logger.warning(msg);
            throw new IllegalStateException(msg);   // ← fix: báo lỗi để servlet set emailSent=false
        }

        Session session = buildSession();

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(FROM_ADDRESS, "Email List", "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress(FROM_ADDRESS));
        }
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail));
        message.setSubject("Welcome to our email list!");
        message.setContent(buildHtmlBody(firstName, lastName, toEmail), "text/html; charset=UTF-8");

        Transport.send(message);
        logger.info("Welcome email sent to: " + toEmail);
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private static Session buildSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");   // STARTTLS (port 587)
        props.put("mail.smtp.host",            SMTP_HOST);
        props.put("mail.smtp.port",            SMTP_PORT);
        props.put("mail.smtp.ssl.protocols",   "TLSv1.2 TLSv1.3");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    /** Build a simple HTML email body. */
    private static String buildHtmlBody(String firstName, String lastName, String email) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="font-family: Arial, Helvetica, sans-serif; font-size: 11pt; margin: 2em;">
                    <h2 style="color: teal;">Thanks for joining our email list!</h2>
                    <p>Hi <strong>%s %s</strong>,</p>
                    <p>You have successfully joined our email list using the address:
                       <strong>%s</strong></p>
                    <p>We'll keep you updated with the latest news.</p>
                    <hr>
                    <p style="font-size: 9pt;">&copy; %d Mike Murach &amp; Associates</p>
                </body>
                </html>
                """.formatted(firstName, lastName, email,
                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
    }

    private static String getEnv(String name, String defaultValue) {
        String val = System.getenv(name);
        return (val != null && !val.isBlank()) ? val : defaultValue;
    }
}
