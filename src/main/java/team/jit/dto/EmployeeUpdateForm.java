package team.jit.dto;

import lombok.Data;
import team.jit.entity.Address;
import team.jit.entity.Position;

import java.math.BigDecimal;

@Data
public class EmployeeUpdateForm {

    private String name;
    private BigDecimal salary;
    private Address address;
    private Long companyId;
    private Position position;
}
