package com.yeremenko.myProject.banks;

import com.yeremenko.myProject.CurrencyHelper;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
@Qualifier("mono")
@Component
public class MonoBank implements CurrencyService {
    public static final String BASE_URL = "https://api.monobank.ua/bank/currency";


    @Override
    public RateView getRateFor(Date date, String currency) {

        MonobankRate monobankRate = parseCurrentExchangeRateRestTemplate(currency);
        if (monobankRate != null) {
            monobankRate.setCurrency(currency);
            monobankRate.setCurrencyCode(CurrencyHelper.getCurrencyCode(currency));
        } else {
            monobankRate = new MonobankRate();
            monobankRate.setCurrency(currency);
            monobankRate.errorText = "No rate for currency " + currency;
        }
        return RateMapper.from(monobankRate);
    }

    @Override
    public RateView[] getRateArrayFor(Date dateFrom, Date dateTo, String currency) {
        return new RateView[0];
    }

    @Override
    public RateView getBestRate(Date dateFrom, Date dateTo, String currency) {
        return null;
    }

    //rest template
    private static MonobankRate parseCurrentExchangeRateRestTemplate(String currency) {
        RestTemplate restTemplate = new RestTemplate();
        int currencyCode = 0;
        if (currency != null) {
            currencyCode = CurrencyHelper.getCurrencyCode(currency);
        }
        try {
            MonobankRate[] monobankRateArray = restTemplate.getForObject(BASE_URL, MonobankRate[].class);

            if (monobankRateArray != null) {

                int finalCurrencyCode = currencyCode;
                return Arrays.stream(monobankRateArray).
                        filter(rate -> rate.getCurrencyCodeA()==finalCurrencyCode)
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
