package com.maksimnk.gym_website.controller;

import com.maksimnk.gym_website.model.Subscription;
import com.maksimnk.gym_website.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
    @PostMapping
    public Subscription createSubscription (@RequestBody Subscription subscription) {
        return subscriptionService.createSubscription(subscription);
    }

    @GetMapping("/{id}")
    public Subscription getSubscription(@PathVariable Long id) {
        return subscriptionService.getSubscription(id);
    }

    @GetMapping
    public List<Subscription> getAllSubscription(){
        return subscriptionService.getAllSubscription();
    }

    @PostMapping("/update")
    public Subscription upadateSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.updateSubscription(subscription);
    }

    @DeleteMapping
    public void deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteUserSubscription(id);
    }


}
