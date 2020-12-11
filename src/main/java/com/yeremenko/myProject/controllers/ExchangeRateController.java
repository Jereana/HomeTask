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
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/rates")
public class ExchangeRateController {

    private static final Logger LOGGER = Logger.getLogger(ExchangeRateController.class.getName());

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

        List<RateView> ratesList;

        currency = currency == null ? "USD" : currency.toUpperCase();

        if (lastDaysCount == null) {
            lastDaysCount = 0;
        }

        List<Date> datesList = DateHelper.getDatesList(date, lastDaysCount, dateFrom, dateTo);

        ratesList = getBestRate(datesList, currency);

        return ratesList;
    }

    private List<RateView> getBestRate( List<Date> datesList, String currency) {

        List<RateView> ratesList = new ArrayList<>();

        final RateView[] minRateMono = new RateView[1];
        final RateView[] minRatePB = new RateView[1];
        final RateView[] minRateNBU = new RateView[1];
        final RateView[] minRateNBUXml = new RateView[1];
        for (Date date : datesList) {
            Thread threadMono = new Thread(() -> {
                RateView rateMono = monoBank.getRateFor(date, currency);
                    minRateMono[0] = getMinRate(minRateMono[0],rateMono);
            });
            threadMono.start();

            Thread threadPB = new Thread(() -> {
                RateView ratePB = privateBank.getRateFor(date, currency);
                minRatePB[0] = getMinRate(minRatePB[0], ratePB);

            });
            threadPB.start();

            Thread threadNBU = new Thread(() -> {
                RateView rateNBU = nbu.getRateFor(date, currency);
                minRateNBU[0] = getMinRate(minRateNBU[0], rateNBU);
            });
            threadNBU.start();

            Thread threadNBUXml = new Thread(() -> {
                RateView rateNBUXml = nbuXml.getRateFor(date, currency);
                minRateNBUXml[0] = getMinRate(minRateNBUXml[0], rateNBUXml);
            });
            threadNBUXml.start();

            try {
                threadMono.join();
                threadPB.join();
                threadNBU.join();
                threadNBUXml.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.log(Level.INFO, "______________________");
        }

        ratesList.add(minRatePB[0]);
        ratesList.add(minRateMono[0]);
        ratesList.add(minRateNBU[0]);
        ratesList.add(minRateNBUXml[0]);

        return ratesList;
    }

    private RateView getMinRate(RateView minRate, RateView currentRate) {
        if (minRate != null && minRate.getSaleRate()!=0) {
            if (minRate.getSaleRate() == currentRate.getSaleRate()) {
                minRate.setDate(minRate.getDate() + ";" + currentRate.getDate());
            } else if (minRate.getSaleRate() > currentRate.getSaleRate()) {
                minRate = currentRate;
            }
        } else {
            minRate = currentRate;
        }
        LOGGER.log(Level.INFO, new StringBuilder().append(currentRate.getBank())
                .append("; ")
                .append(currentRate.getSaleRate())
                .append("; ").append(currentRate.getDate()).toString());
        return minRate;
    }
}
