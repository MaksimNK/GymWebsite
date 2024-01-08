package com.maksimnk.gym_website.service;

import com.maksimnk.gym_website.model.Session;
import com.maksimnk.gym_website.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void saveSession(Session session) {
        // Дополнительная логика, если необходимо
        sessionRepository.save(session);
    }


    public List<Session> getSessionsByUserId(Long userId) {
        return sessionRepository.getSessionsByUser_Id(userId);
    }
}
