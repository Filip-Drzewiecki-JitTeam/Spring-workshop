package team.jit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class Company {

    @Id
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private List<Employee> employees;
}
