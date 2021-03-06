package com.yeremenko.myProject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CurrencyHelper {

    public static final Map<String, Integer> CURRENCY_CODES_MAP;
    static {
        CURRENCY_CODES_MAP = new HashMap<>();
        CURRENCY_CODES_MAP.put("USD", 840);
        CURRENCY_CODES_MAP.put("EUR", 978);
        CURRENCY_CODES_MAP.put("GBP",826);
        CURRENCY_CODES_MAP.put("JPY", 392);
        CURRENCY_CODES_MAP.put("CHF", 756);
        CURRENCY_CODES_MAP.put("CNY",156);
        CURRENCY_CODES_MAP.put("RUB", 643);
        CURRENCY_CODES_MAP.put("PLN", 985);
    }

    public static String getCurrencyText(int cc){

        return null;
    }
    public static int getCurrencyCode(String currency){
        if (currency == null) {
            return 0;
        }
        Integer resultCurrency = CURRENCY_CODES_MAP.get(currency.toUpperCase());
        if (resultCurrency == null) {
            return 0;
        }
        return CURRENCY_CODES_MAP.get(currency.toUpperCase());
    }

    public static String convertDateToString(Date date, String format){
        LocalDate localDate;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        if (date == null) {
            localDate = LocalDate.now();
        } else {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return dtf.format(localDate);
    }
}
