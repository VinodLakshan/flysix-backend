package com.edu.flysixbackend.service;

import com.edu.flysixbackend.dto.PaymentDto;
import com.stripe.exception.StripeException;

import javax.servlet.http.HttpServletResponse;

public interface PaymentService {

    String makePayment(PaymentDto paymentDto) throws StripeException;

    boolean confirmPayment(Long bookingId);
}
