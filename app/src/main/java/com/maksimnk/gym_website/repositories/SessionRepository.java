package com.maksimnk.gym_website.repositories;

import com.maksimnk.gym_website.model.Session;
import com.maksimnk.gym_website.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> getSessionsByUser_Id(Long userId);

    void deleteByUser(User user);

}
