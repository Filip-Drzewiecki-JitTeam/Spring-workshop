package team.jit.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.jit.entity.Company;
import team.jit.entity.Employee;
import team.jit.service.CompanyService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OPERATOR', 'ADMIN')")
    @GetMapping
    public List<Company> findAllCompanies() {
        return companyService.findAllCompanies();
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OPERATOR', 'ADMIN')")
    @GetMapping("/{id}")
    public Company findCompany(@PathVariable Long id) {
        return companyService.findCompany(id);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'OPERATOR', 'ADMIN')")
    @GetMapping("/{id}/employees")
    public List<Employee> findEmployeesOfCompany(@PathVariable Long id) {
        return companyService.findEmployeesOfCompany(id);
    }
}


