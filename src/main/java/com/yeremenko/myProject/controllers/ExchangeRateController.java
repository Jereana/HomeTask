package com.yeremenko.myProject.controllers;

import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.DateHelper;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${exchangeRateController.poolSize}")
    private int poolSize;

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
        String logString = String.format("%s; %f; %s", currentRate.getBank(), currentRate.getSaleRate(), currentRate.getDate());
        LOGGER.log(Level.INFO, logString);
        return minRate;
    }

    private List<RateView> getBestRate( List<LocalDate> datesList, String currency) {

        List<RateView> ratesList = new ArrayList<>();
        List<Future<RateView>> futures = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        for (CurrencyService service : currencyServices) {
            Callable<RateView> callable = () -> {
                final RateView[] minRate = new RateView[1];
                for (LocalDate date : datesList) {
                    String strToLogStart = service.getName().concat(" is starting");
                    LOGGER.log(Level.INFO, strToLogStart);
                    RateView rateView = service.getRateFor(date, currency);
                    String strToLogFinish = service.getName().concat(" finished");
                    LOGGER.log(Level.INFO, strToLogFinish);
                    minRate[0] = getMinRate(minRate[0], rateView);
                }
                return minRate[0];
            };
            futures.add(executorService.submit(callable));
        }
        for (Future<RateView> future: futures) {
            try {
                ratesList.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
                if (e.getClass().equals(InterruptedException.class)) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return ratesList;
    }
}
