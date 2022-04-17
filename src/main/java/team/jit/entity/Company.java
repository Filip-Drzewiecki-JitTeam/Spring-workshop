package team.jit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
