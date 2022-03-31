package com.edu.flysixbackend.controller;

import com.edu.flysixbackend.dto.ErrorDto;
import com.edu.flysixbackend.dto.PaymentDto;
import com.edu.flysixbackend.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping()
    public ResponseEntity<?> makePayment(@RequestBody PaymentDto payment) {
        try {
            log.info(payment.getBookingId() + " Payment is in progress...");
            JSONObject paymentObj = new JSONObject();
            paymentObj.put("paymentUrl", paymentService.makePayment(payment));
            return ResponseEntity.ok(paymentObj);

        } catch (StripeException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/confirm/{bookingId}")
    public ResponseEntity<?> confirmPayment(@PathVariable Long bookingId) {

        try {
            log.info("Confirming payment for bookingId : " + bookingId);
            boolean confirmPayment = paymentService.confirmPayment(bookingId);

            JSONObject paymentObj = new JSONObject();
            paymentObj.put("isPaymentConfirmed", confirmPayment);
            return ResponseEntity.ok(paymentObj);

        } catch (Exception e) {

            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
