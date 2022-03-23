package com.edu.flysixbackend.service.impl;

import com.edu.flysixbackend.dto.PaymentDto;
import com.edu.flysixbackend.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.apiKey}")
    private String apiKey;

    @Override
    public String makePayment(PaymentDto paymentDto) {

        Stripe.apiKey = apiKey;

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(paymentDto.getSuccessUrl())
                    .setCancelUrl(paymentDto.getCancelUrl())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(paymentDto.getCurrency())
                                                    .setUnitAmount(paymentDto.getPrice() * 100)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Total Amount")
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();

            Session session = Session.create(params);
            return session.getUrl();

        } catch (StripeException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
