package team.jit.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Employee {

    @Id
    private Long id;

    private String name;

    private BigDecimal salary;

    private BigDecimal annualIncome;

    @Enumerated(EnumType.STRING)
    private Position position;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
