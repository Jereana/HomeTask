package com.yeremenko.myProject.controllers;

import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.DateHelper;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/rates")
public class ExchangeRateController {

    private static final Logger LOGGER = Logger.getLogger(ExchangeRateController.class.getName());

    @Autowired
    private List<CurrencyService> currencyServices;

    @GetMapping("/all")
    public List<RateView> getRates(@RequestParam(value = "date", required = false)
                                        @DateTimeFormat(pattern = "d.MM.yyyy") LocalDate date,
                                   @RequestParam(value = "currency", required = false) String currency,
                                   @RequestParam(value = "dateFrom", required = false)
                                       @DateTimeFormat(pattern = "d.MM.yyyy") LocalDate dFrom,
                                   @RequestParam(value = "dateTo", required = false)
                                       @DateTimeFormat(pattern = "d.MM.yyyy") LocalDate dTo,
                                   @RequestParam(value = "lastDaysCount", required = false) Integer lastDaysCount)
                                   {

        List<RateView> ratesList;

        currency = currency == null ? "USD" : currency.toUpperCase();
        if (lastDaysCount == null) {
            lastDaysCount = 0;
        }
        List<LocalDate> datesList = DateHelper.getLocalDatesList(date, lastDaysCount, dFrom, dTo);
        ratesList = getBestRate(datesList, currency);

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


    private List<RateView> getBestRate( List<LocalDate> datesList, String currency) {

        List<RateView> ratesList = new ArrayList<>();
        List<Future<RateView>> futures = new ArrayList<>();

        int poolSize = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        for (CurrencyService service : currencyServices) {
            Callable<RateView> callable = () -> {
                final RateView[] minRate = new RateView[1];
                for (LocalDate date : datesList) {
                    LOGGER.log(Level.INFO, service.getName().concat(" is starting"));
                     RateView rateView = service.getRateFor(date, currency);
                    LOGGER.log(Level.INFO, service.getName().concat(" finished"));
                    minRate[0] = getMinRate(minRate[0], rateView);
                }
                return minRate[0];
            };
            futures.add(executorService.submit(callable));
        }
        try {
            for (Future<RateView> future: futures) {
                ratesList.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return ratesList;
    }
}
