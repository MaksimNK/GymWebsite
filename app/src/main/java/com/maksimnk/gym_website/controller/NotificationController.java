package com.maksimnk.gym_website.controller;

import com.maksimnk.gym_website.model.Notification;
import com.maksimnk.gym_website.security.UserDetailsImpl;
import com.maksimnk.gym_website.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String showNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        String username = userDetails.getUsername();
        List<Notification> userNotifications = notificationService.getNotificationsByUsername(username);
        model.addAttribute("userNotifications", userNotifications);
        return "userNotifications";
    }

}
