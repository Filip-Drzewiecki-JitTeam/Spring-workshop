package team.jit.listener;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.jit.entity.Horse;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class ApplicationListener {

    private final EntityManager em;

    public ApplicationListener(EntityManager em) {
        this.em = em;
    }

    @Transactional
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Horse horse = new Horse(1L, "John");
        em.persist(horse);
    }

    public <T> List<T> loadObjectList(Class<T> type) {
        String fileName = type.getSimpleName() + ".csv";
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');
            CsvMapper mapper = new CsvMapper();
            File file = new ClassPathResource(fileName).getFile();
            MappingIterator<T> readValues =
                    mapper.readerFor(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }
}

