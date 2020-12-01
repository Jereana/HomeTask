package com.yeremenko.myProject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PBRate {
    public static String bankName = "Private bank";
    public String bank;
    public String date;
    public String currency;
    public int currencyCode;
    public String errorText;

    public List<ExchangeRate> exchangeRate;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBaseCurrency() {
        return "UAH";
    }

    public List<ExchangeRate> getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(List<ExchangeRate> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getBankName() {
        return bankName;
    }

    public String getCurrency() {

        return currency;
    }

    public void setCurrency(String currency) {
        if (currency!=null) {
            currency = currency.toUpperCase();
        }
        this.currency = currency;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExchangeRate {
        private String currency; // валюта, трёх-символьное значение
        private String currencyCode;
        private double saleRate;
        private double purchaseRate;

        private double saleRateNB;
        private double purchaseRateNB;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getSaleRate() {
            return saleRate;
        }

        public void setSaleRate(double saleRate) {
            this.saleRate = saleRate;
        }

        public double getPurchaseRate() {
            return purchaseRate;
        }

        public void setPurchaseRate(double purchaseRate) {
            this.purchaseRate = purchaseRate;
        }

        public double getSaleRateNB() {
            return saleRateNB;
        }

        public void setSaleRateNB(double saleRateNB) {
            this.saleRateNB = saleRateNB;
        }

        public double getPurchaseRateNB() {
            return purchaseRateNB;
        }

        public void setPurchaseRateNB(double purchaseRateNB) {
            this.purchaseRateNB = purchaseRateNB;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }
    }
}

