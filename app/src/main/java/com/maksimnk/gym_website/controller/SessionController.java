package com.maksimnk.gym_website.controller;

import com.maksimnk.gym_website.model.Session;
import com.maksimnk.gym_website.model.Trainer;
import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.security.UserDetailsImpl;
import com.maksimnk.gym_website.service.SessionService;
import com.maksimnk.gym_website.service.TrainerService;
import com.maksimnk.gym_website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    private final TrainerService trainerService;
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public SessionController(TrainerService trainerService, SessionService sessionService, UserService userService) {
        this.trainerService = trainerService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreateSessionForm(Model model) {
        // Получите список тренеров и передайте его в модель
        List<Trainer> trainers = trainerService.getAllTrainers();
        model.addAttribute("trainers", trainers);

        // Передайте пустой объект сессии для заполнения формы
        model.addAttribute("session", new Session());

        return "createSession";
    }

    @PostMapping("/create")
    public String createSession(@ModelAttribute("session") Session session, Model model, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();


        Optional<User> userOptional = userService.getUserByUsername(username);

        if (userOptional.isEmpty()) {
            model.addAttribute("error", "User not found.");
            List<Trainer> trainers = trainerService.getAllTrainers();
            model.addAttribute("trainers", trainers);
            return "createSession";
        }

        Long userId = userOptional.get().getId();

        // Check if userId is null or not
        if (userId == null) {
            model.addAttribute("error", "User ID cannot be null.");
            List<Trainer> trainers = trainerService.getAllTrainers();
            model.addAttribute("trainers", trainers);
            return "createSession";
        }
        User user = userOptional.orElse(null);

        boolean hasValidSubscription = userService.hasValidSubscription(userId, session.getDateTime());

        if (!hasValidSubscription) {
            model.addAttribute("error", "You need a valid subscription to book a session on this date.");
            List<Trainer> trainers = trainerService.getAllTrainers();
            model.addAttribute("trainers", trainers);
            return "createSession";
        }

        Long trainerId = session.getTrainer().getId();
        Optional<Trainer> trainerOptional = trainerService.getTrainerById(trainerId);
        Trainer trainer = trainerOptional.orElse(null);


        if (trainerId == null) {
            model.addAttribute("error", "Trainer ID cannot be null.");
            List<Trainer> trainers = trainerService.getAllTrainers();
            model.addAttribute("trainers", trainers);
            return "createSession";
        }

        if (trainerOptional.isEmpty()) {
            model.addAttribute("error", "Trainer not found.");
            List<Trainer> trainers = trainerService.getAllTrainers();
            model.addAttribute("trainers", trainers);
            return "createSession";
        }


        session.setTrainer(trainer);
        session.setDescription(trainer.getSpecialization());
        //session.setUserId(userId);
        session.setUser(user);
        sessionService.saveSession(session);

        return "redirect:/sessions";
    }

    @GetMapping("")
    public String showMySessions(Model model, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<User> userOptional = userService.getUserByUsername(username);

        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();
            List<Session> mySessions = sessionService.getSessionsByUserId(userId);
            model.addAttribute("mySessions", mySessions);
        }

        return "sessions";
    }

}
