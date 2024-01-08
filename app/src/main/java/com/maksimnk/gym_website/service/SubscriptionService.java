package com.maksimnk.gym_website.service;


import com.maksimnk.gym_website.model.Subscription;
import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription createSubscription(Subscription subscription) {
        return  subscriptionRepository.save(subscription);
    }

    public Subscription getSubscription(Long id) {
        return subscriptionRepository.findById(id).orElse(null);
    }

    public List<Subscription> getAllSubscription() {
        return subscriptionRepository.findAll();
    }

    public Subscription updateSubscription (Subscription user) {
        return subscriptionRepository.save(user);
    }
    public void deleteUserSubscription (Long id) {
        subscriptionRepository.deleteById(id);
    }



}
