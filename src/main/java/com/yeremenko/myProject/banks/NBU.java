package com.yeremenko.myProject.banks;

import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.DateHelper;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.views.RateView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.*;

import static com.yeremenko.myProject.JsonUtils.parseUrl;

@Qualifier("nbu")
@Component
public class NBU implements CurrencyService {

    public static final String BASE_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";

    @Override
    public RateView getRateFor(Date date, String currency) {
        NBURate nbuRate = parseCurrentExchangeRateJson(date, currency);
        return RateMapper.from(nbuRate);
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


    private NBURate parseCurrentExchangeRateJson(Date date, String currency) {
        String dateStrUrl = DateHelper.convertDateToString(date, "yyyyMMdd");
        String dateStr = DateHelper.convertDateToString(date, "dd.MM.yyyy");
        String errorText;
        String fullUrl = String.format("%s?date=%s&json", BASE_URL, dateStrUrl);

        String resultJson = parseUrl(fullUrl);
        JSONArray joList = new JSONArray(resultJson);
        if (!joList.isEmpty()) {
            for (int i = 0; i < joList.length(); i++) {
                JSONObject objects = joList.getJSONObject(i);
                if (objects.get("cc").equals(currency)) {
                    BigDecimal deci = (BigDecimal) objects.get("rate");
                    double rate = deci.doubleValue();
                    return new NBURate(objects.get("exchangedate").toString(),
                            rate,
                            objects.get("cc").toString(),
                            (Integer) objects.get("r030"));
                }
            }
            errorText = "No rate for currency " + currency;
        } else {
            errorText = "No rate for date " + dateStr;
        }
        return new NBURate(dateStr, currency, errorText);
    }
}
