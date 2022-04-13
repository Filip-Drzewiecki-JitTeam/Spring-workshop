package team.jit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.jit.entity.Horse;
import team.jit.service.HorseService;

import java.util.List;

@RestController
@RequestMapping("/horses")
public class HorseController {

    private final HorseService horseService;

    public HorseController(HorseService horseService) {
        this.horseService = horseService;
    }

    @GetMapping
    public ResponseEntity<List<Horse>> findAllHorses() {
        List<Horse> horses = horseService.findAllHorses();
        return new ResponseEntity<>(horses, HttpStatus.OK);
    }
}
