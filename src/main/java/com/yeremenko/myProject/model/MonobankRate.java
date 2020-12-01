package com.yeremenko.myProject.model;


import com.yeremenko.myProject.CurrencyHelper;

import java.sql.Timestamp;

public class MonobankRate {

    public Timestamp date;
    public double rateBuy;
    public double rateSell;
    public String currency; // валюта
    public int currencyCodeA;
    public static String bankName = "Monobank";
    public String errorText;


    public MonobankRate(Timestamp date, double rateBuy, double rateSell, int currencyCodeA) {
        this.date = date;
        this.rateBuy = rateBuy;
        this.rateSell = rateSell;
        this.currencyCodeA = currencyCodeA;
    }

    public MonobankRate() {
    }

    public static String getBank() {
        return bankName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public double getRateBuy() {
        return rateBuy;
    }

    public void setRateBuy(double rateBuy) {
        this.rateBuy = rateBuy;
    }

    public double getRateSell() {
        return rateSell;
    }

    public void setRateSell(double rateSell) {
        this.rateSell = rateSell;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCurrencyCodeA() {
        return currencyCodeA;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCodeA = currencyCode;
    }
}
