package team.jit.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NbpRates {

    private String table;
    private List<Rate> rates;

    @Data
    public static class Rate {
        private String currency;
        private String code;
        private BigDecimal mid;
    }
}
