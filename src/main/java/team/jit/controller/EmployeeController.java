package team.jit.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import team.jit.dto.EmployeeForm;
import team.jit.dto.EmployeeUpdateForm;
import team.jit.entity.Employee;
import team.jit.service.EmployeeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public List<Employee> findAllEmployees() {
        return employeeService.findAllEmployees();
    }
    
    @GetMapping("/paged")
    public Page<Employee> findPagedEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal salaryMin,
            @RequestParam(required = false) BigDecimal salaryMax,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return employeeService.findPagedEmployees(name, salaryMin, salaryMax, pageable);
    }

    @GetMapping("/{id}")
    public Employee findEmployee(@PathVariable Long id) {
        return employeeService.findEmployee(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Employee saveEmployee(@Valid @RequestBody EmployeeForm form) {
        return employeeService.saveEmployee(form);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody EmployeeUpdateForm form) {
        return employeeService.updateEmployee(id, form);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
