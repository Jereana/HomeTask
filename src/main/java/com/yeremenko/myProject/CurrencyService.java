package com.yeremenko.myProject;

import com.yeremenko.myProject.views.RateView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface CurrencyService {

    RateView getRateFor(Date date, String currency);

}
