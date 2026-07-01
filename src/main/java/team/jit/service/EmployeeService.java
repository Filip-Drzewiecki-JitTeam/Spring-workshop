package team.jit.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.jit.dto.EmployeeForm;
import team.jit.dto.EmployeeUpdateForm;
import team.jit.entity.Company;
import team.jit.entity.Employee;
import team.jit.repository.CompanyRepository;
import team.jit.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import team.jit.repository.InterfaceImpl;
import team.jit.repository.MyCustomInterface;
import team.jit.repository.MyDifferentImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeService {

    public final EmployeeRepository employeeRepository;
    public final CompanyRepository companyRepository;
    public final EmailService emailService;

    public List<Employee> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        Set<Employee> employeesSet = new HashSet<Employee>(employees);
        employeesSet.add(employees.get(0));

        Map<Long, Employee> employeesMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, e -> e));

        log.info("fetching employees");
        return employees;
    }

    public Page<Employee> findPagedEmployees(String name,
                                              BigDecimal salaryMin,
                                              BigDecimal salaryMax,
                                              Pageable pageable) {
        return employeeRepository.findByFilters(name, salaryMin, salaryMax, pageable);
    }

    public Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id=" + id + " doesn't exist"));
    }

    @Transactional
    public Employee saveEmployee(EmployeeForm form) {
        Employee employee = Employee.of(form);
        var saved = employeeRepository.save(employee);
        emailService.sendEmail(employee);
        return saved;
    }

    @Transactional
    public Employee updateEmployee(Long id, EmployeeUpdateForm updateForm) {
        Employee employee = findEmployee(id);
        Company company = companyRepository.getOne(updateForm.getCompanyId());
        employee.setSalary(updateForm.getSalary());
        employee.setAddress(updateForm.getAddress());
        employee.setPosition(updateForm.getPosition());
        employee.setCompany(company);
        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
