package com.edu.flysixbackend.controller;

import com.edu.flysixbackend.dto.ErrorDto;
import com.edu.flysixbackend.dto.PaymentDto;
import com.edu.flysixbackend.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/paymentSession")
    public ResponseEntity<?> createSession(@RequestBody PaymentDto payment) {
        try {
            log.info("Creating payment session is in progress.");

            JSONObject paymentObj = new JSONObject();
            Session paymentSession = paymentService.createPaymentSession(payment);
            log.info("Payment session is created.");

            paymentObj.put("paymentUrl", paymentSession.getUrl());
            return ResponseEntity.ok(paymentObj);

        } catch (StripeException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/webhook")
    public Object webhook(HttpServletRequest request, HttpServletResponse response, @RequestBody String payload) {
        log.info("executing webhook...");
        return paymentService.webhook(request, response, payload);
    }
}
