package com.yeremenko.myProject.controllers;

import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rates")
public class ExchangeRateController {

    @Qualifier("mono")
    @Autowired
    private CurrencyService monoBank;

    @Qualifier("nbu")
    @Autowired
    private CurrencyService nbu;

    @Qualifier("pb")
    @Autowired
    private CurrencyService privateBank;

    @Qualifier("nbu_xml")
    @Autowired
    private CurrencyService nbu_xml;

    @GetMapping("/all")
    public List<RateView> getRates(@RequestParam(value = "date", required = false) String dateStr,
                                   @RequestParam(value = "currency", required = false) String currency,
                                   @RequestParam(value = "dateFrom", required = false) String dFrom,
                                   @RequestParam(value = "dateTo", required = false) String dTo) throws ParseException {

        Date date = stringToDate(dateStr);
        Date dateFrom = stringToDate(dFrom);
        Date dateTo = stringToDate(dTo);
        List <Date> dateList = Arrays.asList(date, dateFrom, dateTo);

        List<RateView> ratesList = new ArrayList<>();

        currency = currency==null?null:currency.toUpperCase();

        ratesList.add(getRatePB(date, currency, dateFrom, dateTo));
        ratesList.add(getRateMono(date, currency));
        ratesList.add(getRateNBU_Json(date, currency));
        ratesList.add(getRateNBU_XML(date, currency));
        return ratesList;
    }


    public RateView getRatePB(Date date, String currency, Date dateFrom, Date dateTo) {
     //   Date date = stringToDate(dateStr);
     //   Date dateFrom = stringToDate(dFrom);
     //   Date dateTo = stringToDate(dTo);

        List <Date> dateList = Arrays.asList(date, dateFrom, dateTo);

        return privateBank.getRateFor(date, currency);

    }

    public RateView getRateMono(Date date, String currency){
        return monoBank.getRateFor(date, currency);
    }

    public RateView getRateNBU_Json(Date date, String currency){
        return nbu.getRateFor(date,currency);
    }

    public RateView getRateNBU_XML(Date date, String currency){
        return nbu_xml.getRateFor(date, currency);
    }

    @GetMapping("/pb")

    public RateView getRate(@RequestParam(value = "date", required = false) String dateStr,
                            @RequestParam(value = "currency", required = false) String currency) throws ParseException {
        Date date = stringToDate(dateStr);
        return privateBank.getRateFor(date, currency.toUpperCase());
    }


    @GetMapping("/mono")
    public RateView getRateRestTemplate(@RequestParam(value = "date", required = false) String dateStr,
                                        @RequestParam(value = "currency", required = false) String currency) {
        return monoBank.getRateFor(null, currency);
    }

    @GetMapping("/nbu")
    public RateView getRateJson(@RequestParam(value = "date", required = false) String dateStr,
                                @RequestParam(value = "currency", required = false) String currency) throws ParseException {
        Date date = stringToDate(dateStr);
        return nbu.getRateFor(date,currency);

    }

    public Date stringToDate(String dateStr) throws ParseException {
        Date date = null;
        if (dateStr!=null){
            date = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        }
        return date;
    }

}
