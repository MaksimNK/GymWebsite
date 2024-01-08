package com.maksimnk.gym_website.service;

import com.maksimnk.gym_website.model.Subscription;
import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.repositories.PurchaseRepository;
import com.maksimnk.gym_website.repositories.SessionRepository;
import com.maksimnk.gym_website.repositories.SubscriptionRepository;
import com.maksimnk.gym_website.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepositories userRepositories;

    private final SubscriptionRepository subscriptionRepository;

    private final SessionRepository sessionRepository;

    private final PurchaseRepository purchaseRepository;
    @Autowired
    public UserService(UserRepositories userRepositories, SubscriptionRepository subscriptionRepository, SessionRepository sessionRepository, PurchaseRepository purchaseRepository) {
        this.userRepositories = userRepositories;
        this.subscriptionRepository = subscriptionRepository;
        this.sessionRepository = sessionRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public User createUser (User user) {
        return userRepositories.save(user);
    }

    public void save(User user) {
        // Хэшируем пароль перед сохранением в базу данных
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepositories.save(user);
    }

    public User getUser(Long id) {
        return userRepositories.findById(id).orElse(null);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepositories.findByUsername(username);
    }
    public List<User> getAllUser() {
        return userRepositories.findAll();
    }

    public User updateUser (User user) {
        return userRepositories.save(user);
    }

    @Transactional
    public void deleteUser (Long id) {
        User user = userRepositories.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        sessionRepository.deleteByUser(user);

        // Удаляем все связанные записи в таблице purchase
        List<Subscription> subscriptions = user.getSubscriptions();
        for (Subscription subscription : subscriptions) {
            purchaseRepository.deleteBySubscription(subscription);
        }

        // Затем удаляем все записи в таблице subscription
        subscriptionRepository.deleteByUser(user);

        // Затем удаляем пользователя
        userRepositories.deleteById(id);

    }

    public boolean hasValidSubscription(Long userId, Date currentDate) {
        Optional<User> optionalUser = getUserById(userId);

        // Проверяем, что пользователь существует и у него есть абонементы
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Subscription> subscriptions = user.getSubscriptions();

            // Проверяем абонементы пользователя
            for (Subscription subscription : subscriptions) {
                Date startDate = subscription.getStartDate();
                Date endDate = subscription.getEndDate();

                // Проверяем, что текущая дата входит в период действия абонемента
                if (startDate != null && endDate != null && currentDate.after(startDate) && currentDate.before(endDate)) {
                    return true; // У пользователя есть действующий абонемент
                }
            }
        }

        return false;
    }

    private Optional<User> getUserById(Long userId) {
        return userRepositories.findById(userId);
    }


}
