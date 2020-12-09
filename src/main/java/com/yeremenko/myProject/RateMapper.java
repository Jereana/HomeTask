package com.yeremenko.myProject;

import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;

import java.util.List;

// принимает на вход разные рейты и отдает на вью
public class RateMapper {

    public static RateView from(PBRate pbRate){

        List<PBRate.ExchangeRate> exchangeRateList = pbRate.getExchangeRate();

        PBRate.ExchangeRate exchangeRate;
        double saleRate = 0;
        double purchaseRate = 0;
        String errorText = pbRate.getErrorText()==null?"":pbRate.getErrorText();

        if (exchangeRateList.size()!=0) {
            exchangeRate = exchangeRateList
                    .stream()
                    .filter(rate -> pbRate.getCurrency().equals(rate.getCurrency()))
                    .findAny().orElse(null);
            if (exchangeRate!=null) {
                saleRate = exchangeRate.getSaleRate()==0?exchangeRate.getSaleRateNB():exchangeRate.getSaleRate();
                purchaseRate = exchangeRate.getPurchaseRate()==0?exchangeRate.getPurchaseRateNB():exchangeRate.getPurchaseRate();
            } else {
                errorText = errorText.concat(" No rate for currency " + pbRate.getCurrency() + ".");
            }
        }

        return new RateView(pbRate.getBankName(),
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

        return new RateView(MonobankRate.bankName,
                monobankRate.getCurrency(),
                monobankRate.getCurrencyCodeA(),
                monobankRate.getDate(),
                monobankRate.getRateSell(),
                monobankRate.getRateBuy(),
                monobankRate.getErrorText());
    }

    public static RateView from(NBURate nbuRate) {// нацбанк
        if (nbuRate == null) {
            return null;
        }

        return new RateView(NBURate.bankName,
                nbuRate.getCurrency(),
                nbuRate.getCurrencyCode(),
                nbuRate.getExchangeDate(),
                nbuRate.getRate(),
                nbuRate.getRate(),
                nbuRate.getErrorText());
    }
}
