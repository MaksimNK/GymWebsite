package com.maksimnk.gym_website.util;

import com.maksimnk.gym_website.model.Trainer;
import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TrainerValidator implements Validator {

    private final TrainerService trainerService;

    @Autowired
    public TrainerValidator(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Trainer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Trainer trainer = (Trainer) target;
        try {
            trainerService.loadUserByName(trainer.getName());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("name" ,"" , "Человек с таким именем уже существует");

    }
}
