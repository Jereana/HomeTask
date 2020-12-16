package com.yeremenko.myProject.banks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.*;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PrivateBank implements CurrencyService {
    private static final Logger LOGGER = Logger.getLogger(PrivateBank.class.getName());
    public static final String BASE_URL = "https://api.privatbank.ua/p24api/exchange_rates";

    @Override
    public RateView getRateFor(LocalDate date, String currency) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateStr = date.format(dtf);
        String errorText;
        int currencyCode = CurrencyHelper.getCurrencyCode(currency);
        PBRate pbRate = new PBRate();
        try {
            String url = String.format("%s?json&date=%s", BASE_URL, dateStr);
            HttpResponse<String> response = Unirest.get(url).asString();

            pbRate = JsonUtils.parseJsonWithJackson(response.getBody(), PBRate.class);

            if(pbRate != null && pbRate.exchangeRate.isEmpty())  {
                errorText = "Bank PrivateBank: date " + date.toString() + "; currency " +
                        currency + "; No rate for date.";
                LOGGER.log(Level.INFO, errorText);
            }
        } catch (UnirestException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        if (pbRate!=null) {
            pbRate.setDate(date.toString());
            pbRate.setCurrency(currency);
            pbRate.setCurrencyCode(currencyCode);
        }
        return RateMapper.from(pbRate);

    }
}
