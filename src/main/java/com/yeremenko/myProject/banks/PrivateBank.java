package com.yeremenko.myProject.banks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.CurrencyHelper;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.JsonUtils;
import com.yeremenko.myProject.RateMapper;
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
        String dateStr = convertDateToString(date);

        try {
            String url = String.format("%s?json&date=%s&currency=%s",BASE_URL, dateStr,currency);
            HttpResponse<String> response = Unirest.get(url)
                        .queryString("date", dateStr).asString();
            PBRate pbRate = JsonUtils.parseJsonWithJackson(response.getBody(), PBRate.class);
            if (currency != null & pbRate!=null) {
                pbRate.setCurrency(currency);
                pbRate.setCurrencyCode(CurrencyHelper.getCurrencyCode(currency));
            }
            return RateMapper.from(pbRate);

            } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public RateView[] getRateArrayFor(Date dateFrom, Date dateTo, String currency) {
        return new RateView[0];
    }

    @Override
    public RateView getBestRate(Date dateFrom, Date dateTo, String currency) {
        return null;
    }
/*
    private static String convertDateToString(Date date) {
        String dateStr;
        if (date == null) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.now();
            dateStr = dtf.format(localDate);
        } else {
            dateStr = date.toString();
        }
        return dateStr;
    }*/

    private static String convertDateToString(Date date) {
        LocalDate localDate;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (date == null) {
            localDate = LocalDate.now();
        } else {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return dtf.format(localDate);
    }
}
