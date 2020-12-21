package com.yeremenko.myProject.banks;

import com.yeremenko.myProject.CurrencyHelper;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MonoBank implements CurrencyService {
    public static final String BASE_URL = "https://api.monobank.ua/bank/currency";
    private static final Logger LOGGER = Logger.getLogger(MonoBank.class.getName());
    @Override
    public RateView getRateFor(LocalDate date, String currency) {
        MonobankRate monobankRate;
        int currencyCode = CurrencyHelper.getCurrencyCode(currency);
        LocalDate todayDate = LocalDate.now().atStartOfDay().toLocalDate();
        LocalDate parameterDate = date==null?todayDate:date;
        String errorText = null;
        if (!todayDate.equals(parameterDate)) {
            monobankRate = new MonobankRate(parameterDate, currency, currencyCode);
            errorText = String.format("Bank MonoBank: date %s; currency %s; No rate for date.", parameterDate.toString(), currency);
        } else {
            monobankRate = parseCurrentExchangeRateRestTemplate(currencyCode);
            if (monobankRate != null) {
                monobankRate.setCurrency(currency);
                monobankRate.setCurrencyCode(currencyCode);
                monobankRate.setDate(parameterDate);
            } else {
                monobankRate = new MonobankRate(parameterDate, currency, currencyCode);
                errorText = String.format("Bank MonoBank: date %s; currency %s; No rate for currency.", parameterDate.toString(), currency);
            }
        }
        if (errorText!= null) {
            LOGGER.log(Level.INFO, errorText);
        }
        return RateMapper.from(monobankRate);
    }

    private static MonobankRate parseCurrentExchangeRateRestTemplate(int currencyCode) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            MonobankRate[] monobankRateArray = restTemplate.getForObject(BASE_URL, MonobankRate[].class);
            if (monobankRateArray != null) {
                return Arrays.stream(monobankRateArray).
                        filter(rate -> rate.getCurrencyCodeA()== currencyCode)
                        .findAny().orElse(null);
            }
        } catch (RestClientResponseException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
           return new MonobankRate();
        }
        return null;
    }
}
