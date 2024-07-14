package jpa.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.spring.model.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

    
} 