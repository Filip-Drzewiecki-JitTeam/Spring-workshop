package team.jit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.jit.entity.Company;
import team.jit.repository.CompanyRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }
}
