package com.yeremenko.myProject.controllers;

import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.DateHelper;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private CurrencyService nbuXml;

    @GetMapping("/all")
    public List<RateView> getRates(@RequestParam(value = "date", required = false) String dateStr,
                                   @RequestParam(value = "currency", required = false) String currency,
                                   @RequestParam(value = "dateFrom", required = false) String dFrom,
                                   @RequestParam(value = "dateTo", required = false) String dTo,
                                   @RequestParam(value = "lastDaysCount", required = false) Integer lastDaysCount) {

        Date date = DateHelper.stringToDate(dateStr);
        Date dateFrom = DateHelper.stringToDate(dFrom);
        Date dateTo = DateHelper.stringToDate(dTo);

        List<RateView> ratesList = new ArrayList<>();

        currency = currency == null ? "USD" : currency.toUpperCase();

        if (lastDaysCount == null) {
            lastDaysCount = 0;
        }

        List<Date> datesList = DateHelper.getDatesList(lastDaysCount, dateFrom, dateTo);

        if (datesList.isEmpty()) {
            ratesList.add(privateBank.getRateFor(date, currency));
            ratesList.add(monoBank.getRateFor(date, currency));
            ratesList.add(nbu.getRateFor(date, currency));
            ratesList.add(nbuXml.getRateFor(date, currency));
        } else
            if ((date == null) && (lastDaysCount!=0 || (dateFrom!=null && dateTo != null))){

                ratesList.addAll(privateBank.getBestRate(dateFrom,dateTo,currency,lastDaysCount));
                ratesList.addAll(monoBank.getBestRate(dateFrom, dateTo, currency, lastDaysCount));
                ratesList.addAll(nbu.getBestRate(dateFrom, dateTo, currency, lastDaysCount));
                ratesList.addAll(nbuXml.getBestRate(dateFrom, dateTo, currency, lastDaysCount));
                }
        return ratesList;
    }
/*
    private List<RateView> getBestRate(Date dateFrom, Date dateTo, String currency, int lastDaysCount) {

        List<RateView> ratesList = new ArrayList<>();
        List<Date> datesList = DateHelper.getDatesList(lastDaysCount, dateFrom, dateTo);
        List<RateView> minRatesListMono = new ArrayList<>();
        List<RateView> minRatesListPB = new ArrayList<>();
        List<RateView> minRatesListNBU = new ArrayList<>();
        List<RateView> minRatesListNBUXml = new ArrayList<>();


        for (Date date : datesList) {
            RateView rateViewMono = getRateMono(date, currency);
            ratesList.add(rateViewMono);

            RateView rateViewPB = getRatePB(date, currency);
        }

        double minSaleRate = 1000;
        System.out.println("All list of NBUxml rates:");
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
*/

    @GetMapping("/pb")
    public RateView getRate(@RequestParam(value = "date", required = false) String dateStr,
                            @RequestParam(value = "currency", required = false) String currency) {
        Date date = DateHelper.stringToDate(dateStr);
        return privateBank.getRateFor(date, currency.toUpperCase());
    }


    @GetMapping("/mono")
    public RateView getRateRestTemplate(@RequestParam(value = "date", required = false) String dateStr,
                                        @RequestParam(value = "currency", required = false) String currency) {
        return monoBank.getRateFor(null, currency);
    }

    @GetMapping("/nbu")
    public RateView getRateJson(@RequestParam(value = "date", required = false) String dateStr,
                                @RequestParam(value = "currency", required = false) String currency) {
        Date date = DateHelper.stringToDate(dateStr);
        return nbu.getRateFor(date,currency);
    }
}
