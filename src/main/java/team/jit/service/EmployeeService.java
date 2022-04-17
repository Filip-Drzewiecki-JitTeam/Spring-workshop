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

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeService {

    public final EmployeeRepository employeeRepository;
    public final CompanyRepository companyRepository;

    public List<Employee> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        log.info("fetching employees");
        return employees;
    }

    public Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id=" + id + " doesn't exist"));
    }

    @Transactional
    public Employee saveEmployee(EmployeeForm form) {
        Employee employee = Employee.of(form);
        return employeeRepository.save(employee);
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
