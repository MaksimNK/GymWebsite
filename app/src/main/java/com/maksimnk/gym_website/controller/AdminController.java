package com.maksimnk.gym_website.controller;


import com.maksimnk.gym_website.model.Notification;
import com.maksimnk.gym_website.model.Trainer;
import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.security.UserDetailsImpl;
import com.maksimnk.gym_website.service.NotificationService;
import com.maksimnk.gym_website.service.RegistrationService;
import com.maksimnk.gym_website.service.TrainerService;
import com.maksimnk.gym_website.service.UserService;
import com.maksimnk.gym_website.util.TrainerValidator;
import com.maksimnk.gym_website.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    private final UserValidator userValidator;

    private final TrainerValidator trainerValidator;

    private final RegistrationService registrationService;

    private final TrainerService trainerService;

    private final NotificationService notificationService;


    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, TrainerValidator trainerValidator, RegistrationService registrationService, TrainerService trainerService, NotificationService notificationService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.trainerValidator = trainerValidator;
        this.registrationService = registrationService;
        this.trainerService = trainerService;
        this.notificationService = notificationService;
    }


    @GetMapping("/home")
    public String homePage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();

        Optional<User> userOptional = userService.getUserByUsername(username);

        model.addAttribute("username", username);

        return "adminHome";
    }

    @GetMapping("/editUsers")
    public String UsersPage(Model model) {

        List<User> users =  userService.getAllUser();
        model.addAttribute("users", users);

        return "editUsers";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long userId){
        userService.deleteUser(userId);
        return "redirect:/admin/editUsers";
    }

    @GetMapping("/addAdmin")
    public String addAdmin(@ModelAttribute("user") User user) {
        return "addAdmin";
    }
    @PostMapping("/addAdmin")
    public String AddAdminRegistration(@ModelAttribute("user") @Validated User user, BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);
        registrationService.registerAdmin(user);

        if(bindingResult.hasErrors()){
            return "/admin/addAdmin";
        }

        return "redirect:/admin/home";
    }

    @GetMapping("/addTrainer")
    public String addAdmin(@ModelAttribute("trainer") Trainer trainer) {
        return "addTrainer";
    }

    @PostMapping("/addTrainer")
    public String AddATrainerRegistration(@ModelAttribute("trainer") @Validated Trainer trainer, BindingResult bindingResult) {


        trainerService.registerTrainer(trainer);

        return "redirect:/admin/home";
    }

    @GetMapping("/editTrainer")
    public String trainerPage(Model model) {

        List<Trainer> trainers  = trainerService.getAllTrainers();
        model.addAttribute("trainers", trainers);

        return "editTrainers";
    }

    @PostMapping("/deleteTrainer")
    public String deleteTrainer(@RequestParam("trainerId") Long trainerId){
        trainerService.deleteTrainer(trainerId);
        return "redirect:/admin/editTrainer";
    }

    @PostMapping("/sendNotification")
    public String sendNotification(@ModelAttribute("notification") Notification notification) {
        // Create the notification and save it
        notificationService.sendNotification(notification);
        return "redirect:/admin/home";
    }

    @PostMapping("/sendBulkNotifications")
    public String sendBulkNotifications(@ModelAttribute("notifications") List<Notification> notifications) {
        // Create and send notifications in bulk
        notificationService.sendNotifications(notifications);
        return "redirect:/admin/home";
    }

    @GetMapping("/createNotification")
    public String showCreateNotificationPage(Model model) {
        List<User> users = userService.getAllUser();
        model.addAttribute("users", users);
        model.addAttribute("notification", new Notification());
        return "createNotification";
    }



}
