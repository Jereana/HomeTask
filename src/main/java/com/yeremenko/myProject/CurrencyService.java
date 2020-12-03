package com.yeremenko.myProject;

import com.yeremenko.myProject.views.RateView;

import java.util.Date;

public interface CurrencyService {

    RateView getRateFor(Date date, String currency);

    RateView getBestRate(Date dateFrom, Date dateTo, String currency); // реализовать в каждом банке.
}
