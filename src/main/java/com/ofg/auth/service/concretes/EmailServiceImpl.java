package com.ofg.auth.service.concretes;

import java.util.Properties;

import com.ofg.auth.config.AppProperties;
import com.ofg.auth.exception.email.EmailServiceException;
import com.ofg.auth.service.abstracts.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSenderImpl mailSender;
    private final AppProperties appProperties;
    private final MessageSource messageSource;

    @Autowired
    public EmailServiceImpl(AppProperties appProperties,
                            MessageSource messageSource) {
        this.appProperties = appProperties;
        this.messageSource = messageSource;
        this.mailSender = new JavaMailSenderImpl();
    }

    @PostConstruct
    public void initialize() {
        mailSender.setHost(appProperties.getEmail().host());
        mailSender.setPort(appProperties.getEmail().port());
        mailSender.setUsername(appProperties.getEmail().username());
        mailSender.setPassword(appProperties.getEmail().password());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", "true");
    }

    @Override
    public void sendActivationEmail(String email, String activationToken) {
        String activationUrl = buildActivationUrl(activationToken);
        String mailBody = buildEmailBody("app.msg.mail.user.created.title", activationUrl);
        sendEmail(email, mailBody, "Activation Email");
    }

    @Override
    public void sendPasswordResetEmail(String email, String passwordResetToken) {
        String passwordResetUrl = appProperties.getClient().host() + "/password-reset/set?tk=" + passwordResetToken;
        String mailBody = buildEmailBody("app.msg.mail.user.password.reset.title", passwordResetUrl);

        sendEmail(email, mailBody, "Password Reset Email");
    }

    @Override
    public void sendEmail(String to, String body, String subject) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;

        try {
            messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            messageHelper.setFrom(appProperties.getEmail().from());
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailServiceException();
        }
    }

    private String buildEmailBody(String titleKey, String url) {
        String title = messageSource.getMessage(titleKey, null, LocaleContextHolder.getLocale());
        String clickHere = messageSource.getMessage("app.msg.mail.click.here", null, LocaleContextHolder.getLocale());

        return new StringBuilder()
                .append("<html><body>")
                .append("<h1>").append(title).append("</h1>")
                .append("<a href=\"").append(url).append("\">").append(clickHere).append("</a>")
                .append("</body></html>")
                .toString();
    }

    private String buildActivationUrl(String activationToken) {
        return appProperties.getClient().host() + "/users/activate/" + activationToken;
    }
}
