package team.jit.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team.jit.dto.NbpRate;
import team.jit.dto.NbpRates;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NbpClient {

    private final RestTemplate nbpRestTemplate;

    public NbpRate getCurrency(String currency) {
        log.info("getting single currency");
        ResponseEntity<NbpRate> response = nbpRestTemplate.exchange(
                "/rates/A/" + currency,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                NbpRate.class);

        return response.getBody();
    }

    public List<NbpRates> getCurrencies() {
        log.info("getting currencies");
        ResponseEntity<List<NbpRates>> response = nbpRestTemplate.exchange(
                "/tables/A",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<NbpRates>>() {
                });

        List<NbpRates> rates = response.getBody();
        return rates;
    }
}
