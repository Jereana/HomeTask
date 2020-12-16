package com.yeremenko.myProject.model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class MonobankRate {

    public Timestamp timestamp;

    public LocalDate date;
    public double rateBuy;
    public double rateSell;
    public String currency;
    public int currencyCodeA;
    public static final String BANK_NAME = "MonoBank";

    public MonobankRate(LocalDate date, double rateBuy, double rateSell, int currencyCodeA) {
        this.date = date;
        this.rateBuy = rateBuy;
        this.rateSell = rateSell;
        this.currencyCodeA = currencyCodeA;
    }

    public MonobankRate() {
    }

    public MonobankRate(LocalDate date, String currency, int currencyCode) {
        this.date = date;
        this.currency = currency;
        this.currencyCodeA = currencyCode;
    }

    public MonobankRate(LocalDate date, double rateSell, String currency, int currencyCodeA) {
        this.date = date;
        this.rateSell = rateSell;
        this.currency = currency;
        this.currencyCodeA = currencyCodeA;
    }

    public static String getBank() {
        return BANK_NAME;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
