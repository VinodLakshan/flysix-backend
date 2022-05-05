package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
