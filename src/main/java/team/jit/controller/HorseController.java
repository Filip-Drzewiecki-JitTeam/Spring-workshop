package team.jit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.jit.dto.HorseDto;
import team.jit.entity.Horse;
import team.jit.service.HorseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/horses")
public class HorseController {

    private final ObjectMapper objectMapper;
    private final HorseService horseService;

    @GetMapping()
    public List<HorseDto> findAllHorses() throws JsonProcessingException {
        //String json = objectMapper.writeValueAsString(horseService.findAllHorses());
        return horseService.findAllHorses().stream()
                .map(HorseDto::of)
                .toList();
    }

    @GetMapping("/forbidden")
    public void findHorses(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Making {} request on {}", request.getMethod(), request.getServletPath());
        response.setStatus(403);
    }
}
