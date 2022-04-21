package team.jit.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.jit.entity.Company;
import team.jit.entity.Employee;
import team.jit.repository.CompanyRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Employee> findEmployeesOfCompany(Long id) {
        Company company = findCompany(id);
        log.info("Fetching employees for company {}", company.getName());
        return company.getEmployees();
    }
}
