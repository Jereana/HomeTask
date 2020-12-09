package com.yeremenko.myProject.controllers;

import com.yeremenko.myProject.CurrencyHelper;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.DateHelper;
import com.yeremenko.myProject.banks.PrivateBank;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private CurrencyService nbuXml;

    @GetMapping("/all")
    public List<RateView> getRates(@RequestParam(value = "date", required = false) String dateStr,
                                   @RequestParam(value = "currency", required = false) String currency,
                                   @RequestParam(value = "dateFrom", required = false) String dFrom,
                                   @RequestParam(value = "dateTo", required = false) String dTo,
                                   @RequestParam(value = "lastDaysCount", required = false) Integer lastDaysCount) throws ParseException {

        Date date = DateHelper.stringToDate(dateStr);
        Date dateFrom = DateHelper.stringToDate(dFrom);
        Date dateTo = DateHelper.stringToDate(dTo);

        List<RateView> ratesList = new ArrayList<>();

        currency = currency == null ? "USD" : currency.toUpperCase();

        //по умолчанию выводим для $ , но можно считывать это значение с файла проперти?!

        if (lastDaysCount == null) {
            lastDaysCount = 0;
        }

        // Здесь должна быть логика построения запросов, при задании различных параметров, согласно приоритету:
        // date
        // lastDaysCount
        // dateFrom + dateTo

        // 1. Если все даты null - формируем запрос на текущую дату
        // 2. Если задана дата date формируем запрос на указанную дату.
        //1 и 2 одно и то же.

        // 3. Если date= null и задана lastDaysCount - формируем запрос за последние lastDaysCount количество дней, влючая сегодня.
        // 4. Если date= null и lastDaysCount = null, и заданы dateFrom и dateTo - формируем запрос на период.
        // 3-4 одно и то же, реализуется в методе PrivateBank.getBestRate
        // начало логики

        //конец логики


        List<Date> datesList = DateHelper.getDatesList(lastDaysCount, dateFrom, dateTo);

        if (datesList.size()==0) {

            // на заданную дату/ на текущую дату
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
                            @RequestParam(value = "currency", required = false) String currency) throws ParseException {
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
                                @RequestParam(value = "currency", required = false) String currency) throws ParseException {
        Date date = DateHelper.stringToDate(dateStr);
        return nbu.getRateFor(date,currency);

    }




}
