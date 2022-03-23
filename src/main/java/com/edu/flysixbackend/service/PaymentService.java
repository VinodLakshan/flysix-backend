package com.edu.flysixbackend.service;

import com.edu.flysixbackend.dto.PaymentDto;

import javax.servlet.http.HttpServletResponse;

public interface PaymentService {

    String makePayment(PaymentDto paymentDto);
}
