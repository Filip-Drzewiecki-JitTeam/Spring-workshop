package team.jit.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class NbpRate {

    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    @Data
    private static class Rate {
        private String no;
        private LocalDate effectiveDate;
        private BigDecimal mid;
    }
}
