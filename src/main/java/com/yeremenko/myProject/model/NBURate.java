package com.yeremenko.myProject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NBURate {

    public static String bankName = "NBU";
    public String exchangeDate;
    public double rate;
    public String currency; // валюта буквы
    public int currencyCode; // код валюты
    public String errorText;

    public NBURate(String exchangeDate, double rate, String currency, int currencyCode) {
        this.exchangeDate = exchangeDate;
        this.rate = rate;
        this.currency = currency;
        this.currencyCode = currencyCode;
    }

    public NBURate(String exchangeDate, String currency, String errorText) {
        this.exchangeDate = exchangeDate;
        this.currency = currency;
        this.errorText = errorText;
    }

    public static String getBankName() {
        return bankName;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCc(String currency) {
        this.currency = currency;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExchangeRate {
        public String exchangeDate;
        public double rate;
        public String currency; // валюта буквы
        public int currencyCode;
        public String currencyName;

        public ExchangeRate(String exchangeDate, double rate, String currency, int currencyCode, String currencyName) {
            this.exchangeDate = exchangeDate;
            this.rate = rate;
            this.currency = currency;
            this.currencyCode = currencyCode;
            this.currencyName = currencyName;
        }

        public String getExchangeDate() {
            return exchangeDate;
        }

        public void setExchangeDate(String exchangeDate) {
            this.exchangeDate = exchangeDate;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(int currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }
    }
}
