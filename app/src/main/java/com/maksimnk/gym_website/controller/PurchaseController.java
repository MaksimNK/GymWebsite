package com.maksimnk.gym_website.controller;

import com.maksimnk.gym_website.model.Purchase;
import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.security.UserDetailsImpl;
import com.maksimnk.gym_website.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public String purchasePage(Model model) {
        List<Purchase> allPurchases = purchaseService.getAllPurchases();
        model.addAttribute("allPurchases", allPurchases);
        return "purchase";
    }

    @PostMapping("/subscribe")
    public String purchaseSubscription(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String subscriptionType, Model model) {
        User user = userDetails.getUser(); // Получаем пользователя из UserDetailsImpl
        try {
            purchaseService.purchaseSubscription(user, subscriptionType);
        } catch (IllegalStateException e) {
            // Обработка ошибки: пользователь уже имеет активный абонемент
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", e.getMessage());
            return "purchase";
        }
        return "redirect:/purchase";
    }
    @GetMapping("/history/{userId}")
    public String purchaseHistory(@PathVariable Long userId, Model model) {
        List<Purchase> purchaseHistory = purchaseService.getPurchaseHistory(userId);
        model.addAttribute("purchaseHistory", purchaseHistory);
        return "purchaseHistory";
    }
}
