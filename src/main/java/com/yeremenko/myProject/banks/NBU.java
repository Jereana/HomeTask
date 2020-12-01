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
import java.text.SimpleDateFormat;
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

        NBURate nbuRate = parseCurrentExchangeRateJson(date, currency);// currency код получить из currencyHelper
        return RateMapper.from(nbuRate);
    }

    @Override
    public RateView[] getRateArrayFor(Date dateFrom, Date dateTo, String currency) {
        return new RateView[0];
    }

    @Override
    public RateView getBestRate(Date dateFrom, Date dateTo, String currency) {
        return null;
    }

    private NBURate parseCurrentExchangeRateJson(Date date, String currency) {
        //date=20201021&json
        String dateStr = convertDate(date);

        int ccInt = CurrencyHelper.getCurrencyCode(currency);

        String fullUrl = String.format("%s?date=%s&json", BASE_URL, dateStr);

        String resultJson = parseUrl(fullUrl);
        JSONArray joList = new JSONArray(resultJson);
        if (currency == null) {
            //по умолчанию выводим все курсы:
            List<Map<String, Object>> ratesData = new ArrayList<Map<String, Object>>();
            for (int i =0; i < joList.length(); i++) {
                JSONObject oneEntry = joList.getJSONObject(i);
                JSONArray entKey = oneEntry.names();
                Map<String, Object> jsonItem = new HashMap<String, Object>();
                for (int j = 0; j < entKey.length(); j++){
                    String entValue = entKey.getString(j);
                    Object genCell = oneEntry.opt(entValue);//.toString();
                    jsonItem.put(entValue,genCell);
                }
                ratesData.add(jsonItem);
            }
            NBURate resultRate = new NBURate(date.toString(),
                    ratesData);
            return resultRate;

        } else {
            if (CurrencyHelper.getCurrencyCode(currency) != 0) {


                for (int i = 0; i < joList.length(); i++) {
                    JSONObject objects = joList.getJSONObject(i);
                    if (objects.get("r030").equals(ccInt)) {
                        BigDecimal deci = (BigDecimal) objects.get("rate");
                        double rate = deci.doubleValue();
                        return new NBURate(objects.get("exchangeDate").toString(),
                                rate,
                                objects.get("cc").toString(),
                                (Integer) objects.get("r030"));
                    }
                }
            }
        }
        return new NBURate("No data found for currency = " + currency); // возвращать страничку с ошибкой, что нет такой валюты
    }

   // private static String  convertDate(SimpleDateFormat fromFormat, SimpleDateFormat toFormat, Date date) {
   private static String  convertDate(Date date) {
        LocalDate localDate;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (date == null) {
            localDate = LocalDate.now();
        } else {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return dtf.format(localDate);

        /*
            try {
                Date dateFromUser = fromFormat.parse(dateStr);
                return toFormat.format(dateFromUser);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        return null;*/
    }

}
