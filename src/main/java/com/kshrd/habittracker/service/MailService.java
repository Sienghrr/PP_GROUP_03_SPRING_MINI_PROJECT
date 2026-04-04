    package com.kshrd.habittracker.service;

    import jakarta.mail.internet.MimeMessage;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.mail.javamail.MimeMessageHelper;
    import org.springframework.stereotype.Service;
    import org.thymeleaf.context.Context;
    import org.thymeleaf.spring6.SpringTemplateEngine;

    import java.nio.charset.StandardCharsets;


    @Service
    @RequiredArgsConstructor
    public class MailService {

        @Value("${spring.mail.username}")
        private String fromEmail;
        private final JavaMailSender mailSender;
        private final SpringTemplateEngine templateEngine;

        public void sendOtpVerificationEmail(String to, String username, String otp) {
            try {
                Context context = new Context();
                context.setVariable("username", username);
                context.setVariable("otp", otp);

                String html = templateEngine.process("verify-otp", context);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

                helper.setFrom(fromEmail);
                helper.setTo(to);
                helper.setSubject("Verify your account");
                helper.setText(html, true);

                mailSender.send(message);
            } catch (Exception e) {
                throw new RuntimeException("Failed to send verification email", e);
            }
        }
    }