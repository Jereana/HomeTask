package com.yeremenko.myProject;

import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;

import java.util.List;

public class RateMapper {

    public static RateView from(PBRate pbRate){
        if (pbRate == null) {
            return null;
        }

        List<PBRate.ExchangeRate> exchangeRateList = pbRate.getExchangeRate();
        String errorText = pbRate.getErrorText();
        PBRate.ExchangeRate exchangeRate;
        double saleRate = 0;
        double purchaseRate = 0;

        if (!exchangeRateList.isEmpty()) {
            exchangeRate = exchangeRateList
                    .stream()
                    .filter(rate -> pbRate.getCurrency().equals(rate.getCurrency()))
                    .findAny().orElse(null);
            if (exchangeRate!=null) {
                saleRate = exchangeRate.getSaleRate()==0?exchangeRate.getSaleRateNB():exchangeRate.getSaleRate();
                purchaseRate = exchangeRate.getPurchaseRate()==0?exchangeRate.getPurchaseRateNB():exchangeRate.getPurchaseRate();
            } else {
                if (errorText==null) {
                    errorText = "";
                }
                errorText = errorText.concat(" No rate for currency " + pbRate.getCurrency() + ".");
            }
        }

        return new RateView(PBRate.BANK_NAME,
                pbRate.getCurrency(),
                CurrencyHelper.getCurrencyCode(pbRate.getCurrency()),
                pbRate.getDate(),
                saleRate,
                purchaseRate,
                errorText);
    }

    public static RateView from(MonobankRate monobankRate) {
        if (monobankRate == null) {
            return null;
        }

        return new RateView(MonobankRate.BANK_NAME,
                monobankRate.getCurrency(),
                monobankRate.getCurrencyCodeA(),
                monobankRate.getDate(),
                monobankRate.getRateSell(),
                monobankRate.getRateBuy(),
                monobankRate.getErrorText());
    }

    public static RateView from(NBURate nbuRate) {
        if (nbuRate == null) {
            return null;
        }

        return new RateView(NBURate.BANK_NAME,
                nbuRate.getCurrency(),
                nbuRate.getCurrencyCode(),
                nbuRate.getExchangeDate(),
                nbuRate.getRate(),
                nbuRate.getRate(),
                nbuRate.getErrorText());
    }
}
