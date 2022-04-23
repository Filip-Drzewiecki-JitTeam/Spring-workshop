package team.jit.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.jit.dto.NbpRate;
import team.jit.dto.NbpRates;
import team.jit.service.CurrencyService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/{currency}")
    public NbpRate getCurrency(@PathVariable String currency) {
        return currencyService.getCurrency(currency);
    }

    @GetMapping
    public List<NbpRates> getCurrencies() {
        return currencyService.getCurrencies();
    }
}
