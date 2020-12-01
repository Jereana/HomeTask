package com.yeremenko.myProject.controllers;

import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Past;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @GetMapping("/pb")

    public RateView getRate(@RequestParam(value = "date", required = false) String dateStr,
                            @RequestParam(value = "currency", required = false) String currency) throws ParseException {
        Date date = null;
        if (dateStr!=null){
            date = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        }
        return privateBank.getRateFor(date, currency.toUpperCase());
    }


    @GetMapping("/mono")
    public RateView getRateRestTemplate(@RequestParam(value = "date", required = false) String dateStr,
                                        @RequestParam(value = "currency", required = false) String currency) throws ParseException {
        return monoBank.getRateFor(null, currency.toUpperCase());
    }

    @GetMapping("/nbu")
    public RateView getRateJson(@RequestParam(value = "date", required = false) String dateStr,
                                @RequestParam(value = "currency", required = false) String currency) throws ParseException {
        Date date = null;
        if (dateStr!=null){
            date = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        }
       return nbu.getRateFor(date,currency);

    }
}
