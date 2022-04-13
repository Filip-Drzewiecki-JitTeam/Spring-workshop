package team.jit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.jit.entity.Horse;
import team.jit.repository.HorseRepository;

import java.util.List;

@Service
public class HorseService {

    private final HorseRepository horseRepository;

    public HorseService(HorseRepository horseRepository) {
        this.horseRepository = horseRepository;
    }

    public List<Horse> findAllHorses() {
        List<Horse> horses = horseRepository.findAll();
        return horses;
    }
}
