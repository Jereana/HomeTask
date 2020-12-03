package com.yeremenko.myProject;

import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;

import java.util.List;

// принимает на вход разные рейты и отдает на вью
public class RateMapper {

    public static RateView from(PBRate pbRate){

        //пока что вывод курса доллара
        List<PBRate.ExchangeRate> exchangeRateList = pbRate.getExchangeRate();

        PBRate.ExchangeRate exchangeRate1;
        double saleRate = 0;
        double purchaseRate = 0;

        if (exchangeRateList.size()!=0) {
            //
            //сюда PBRate приходит c заполненной currency!!!

            // проверка наличия валюты в списке:
            //
            //if(exchangeRateList.stream().anyMatch(rate -> currency.equals(rate.getCurrency()))) {
            exchangeRate1 = exchangeRateList
                    .stream()
                    .filter(rate -> pbRate.getCurrency().equals(rate.getCurrency()))
                    .findAny().orElse(null);
            // }
            if (exchangeRate1!=null) {
                saleRate = exchangeRate1.getSaleRate();
                purchaseRate = exchangeRate1.getPurchaseRate();
            }
            /*
                return new RateView(pbRate.getBank(),
                    exchangeRate1.getCurrency(),
                    pbRate.getCurrencyCode(),
                    pbRate.getDate(),
                    exchangeRate1.getSaleRate(),
                    exchangeRate1.getPurchaseRate(),
                    pbRate.getErrorText());
                    */


        }
        /*else {
            return new RateView(pbRate.getBank(),
                    pbRate.getCurrency(),
                    pbRate.getCurrencyCode(),
                    pbRate.getDate(),
                    0,0,
                    pbRate.getErrorText());
        }*/






/*
            if (pbRate.getCurrency() == null) {


                PBRate.ExchangeRate exchangeRate = pbRate.getExchangeRate()
                        .stream()
                        .filter(rate -> "USD".equals(rate.getCurrency())) // тут может біть ошибка NoSuchElementException, если
                        // курсі валют на сегодня еще не обновили на сервере
                        .findAny()
                        .get();
                return new RateView(pbRate.getBank(),
                        exchangeRate.getCurrency(),
                        CurrencyHelper.getCurrencyCode(exchangeRate.getCurrency()),
                        pbRate.getDate(),
                        exchangeRate.getSaleRate(),
                        exchangeRate.getPurchaseRate(),
                        pbRate.getErrorText());
            } else {
                // проверка наличия валюты в списке exchangeRate
                //PBRate.ExchangeRate exchangeRateCheck = pbRate.exchangeRate.
                if (pbRate.getExchangeRate()
                        .stream()
                        .anyMatch(rate -> pbRate.getCurrency().equals(rate.getCurrency()))) {
                    PBRate.ExchangeRate exchangeRate = pbRate.getExchangeRate()
                            .stream()
                            .filter(rate -> pbRate.getCurrency().equals(rate.getCurrency()))
                            .findAny().get();
                    return new RateView(pbRate.getBank(),
                            exchangeRate.getCurrency(),
                            pbRate.getCurrencyCode(),
                            pbRate.getDate(),
                            exchangeRate.getSaleRate(),
                            exchangeRate.getPurchaseRate(),
                            pbRate.getErrorText());
                } else {
                    return new RateView(pbRate.getBankName(),
                            pbRate.getCurrency(),
                            CurrencyHelper.getCurrencyCode(pbRate.getCurrency()),
                            pbRate.getDate(),
                            0,
                            0,
                            pbRate.getErrorText()
                    );
                }
*/

            //}
       // }
        //долже быть какой-то ретурн в конце. Может null?
        return new RateView(pbRate.getBankName(),
                pbRate.getCurrency(),
                CurrencyHelper.getCurrencyCode(pbRate.getCurrency()),
                pbRate.getDate(),
                saleRate,
                purchaseRate,
                pbRate.getErrorText());
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
