package com.yeremenko.myProject.banks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.DateHelper;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

@Qualifier("nbu_xml")
@Component
public class NBUxml implements CurrencyService {

    public static final String BASE_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";

    @Override
    public RateView getRateFor(Date date, String currency) {
        NBURate nbuRate = parseCurrentExchangeRateXML(date, currency);// currency код получить из currencyHelper
        return RateMapper.from(nbuRate);
    }

    private NBURate parseCurrentExchangeRateXML(Date date, String currency) {
        String errorText;
        String dateStrUrl = DateHelper.convertDateToString(date, "yyyyMMdd");
        String dateStr = DateHelper.convertDateToString(date, "dd.MM.yyyy");
        String url = String.format("%s?date=%s&xml", BASE_URL, dateStrUrl);

        try {
            HttpResponse<String> response = Unirest.get(url)
                    .queryString("date", dateStrUrl).asString();

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            InputSource input = new InputSource(new StringReader(response.getBody()));
            Document document = documentBuilder.parse(input);
            document.getDocumentElement().normalize();

            NodeList nList = document.getElementsByTagName("currency");
            if (nList.getLength() != 0) {
                for (int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (eElement
                                .getElementsByTagName("cc")
                                .item(0)
                                .getTextContent().equals(currency)) {
                            return new NBURate(eElement.getElementsByTagName("exchangedate").item(0).getTextContent(),
                                    Double.parseDouble(eElement.getElementsByTagName("rate").item(0).getTextContent()),
                                    eElement.getElementsByTagName("cc").item(0).getTextContent(),
                                    Integer.parseInt(eElement.getElementsByTagName("r030").item(0).getTextContent()));
                        }
                    }
                }
                errorText = "No rate for currency " + currency;
            } else {
                errorText = "No rate for date " + dateStr;
            }
        } catch (SAXException | UnirestException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
            return new NBURate(dateStr, currency, e.getMessage());
        }

        return new NBURate(dateStr, currency, errorText);
    }
}
