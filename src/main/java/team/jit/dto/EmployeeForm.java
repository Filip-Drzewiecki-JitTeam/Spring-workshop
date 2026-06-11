package team.jit.dto;

import lombok.Data;
import team.jit.entity.Address;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class EmployeeForm {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String surname;

    @NotBlank
    private String personalId;

    private Address address;

    @Min(1)
    private BigDecimal salary;
}
