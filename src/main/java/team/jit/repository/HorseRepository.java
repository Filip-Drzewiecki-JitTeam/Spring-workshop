package team.jit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.jit.entity.Horse;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long> {
}
