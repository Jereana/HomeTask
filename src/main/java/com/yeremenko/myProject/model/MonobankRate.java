package com.yeremenko.myProject.model;

import java.sql.Timestamp;
import java.util.Date;

public class MonobankRate {

    public Timestamp timestamp;
    public String date;
    public double rateBuy;
    public double rateSell;
    public String currency; // валюта
    public int currencyCodeA;
    public static String bankName = "MonoBank";
    public String errorText;


    public MonobankRate(String date, double rateBuy, double rateSell, int currencyCodeA) {
        this.date = date;
        this.rateBuy = rateBuy;
        this.rateSell = rateSell;
        this.currencyCodeA = currencyCodeA;
    }

    public MonobankRate() {
    }

    public MonobankRate(String date, String currency, int currencyCode, String errorText) {
        this.date = date;
        this.currency = currency;
        this.currencyCodeA = currencyCode;
        this.errorText = errorText;
    }

    public static String getBank() {
        return bankName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
