package com.maksimnk.gym_website.controller;

import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.security.UserDetailsImpl;
import com.maksimnk.gym_website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String homePage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();

        Optional<User> userOptional = userService.getUserByUsername(username);
        boolean hasSubscriptions = userOptional.map(this::checkUserSubscriptions).orElse(false);

        model.addAttribute("username", username);
        model.addAttribute("hasSubscriptions", hasSubscriptions);

        return "home";
    }


    private boolean checkUserSubscriptions(User user) {

        return user.getSubscriptions() != null && !user.getSubscriptions().isEmpty();
    }
}
