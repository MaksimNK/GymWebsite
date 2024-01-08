package com.maksimnk.gym_website.service;

import com.maksimnk.gym_website.model.Notification;
import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.repositories.NotificationRepository;
import com.maksimnk.gym_website.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepositories userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepositories userRepositories) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepositories;
    }

    public void sendNotification(Notification notification) {
        // Реализуйте логику отправки уведомления (например, по электронной почте, через службу мессенджинга и т. д.)
        // После отправки обновите поле 'sent' на true
        notification.setSent(true);
        notificationRepository.save(notification);
    }

    public void sendNotifications(List<Notification> notifications) {
        // Отправка уведомлений массово
        for (Notification notification : notifications) {
            sendNotification(notification);
        }
    }

    public List<Notification> getNotificationsByUsername(String username) {
        return notificationRepository.findByUserIdOrderByDateTimeDesc(getUserIdByUsername(username));
    }

    // Дополнительный метод для получения ID пользователя по его имени
    private Long getUserIdByUsername(String username) {
        // Реализация зависит от вашей логики и структуры БД
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(User::getId).orElse(null);
    }
}
