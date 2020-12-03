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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
@Qualifier("mono")
@Component
public class MonoBank implements CurrencyService {
    public static final String BASE_URL = "https://api.monobank.ua/bank/currency";

    @Override
    public RateView getRateFor(Date date, String currency) {
        // добавить условие, что если дата!=текущей дате, то return null
        MonobankRate monobankRate;
        int currencyCode;
        if (currency == null) {
            currency = "USD"; // по умолчанию. Возможно сделать пропертю отдельную для этого
        }
        currencyCode = CurrencyHelper.getCurrencyCode(currency);
        String dateStr = CurrencyHelper.convertDateToString(date,"dd.MM.yyyy");

        //Date currentDate = parseDateToFormat(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        //if (!currentDate.equals(date)){
      //      monobankRate = new MonobankRate(dateStr, currency, currencyCode, "No rate for date" + dateStr);
     //   } else {
            monobankRate = parseCurrentExchangeRateRestTemplate(currencyCode);
            if (monobankRate != null) {
                monobankRate.setCurrency(currency);
                monobankRate.setCurrencyCode(currencyCode);
                monobankRate.setDate(dateStr);
            } else
                monobankRate = new MonobankRate(dateStr, currency, currencyCode, "No rate for currency " + currency);

       // }
        return RateMapper.from(monobankRate);
    }

    @Override
    public RateView getBestRate(Date dateFrom, Date dateTo, String currency) {
        return null;
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

    private static Date parseDateToFormat(Date date){
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
