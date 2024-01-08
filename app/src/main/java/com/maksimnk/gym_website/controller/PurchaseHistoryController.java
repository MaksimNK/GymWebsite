package com.maksimnk.gym_website.controller;

import com.maksimnk.gym_website.model.Purchase;
import com.maksimnk.gym_website.security.UserDetailsImpl;
import com.maksimnk.gym_website.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PurchaseHistoryController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseHistoryController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/purchase/history")
    public String purchaseHistory(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Long userId = userDetails.getUser().getId();
        List<Purchase> purchaseHistory = purchaseService.getPurchaseHistory(userId);
        model.addAttribute("purchaseHistory", purchaseHistory);
        return "purchaseHistory";
    }
}
