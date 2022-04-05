package com.edu.flysixbackend.service.impl;

import com.edu.flysixbackend.dto.EmailDto;
import com.edu.flysixbackend.model.Booking;
import com.edu.flysixbackend.repository.BookingRepository;
import com.edu.flysixbackend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public boolean sendConfirmationMail(EmailDto confirmEmail) {

        try {
            Context context = new Context();
            context.setVariable("confirmEmail", confirmEmail);

            String process = templateEngine.process("index", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setSubject("FlySix - Yay! Your booking is confirmed");
            helper.setText(process, true);
            helper.setTo(confirmEmail.getTo());
            javaMailSender.send(mimeMessage);

            log.info("Confirmation email sent");
            return true;

        } catch (MessagingException e) {
            log.error("Confirmation email process failed.");
            log.error(e.getMessage());
            return false;

        } catch (Exception e) {
            log.error("Confirmation email process failed.");
            log.error(e.getMessage());
            return false;
        }
    }
}
