package com.edu.flysixbackend.service.impl;

import com.edu.flysixbackend.constants.Const;
import com.edu.flysixbackend.dto.PaymentDto;
import com.edu.flysixbackend.model.Booking;
import com.edu.flysixbackend.repository.BookingRepository;
import com.edu.flysixbackend.repository.PaymentRepository;
import com.edu.flysixbackend.service.BookingService;
import com.edu.flysixbackend.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.apiKey}")
    private String apiKey;

    @Value("${stripe.endpoint-secret}")
    private String endpointSecret;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Override
    public Session createPaymentSession(PaymentDto paymentDto) throws StripeException {

        Stripe.apiKey = apiKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setClientReferenceId(paymentDto.getBookingId().toString())
                .setSuccessUrl(paymentDto.getSuccessUrl() + "?bookingId=" + paymentDto.getBookingId() +
                        "&type=" + Const.BOOKING_CONFIRMED)
                .setCancelUrl(paymentDto.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(paymentDto.getCurrency())
                                                .setUnitAmount((long) (paymentDto.getPrice() * 100))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Total Amount")
                                                                .build())
                                                .build())
                                .build())
                .build();

        return Session.create(params);
    }

    @Override
    public Object webhook(HttpServletRequest request, HttpServletResponse response, String payload) {

        Stripe.apiKey = apiKey;
        String sigHeader = request.getHeader("Stripe-Signature");
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

        } catch (SignatureVerificationException e) {
            response.setStatus(400);
            return "";
        }

        if ("checkout.session.completed".equals(event.getType())) {

            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

            if (dataObjectDeserializer.getObject().isPresent()) {
                Session session = (Session) dataObjectDeserializer.getObject().get();
                bookingService.confirmBooking(Long.parseLong(session.getClientReferenceId()));
            }

        }

        response.setStatus(200);
        return "";

    }

}
