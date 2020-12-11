package com.yeremenko.myProject.banks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.*;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Qualifier("pb")
@Component
public class PrivateBank implements CurrencyService {

    public static final String BASE_URL = "https://api.privatbank.ua/p24api/exchange_rates";

    @Override
    public RateView getRateFor(Date date, String currency) {
        String dateStr = DateHelper.convertDateToString(date,"dd.MM.yyyy");
        int currencyCode = CurrencyHelper.getCurrencyCode(currency);
        PBRate pbRate = new PBRate();
        try {
            String url = String.format("%s?json&date=%s", BASE_URL, dateStr);
            HttpResponse<String> response = Unirest.get(url).asString();

            pbRate = JsonUtils.parseJsonWithJackson(response.getBody(), PBRate.class);

            if(pbRate != null && pbRate.exchangeRate.isEmpty())  {
                pbRate.setErrorText("No rate for date " + dateStr + ". ");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (pbRate!=null) {
            pbRate.setDate(dateStr);
            pbRate.setCurrency(currency);
            pbRate.setCurrencyCode(currencyCode);
        }
        return RateMapper.from(pbRate);
    }
}
