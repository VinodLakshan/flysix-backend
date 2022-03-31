package com.edu.flysixbackend.service.impl;

import com.edu.flysixbackend.constants.Const;
import com.edu.flysixbackend.dto.PaymentDto;
import com.edu.flysixbackend.model.Booking;
import com.edu.flysixbackend.repository.BookingRepository;
import com.edu.flysixbackend.repository.PaymentRepository;
import com.edu.flysixbackend.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.apiKey}")
    private String apiKey;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public String makePayment(PaymentDto paymentDto) throws StripeException {

        Stripe.apiKey = apiKey;

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
                                                .setUnitAmount((long) (paymentDto.getPrice() * 100))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Total Amount")
                                                                .build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);
        return session.getUrl();

    }

    @Override
    public boolean confirmPayment(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking.getPayment().getStatus().equalsIgnoreCase(Const.PAYMENT_PENDING)) {

            booking.setStatus(Const.BOOKING_CONFIRMED);
            booking.getPayment().setStatus(Const.PAYMENT_COMPLETED);
            booking.getPayment().setDate(new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date()));
            bookingRepository.save(booking);
            log.info("Payment confirmed for booking ID : " + bookingId);

        } else {
            log.info("Payment is already confirmed for booking ID : " + bookingId);
        }

        return true;
    }
}
