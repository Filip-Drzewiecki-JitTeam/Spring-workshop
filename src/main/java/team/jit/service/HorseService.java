package team.jit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.jit.entity.Horse;
import team.jit.repository.HorseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorseService {

    private final HorseRepository horseRepository;

    public List<Horse> findAllHorses() {
        List<Horse> horses = horseRepository.findAll();
        return horses;
    }
}
