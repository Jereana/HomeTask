package com.yeremenko.myProject.views;

public class RateView {

    private String bank;
    private String currency;
    private int currencyCode;
    private double saleRate;
    private double purchaseRate;
    private String date;

   public RateView(String bank, String currency, int currencyCode, String date, double saleRate, double purchaseRate) {
        this.bank = bank;
        this.currency = currency;
        this.currencyCode = currencyCode;
        this.date = date;
        this.saleRate = saleRate;
        this.purchaseRate = purchaseRate;
    }

    public RateView(String bank, String currency, String date) {
        this.bank = bank;
        this.currency = currency;
        this.date = date;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

}
