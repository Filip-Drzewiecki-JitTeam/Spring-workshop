package team.jit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.jit.entity.Employee;

import java.math.BigDecimal;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Filtered + paged query. Every filter parameter is optional — passing null skips that condition.
     *
     * JPQL tricks used here:
     *  - (:param IS NULL OR ...)  — the IS NULL check short-circuits the condition when no value is provided,
     *                               so the filter is simply ignored. This is the standard JPQL pattern
     *                               for optional/nullable query parameters.
     *  - LOWER(...) LIKE LOWER(CONCAT('%', :name, '%'))  — case-insensitive contains search on both
     *                                                       name and surname columns.
     *
     * Example calls:
     *   ?name=john                        → matches "John", "JOHNNY", "ola johnson"
     *   ?salaryMin=3000&salaryMax=7000    → salary between 3000 and 7000
     *   ?name=anna&salaryMin=5000         → name contains "anna" AND salary >= 5000
     *   (no params)                       → returns all employees (paged)
     */
    @Query("""
            SELECT e FROM Employee e
            WHERE (:name IS NULL
                       OR LOWER(e.name)    LIKE LOWER(CONCAT('%', :name, '%'))
                       OR LOWER(e.surname) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:salaryMin IS NULL OR e.salary >= :salaryMin)
              AND (:salaryMax IS NULL OR e.salary <= :salaryMax)
            """)
    Page<Employee> findByFilters(@Param("name")      String name,
                                 @Param("salaryMin") BigDecimal salaryMin,
                                 @Param("salaryMax") BigDecimal salaryMax,
                                 Pageable pageable);
}


