package team.jit.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.jit.entity.Horse;

import javax.persistence.EntityManager;

@Slf4j
@Component
public class DataLoader implements ApplicationRunner {

    private final EntityManager em;

    @Autowired
    public DataLoader(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        loadHorses();
    }

    public void loadHorses() {
        log.info("Loading horses data");
        Horse horse1 = new Horse(1L, "John");
        Horse horse2 = new Horse(2L, "Jim");
        Horse horse3 = new Horse(3L, "William");

        em.persist(horse1);
        em.persist(horse2);
        em.persist(horse3);
    }
}
