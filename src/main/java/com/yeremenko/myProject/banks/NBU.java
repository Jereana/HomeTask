package com.yeremenko.myProject.banks;

import com.yeremenko.myProject.CurrencyHelper;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.views.RateView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    public RateView getBestRate(Date dateFrom, Date dateTo, String currency) {
        //List<RateView> rates = new ArrayList<>();
        return null;
    }

    private NBURate parseCurrentExchangeRateJson(Date date, String currency) {
        String dateStr = convertDate(date);
        //по умолчанию выводим для $ , но можно считывать это значение с файла проперти?!
        if (currency == null) {
            currency = "USD";
        }
        int currencyCode = CurrencyHelper.getCurrencyCode(currency);

        String fullUrl = String.format("%s?date=%s&json", BASE_URL, dateStr);

        String resultJson = parseUrl(fullUrl);
        JSONArray joList = new JSONArray(resultJson);

            /*----------------------------
            //по умолчанию выводим все курсы:
            List<Map<String, Object>> ratesData = new ArrayList<Map<String, Object>>();
            Map<String, Object> jsonItem = new HashMap<String, Object>();
            */

                for (int i = 0; i < joList.length(); i++) {
                    JSONObject objects = joList.getJSONObject(i);
                    if (objects.get("r030").equals(currencyCode)) {
                        BigDecimal deci = (BigDecimal) objects.get("rate");
                        double rate = deci.doubleValue();
                        return new NBURate(objects.get("exchangedate").toString(),
                                rate,
                                objects.get("cc").toString(),
                                (Integer) objects.get("r030"));
                    }
                }

        NBURate nbuRate= new NBURate(dateStr, currency,"No rate for currency " + currency);
        nbuRate.setCurrency(currency);
        return nbuRate;
    }

   public static String  convertDate(Date date) {
        LocalDate localDate;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (date == null) {
            localDate = LocalDate.now();
        } else {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return dtf.format(localDate);
    }
}
