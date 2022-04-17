package team.jit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.jit.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
