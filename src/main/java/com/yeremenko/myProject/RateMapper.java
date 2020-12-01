package com.yeremenko.myProject;

import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.model.PBRate;
import com.yeremenko.myProject.views.RateView;

import java.util.List;

// принимает на вход разные рейты и отдает на вью
public class RateMapper {

    public static RateView from(PBRate pbRate){
        if (pbRate == null) {
            return null;
        }


        if (pbRate.getCurrency() == null) {
            // нужно выводить весь список
           // List<PBRate.ExchangeRate> exchangeRate = pbRate.getExchangeRate();
          //  return new RateView()
            //пока что вывод курса доллара
            PBRate.ExchangeRate exchangeRate = pbRate.getExchangeRate()
                    .stream()
                    .filter(rate -> "USD".equals(rate.getCurrency()))
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
                        pbRate.currency,
                        CurrencyHelper.getCurrencyCode(pbRate.getCurrency()),
                        pbRate.getDate(),
                        0,
                        0,
                        pbRate.getErrorText()
                        );
            }



        }
    }

    public static RateView from(String jsonNode) {
        return null;
        /*new RateView(jsonNode.getBank(),
                jsonNode.getCurrency(),
                jsonNode.getDate(),
                jsonNode.getSaleRate(),
                jsonNode.getPurchaseRate());
                */
    }

    public static RateView from(MonobankRate monobankRate) {
        if (monobankRate == null) {
            return null;
        }
        if (monobankRate.getErrorText()!= null) {
            // показывать страничку с ошибкой
          // return ("/errorPage");
        }
        return new RateView(MonobankRate.bankName,
                monobankRate.getCurrency(),
                monobankRate.getCurrencyCodeA(),
                (monobankRate.getDate()==null?null:monobankRate.getDate().toString()),
                monobankRate.getRateSell(),
                monobankRate.getRateBuy(),
                monobankRate.getErrorText());
    }

    public static RateView from(NBURate nbuRate) {// нацбанк
        if (nbuRate == null) {
            return null;
        }

      /*  if (nbuRate.getCurrency() == null) {
            return new RateView(NBURate.bankName,
                    null,
                    nbuRate.getExchangeDate(),
                    nbuRate.getRatesData());

        }*/

        return new RateView("NBU",
                null,
                null,
                nbuRate.getRatesData());
        /*
        return new RateView(NBURate.bankName,
                nbuRate.getCurrency(),
                nbuRate.getCurrencyCode(),
                nbuRate.getExchangeDate(),
                nbuRate.getRate(),
                nbuRate.getRate(),
                nbuRate.getErrorText());
*/
    }
}
