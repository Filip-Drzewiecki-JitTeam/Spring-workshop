package team.jit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.jit.client.NbpClient;
import team.jit.dto.NbpRate;
import team.jit.dto.NbpRates;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final NbpClient nbpClient;

    public NbpRate getCurrency(String currency) {
        return nbpClient.getCurrency(currency);
    }

    public List<NbpRates> getCurrencies() {
        return nbpClient.getCurrencies();
    }
}
