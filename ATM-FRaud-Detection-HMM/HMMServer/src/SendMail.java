

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class SendMail {

    String d_email = "",
            d_password = "",
            d_host = "",
            d_port = "";

    public SendMail(String EmailAddress, String EmailPassword, String to, String sub, String txt, String FileName, String Option) {
        try {
            if (Option.toLowerCase().equals("gmail")) {
                d_host = "smtp.gmail.com";
                d_port = "465";
            } else if (Option.toLowerCase().equals("yahoo")) {
                d_host = "smtp.mail.yahoo.com";
                d_port = "465";
            } else if (Option.toLowerCase().equals("aol")) {
                d_host = "smtp.aol.com";
                d_port = "465";
            }
            d_email = EmailAddress;
            d_password = EmailPassword;
            Properties props = new Properties();
            props.put("mail.smtp.user", d_email);
            props.put("mail.smtp.host", d_host);
            props.put("mail.smtp.port", d_port);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.socketFactory.port", d_port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            try {
                Authenticator auth = new SMTPAuthenticator();
                Session session = Session.getInstance(props, auth);
                MimeMessage msg = new MimeMessage(session);
                msg.setSubject(sub);
                msg.setFrom(new InternetAddress(d_email));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                MimeBodyPart messagePart = new MimeBodyPart();
                messagePart.setText(txt);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messagePart);
                MimeBodyPart attachmentPart = new MimeBodyPart();
//                if (FileName != null) {
//                    FileName =FileName;
//                    FileDataSource f = new FileDataSource(FileName);
//                    attachmentPart.setDataHandler(new DataHandler(f));
//                    FileName = FileName.substring(FileName.lastIndexOf('\\') + 1);
//                    System.out.println("FileName:" + FileName);
//                    attachmentPart.setFileName(FileName);
//                    multipart.addBodyPart(attachmentPart);
//                }
                msg.setContent(multipart);
                Transport.send(msg);
                if (FileName != null) {
                    File f = new File(FileName);
                    if (f.exists()) {
                        f.delete();
                    }
                }
            } catch (Exception mex) {
                mex.printStackTrace();
            }

        } catch (Exception ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(d_email, d_password);
        }
    }

   
}
