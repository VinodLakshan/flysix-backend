package com.edu.flysixbackend.service;

import com.edu.flysixbackend.dto.PaymentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PaymentService {

    Session createPaymentSession(PaymentDto paymentDto) throws StripeException;

    Object webhook(HttpServletRequest request, HttpServletResponse response, String payload);
}
