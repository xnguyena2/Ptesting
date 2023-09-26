package com.example.heroku.model.repository;

import com.example.heroku.model.Notification;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NotificationRepository extends ReactiveCrudRepository<Notification, Long> {
}
