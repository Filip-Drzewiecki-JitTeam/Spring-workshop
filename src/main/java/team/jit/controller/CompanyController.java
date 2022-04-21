package team.jit.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.jit.entity.Company;
import team.jit.entity.Employee;
import team.jit.service.CompanyService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public List<Company> findAllCompanies() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/{id}")
    public Company findCompany(@PathVariable Long id) {
        return companyService.findCompany(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> findEmployeesOfCompany(@PathVariable Long id) {
        return companyService.findEmployeesOfCompany(id);
    }
}
