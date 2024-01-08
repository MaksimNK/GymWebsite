package com.maksimnk.gym_website.repositories;

import com.maksimnk.gym_website.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepositories extends JpaRepository<Trainer, Long> {

    @Query("SELECT t FROM Trainer t WHERE t.name = :name")
    Trainer loadUserByName(@Param("name") String name);

}
