package com.yeremenko.myProject.banks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.CurrencyHelper;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.JsonUtils;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Qualifier("pb")
@Component
public class PrivateBank implements CurrencyService {

    public static final String BASE_URL = "https://api.privatbank.ua/p24api/exchange_rates";

    @Override
    public RateView getRateFor(Date date, String currency) {
        String dateStr = CurrencyHelper.convertDateToString(date,"dd.MM.yyyy");

        try {
            String url = String.format("%s?json&date=%s&currency=%s",BASE_URL, dateStr,currency);
            HttpResponse<String> response = Unirest.get(url)
                        .queryString("date", dateStr).asString();
            PBRate pbRate = JsonUtils.parseJsonWithJackson(response.getBody(), PBRate.class);

            //Проверка: если на дату нет вообще никаких курсов, например в 00:00+
            if (pbRate != null) {
                if (currency == null) {
                    currency = "USD";
                }
                pbRate.setCurrency(currency);
                int currencyCode = CurrencyHelper.getCurrencyCode(currency);
                pbRate.setCurrencyCode(currencyCode);
                if(currencyCode==0){
                    pbRate.setErrorText("No rate for currency " + currency + ". ");  // если указана некорректная валюта
                }
            } else {
                pbRate = new PBRate();
                pbRate.setCurrency(currency);
                pbRate.setDate(dateStr);
                pbRate.setErrorText("No rate for date " + dateStr + ". ");
            }
            return RateMapper.from(pbRate);
            } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public RateView getBestRate(Date dateFrom, Date dateTo, String currency) {
        return null;
    }

    /*
    public static String convertDateToString(Date date) {
        LocalDate localDate;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (date == null) {
            localDate = LocalDate.now();
        } else {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return dtf.format(localDate);
    }*/
}
