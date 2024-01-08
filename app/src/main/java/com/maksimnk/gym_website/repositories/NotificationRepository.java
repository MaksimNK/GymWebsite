package com.maksimnk.gym_website.repositories;

import com.maksimnk.gym_website.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByDateTimeDesc(Long userIdByUsername);
}
