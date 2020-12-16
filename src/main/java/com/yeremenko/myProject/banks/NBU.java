package com.yeremenko.myProject.banks;

import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.views.RateView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.yeremenko.myProject.JsonUtils.parseUrl;

@Component
public class NBU implements CurrencyService {

    public static final String BASE_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";
    private static final Logger LOGGER = Logger.getLogger(NBU.class.getName());

    @Override
    public RateView getRateFor(LocalDate date, String currency) {
        NBURate nbuRate = parseCurrentExchangeRateJson(date, currency);
        return RateMapper.from(nbuRate);
    }

    private NBURate parseCurrentExchangeRateJson(LocalDate date, String currency) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStrUrl = date.format(dtf);
        String errorText = null;
        String fullUrl = String.format("%s?date=%s&json", BASE_URL, dateStrUrl);

        String resultJson = parseUrl(fullUrl);
        JSONArray joList = new JSONArray(resultJson);
        if (!joList.isEmpty()) {
            for (int i = 0; i < joList.length(); i++) {
                JSONObject objects = joList.getJSONObject(i);
                if (objects.get("cc").equals(currency)) {
                    BigDecimal deci = (BigDecimal) objects.get("rate");
                    double rate = deci.doubleValue();
                    return new NBURate(date,
                            rate,
                            objects.get("cc").toString(),
                            (Integer) objects.get("r030"));
                }
            }
            errorText = "Bank NBU: date " + date.toString() + "; currency " +
                    currency + "; No rate for currency.";
        } else {
            errorText = "Bank NBU: date " + date.toString() + "; currency " +
                    currency + ";  No rate for date.";
        }
        if (errorText != null) {
            LOGGER.log(Level.INFO, errorText);
        }
        return new NBURate(date, currency);
    }
}
