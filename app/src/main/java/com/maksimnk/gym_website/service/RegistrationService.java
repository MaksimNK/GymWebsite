package com.maksimnk.gym_website.service;

import com.maksimnk.gym_website.model.User;
import com.maksimnk.gym_website.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RegistrationService {

    private final UserRepositories userRepositories;

    @Autowired
    public RegistrationService(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }

    @Transactional
    public void register(User user) {
        user.setRole("user");
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepositories.save(user);
    }

    @Transactional
    public void registerAdmin(User user) {
        user.setRole("admin");
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepositories.save(user);
    }
}
