package team.jit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.jit.entity.Employee;
import team.jit.repository.EmployeeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {

    public final EmployeeRepository employeeRepository;

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }
}
