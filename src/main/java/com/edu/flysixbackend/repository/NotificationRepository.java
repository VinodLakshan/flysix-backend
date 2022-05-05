package com.edu.flysixbackend.repository;

import com.edu.flysixbackend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
