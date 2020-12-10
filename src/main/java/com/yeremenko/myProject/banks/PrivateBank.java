package com.yeremenko.myProject.banks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.*;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
            HttpResponse<String> response = Unirest.get(url)
                        .queryString("date", dateStr).asString();

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

        for (RateView rate : ratesList) {
            if (minSaleRate == rate.getSaleRate()) {
                minRatesList.add(rate);
                minSaleRate = rate.getSaleRate();
            } else if (minSaleRate > rate.getSaleRate()) {
                minRatesList.clear();
                minRatesList.add(rate);
                minSaleRate = rate.getSaleRate();
            }
        }
        return minRatesList;
    }
}
