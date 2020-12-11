package com.yeremenko.myProject.banks;

import com.yeremenko.myProject.CurrencyHelper;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.DateHelper;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.yeremenko.myProject.DateHelper.stringToDate;

@Qualifier("mono")
@Component
public class MonoBank implements CurrencyService {
    public static final String BASE_URL = "https://api.monobank.ua/bank/currency";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    @Override
    public RateView getRateFor(Date date, String currency) {
        MonobankRate monobankRate;
        int currencyCode = CurrencyHelper.getCurrencyCode(currency);
        String dateStr = DateHelper.convertDateToString(date, DATE_FORMAT);
        Date parameterDate;
        Date todayDate;
        parameterDate = stringToDate(dateStr);
        todayDate = stringToDate(DateHelper.convertDateToString(null, DATE_FORMAT));

        if (!todayDate.equals(parameterDate)) {
            monobankRate = new MonobankRate(dateStr, currency, currencyCode, "No rate for date " + dateStr);
        } else {
            monobankRate = parseCurrentExchangeRateRestTemplate(currencyCode);
            if (monobankRate != null) {
                monobankRate.setCurrency(currency);
                monobankRate.setCurrencyCode(currencyCode);
                monobankRate.setDate(dateStr);
            } else
                monobankRate = new MonobankRate(dateStr, currency, currencyCode, "No rate for currency " + currency);
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
            e.printStackTrace();
            MonobankRate mbRate = new MonobankRate();
            mbRate.setErrorText(e.getMessage());
            return mbRate;
        }
        return null;
    }
}
