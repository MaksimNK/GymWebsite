package com.maksimnk.gym_website.service;

import com.maksimnk.gym_website.model.Purchase;
import com.maksimnk.gym_website.model.Subscription;
import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.repositories.PurchaseRepository;
import com.maksimnk.gym_website.repositories.SubscriptionRepository;
import com.maksimnk.gym_website.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepositories userRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, SubscriptionRepository subscriptionRepository, UserRepositories userRepository) {
        this.purchaseRepository = purchaseRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    public Purchase purchaseSubscription(User user, String subscriptionType) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Optional<Subscription> activeSubscription = subscriptionRepository.findTop1ByUserIdAndEndDateAfterOrderByEndDateDesc(user.getId(), LocalDate.now());

        if (activeSubscription.isPresent()) {
            throw new IllegalStateException("User already has an active subscription");
            // Вы можете выбрать другое действие, например, обработать этот случай по-другому
        }

        Subscription subscription = getSubscriptionByType(subscriptionType, user);

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setSubscription(subscription);
        purchase.setPurchaseDate(new Date());

        handleSubscriptionPurchase(subscription, purchase);

        return purchaseRepository.save(purchase);
    }

    private Subscription getSubscriptionByType(String subscriptionType, User user) {
        switch (subscriptionType) {
            case SubscriptionConstants.SUBSCRIPTION_BASIC:
                return createSubscription(SubscriptionConstants.SUBSCRIPTION_BASIC, SubscriptionConstants.PRICE_BASIC, SubscriptionConstants.DURATION_BASIC, user);
            case SubscriptionConstants.SUBSCRIPTION_PREMIUM:
                return createSubscription(SubscriptionConstants.SUBSCRIPTION_PREMIUM, SubscriptionConstants.PRICE_PREMIUM, SubscriptionConstants.DURATION_PREMIUM, user);
            default:
                throw new IllegalArgumentException("Invalid subscription type: " + subscriptionType);
        }
    }

    private Subscription createSubscription(String name, double price, int duration, User user) {
        Subscription subscription = new Subscription();
        subscription.setName(name);
        subscription.setStartDate(new Date());
        subscription.setEndDate(calculateEndDate(subscription.getStartDate(), duration));
        subscription.setPrice(price);
        subscription.setUser(user); // Устанавливаем пользователя
        return subscriptionRepository.save(subscription);
    }


    private Date calculateEndDate(Date startDate, int daysToAdd) {
        // Добавьте логику для расчета даты окончания подписки
        // Например, можно воспользоваться классом Calendar или LocalDate
        // В данном примере просто добавим указанное количество дней к дате начала
        long endDateMillis = startDate.getTime() + daysToAdd * 24 * 60 * 60 * 1000L;
        return new Date(endDateMillis);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    private void handleSubscriptionPurchase(Subscription subscription, Purchase purchase) {
        // Дополнительная логика обработки цены, срока действия подписки и т. д.
        // Здесь вы можете добавить любую логику, связанную с покупкой подписки
    }


    public List<Purchase> getPurchaseHistory(Long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase getPurchase(Long purchaseId) {
        return purchaseRepository.findById(purchaseId).orElse(null);
    }
}

final class SubscriptionConstants {
    public static final String SUBSCRIPTION_BASIC = "Basic Subscription";
    public static final double PRICE_BASIC = 19.99;

    public static final String SUBSCRIPTION_PREMIUM = "Premium Subscription";
    public static final double PRICE_PREMIUM = 29.99;

    // Добавим продолжительность подписок в днях
    public static final int DURATION_BASIC = 7;
    public static final int DURATION_PREMIUM = 30;
}
