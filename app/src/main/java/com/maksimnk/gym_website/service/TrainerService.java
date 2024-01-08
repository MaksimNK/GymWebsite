package com.maksimnk.gym_website.service;

import com.maksimnk.gym_website.model.Trainer;
import com.maksimnk.gym_website.repositories.TrainerRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private final TrainerRepositories trainerRepository;

    @Autowired
    public TrainerService(TrainerRepositories trainerRepositories) {
        this.trainerRepository = trainerRepositories;
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Optional<Trainer> getTrainerById(Long id) {
        if (id == null) {
            // Обработка случая, когда идентификатор равен null
            return Optional.empty();
        }
        return trainerRepository.findById(id);

    }

    public Trainer createTrainer(Trainer trainer) {
        // Можно добавить проверки перед сохранением, если необходимо
        return trainerRepository.save(trainer);
    }

    public Trainer updateTrainer(Long id, Trainer updatedTrainer) {
        // Проверяем, существует ли тренер с заданным id
        if (trainerRepository.existsById(id)) {
            updatedTrainer.setId(id);
            return trainerRepository.save(updatedTrainer);
        } else {
            // Можно выбросить исключение или обработать по-другому, в зависимости от требований
            return null;
        }
    }

    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }

    public void registerTrainer(Trainer trainer) {
        trainerRepository.save(trainer);
    }



    public Optional<Trainer> loadUserById(Long id) {
        return trainerRepository.findById(id);
    }

    public Trainer loadUserByName(String name) {
        return trainerRepository.loadUserByName(name);
    }
}
