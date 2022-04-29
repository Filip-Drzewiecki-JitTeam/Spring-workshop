package team.jit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.jit.entity.Horse;
import team.jit.service.HorseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/horses")
public class HorseController {

    @Value("${horses.forbidden.message}")
    private String message;

    private final HorseService horseService;

    public HorseController(HorseService horseService) {
        this.horseService = horseService;
    }

    @GetMapping
    public ResponseEntity<List<Horse>> findAllHorses() {
        List<Horse> horses = horseService.findAllHorses();
        return new ResponseEntity<>(horses, HttpStatus.OK);
    }

    @GetMapping("/forbidden")
    public void findHorses(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Making {} request on {}", request.getMethod(), request.getServletPath());
        response.setStatus(403);
        response.getWriter().write(message);
    }
}
