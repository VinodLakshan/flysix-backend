package com.edu.flysixbackend.service;

import com.edu.flysixbackend.dto.EmailDto;

public interface EmailService {

    boolean sendConfirmationMail(EmailDto confirmEmail);
}
