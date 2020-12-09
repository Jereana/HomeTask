package com.yeremenko.myProject.banks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.*;
import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            HttpResponse<String> response = null;

                response = Unirest.get(url)
                        .queryString("date", dateStr).asString();

            pbRate = JsonUtils.parseJsonWithJackson(response.getBody(), PBRate.class);

            //Проверка: если на дату нет вообще никаких курсов, например в 00:00+
            //if (pbRate != null) {
            if(pbRate.exchangeRate.size() == 0)  {
                pbRate.setErrorText("No rate for date " + dateStr + ". ");
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        pbRate.setDate(dateStr);
        pbRate.setCurrency(currency);
        pbRate.setCurrencyCode(currencyCode);

        return RateMapper.from(pbRate);
    }

    @Override
    public List<RateView> getBestRate(Date dateFrom, Date dateTo, String currency, int lastDaysCount) {

        List<RateView> ratesList = new ArrayList<>();
        List<Date> datesList = DateHelper.getDatesList(lastDaysCount, dateFrom, dateTo);
        List<RateView> minRatesList = new ArrayList<>();

        for (Date date : datesList) {
            RateView rateView = getRateFor(date, currency);
            ratesList.add(rateView);
        }

        double minSaleRate = 1000;
        System.out.println("All list of PrivateBank rates:");
        for (RateView rate : ratesList) {

            System.out.println("Bank: " + rate.getBank() +
                    "; Date: " + rate.getDate() +
                    "; Currency: " + rate.getCurrency() +
                    "; Rate: " + rate.getSaleRate());

            if (minSaleRate == rate.getSaleRate()) {
                minRatesList.add(rate);
                minSaleRate = rate.getSaleRate();
            } else if (minSaleRate > rate.getSaleRate()) {
                minRatesList.clear();
                minRatesList.add(rate);
                minSaleRate = rate.getSaleRate();
            }

        }
        System.out.println("_______________________");

        return minRatesList;
    }




}
