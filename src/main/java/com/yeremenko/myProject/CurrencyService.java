package com.yeremenko.myProject;

import com.yeremenko.myProject.views.RateView;
import java.time.LocalDate;

@FunctionalInterface
public interface CurrencyService {

    RateView getRateFor(LocalDate date, String currency);

    default String getName(){
        return this.getClass().getName();
    }
}
