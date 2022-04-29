package team.jit.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.jit.entity.Employee;
import team.jit.repository.EmployeeRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CsvService {

    private final EmployeeRepository employeeRepository;
    private final CsvMapper csvMapper;
    private final CsvSchema csvSchema;

    public CsvService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        this.csvMapper = csvMapper;
        this.csvSchema = CsvSchema.builder()
                .setUseHeader(true)
                .addColumn("id")
                .addColumn("name")
                .addColumn("surname")
                .setColumnSeparator(',')
                .setLineSeparator("\r\n")
                .build();
    }

    public byte[] exportFile() {
            List<Employee> employees = employeeRepository.findAll();
            return exportEntity(employees);
    }

    public void importFile(MultipartFile file) {
        try {
            MappingIterator<Employee> iterator = csvMapper.readerFor(Employee.class)
                    .with(csvSchema)
                    .readValues(file.getInputStream());
            List<Employee> employees = iterator.readAll();
            log.info("Parsec CSV into:{}", employees);
        } catch (IOException e) {
            log.error("Error thrown while parsing CSV file");
        }
    }

    public <T> byte[] exportEntity(List<Employee> entities) {
        ObjectWriter writer = csvMapper.writer(csvSchema);
        List<List<Object>> values = mapValues(entities);
        try {
            return writer.writeValueAsBytes(values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<List<Object>> mapValues(List<Employee> entities) {
        return entities.stream()
                .map(this::mapValues)
                .collect(Collectors.toList());
    }

    private List<Object> mapValues(Employee e) {
        return Arrays.asList(e.getId(), e.getName(), e.getSurname());
    }
}

