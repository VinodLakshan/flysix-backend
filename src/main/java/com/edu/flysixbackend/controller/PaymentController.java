package com.edu.flysixbackend.controller;

import com.edu.flysixbackend.dto.PaymentDto;
import com.edu.flysixbackend.service.PaymentService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping()
    public ResponseEntity<?> makePayment(@RequestBody PaymentDto payment){
        log.info("Payment is in progress...");
        JSONObject paymentObj = new JSONObject();
        paymentObj.put("paymentUrl", paymentService.makePayment(payment));
        return ResponseEntity.ok(paymentObj);
    }
}
