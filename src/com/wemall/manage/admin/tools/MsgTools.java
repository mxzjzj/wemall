package com.wemall.manage.admin.tools;

import com.wemall.core.tools.PopupAuthenticator;
import com.wemall.core.tools.SmsBase;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * ����Ϣ�������
 */
@Component
public class MsgTools {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserService userService;

    public boolean sendSMS(String mobile, String content)
    throws UnsupportedEncodingException {
        boolean result = true;
        String url = this.configService.getSysConfig().getSmsURL();
        String userName = this.configService.getSysConfig().getSmsUserName();
        String password = this.configService.getSysConfig().getSmsPassword();
        SmsBase sb = new SmsBase("http://service.winic.org/sys_port/gateway/", userName, password);
        String ret = sb.SendSms(mobile, content);
        if (!ret.substring(0, 3).equals("000")) {
            result = false;
        }

        return result;
    }

    public boolean sendEmail(String email, String subject, String content) {
        boolean ret = true;
        String username = "";
        String password = "";
        String smtp_server = "";
        String from_mail_address = "";
        username = this.configService.getSysConfig().getEmailUserName();
        password = this.configService.getSysConfig().getEmailPws();
        smtp_server = this.configService.getSysConfig().getEmailHost();
        from_mail_address = this.configService.getSysConfig().getEmailUser();
        String to_mail_address = email;
        if ((username != null) && (password != null) && (!username.equals("")) &&
                (!password.equals("")) && (smtp_server != null) &&
                (!smtp_server.equals("")) && (to_mail_address != null) &&
                (!to_mail_address.trim().equals(""))) {
            Authenticator auth = new PopupAuthenticator(username, password);
            Properties mailProps = new Properties();
            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("username", username);
            mailProps.put("password", password);
            mailProps.put("mail.smtp.host", smtp_server);
            Session mailSession = Session.getInstance(mailProps, auth);
            MimeMessage message = new MimeMessage(mailSession);
            try {
                message.setFrom(new InternetAddress(from_mail_address));
                message.setRecipient(Message.RecipientType.TO,
                                     new InternetAddress(to_mail_address));
                message.setSubject(subject);
                MimeMultipart multi = new MimeMultipart("related");
                BodyPart bodyPart = new MimeBodyPart();
                bodyPart.setDataHandler(
                    new DataHandler(content,
                                    "text/html;charset=UTF-8"));

                multi.addBodyPart(bodyPart);
                message.setContent(multi);
                message.saveChanges();
                Transport.send(message);
                ret = true;
            } catch (AddressException e) {
                ret = false;
                e.printStackTrace();
            } catch (MessagingException e) {
                ret = false;
                e.printStackTrace();
            }
        } else {
            ret = false;
        }

        return ret;
    }
}




