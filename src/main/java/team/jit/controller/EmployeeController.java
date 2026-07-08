package team.jit.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.jit.dto.EmployeeForm;
import team.jit.dto.EmployeeUpdateForm;
import team.jit.entity.Employee;
import team.jit.service.EmployeeService;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OPERATOR', 'ADMIN')")
    @GetMapping
    public List<Employee> findAllEmployees() {
        return employeeService.findAllEmployees();
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OPERATOR', 'ADMIN')")
    @GetMapping("/paged")
    public Page<Employee> findPagedEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal salaryMin,
            @RequestParam(required = false) BigDecimal salaryMax,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return employeeService.findPagedEmployees(name, salaryMin, salaryMax, pageable);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OPERATOR', 'ADMIN')")
    @GetMapping("/{id}")
    public Employee findEmployee(@PathVariable Long id) {
        return employeeService.findEmployee(id);
    }

    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Employee saveEmployee(@Valid @RequestBody EmployeeForm form) {
        return employeeService.saveEmployee(form);
    }

    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody EmployeeUpdateForm form) {
        return employeeService.updateEmployee(id, form);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}


