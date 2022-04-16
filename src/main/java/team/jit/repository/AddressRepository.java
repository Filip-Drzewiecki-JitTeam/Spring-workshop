package team.jit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.jit.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
