package com.yeremenko.myProject.banks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.CurrencyService;
import com.yeremenko.myProject.RateMapper;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.views.RateView;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class NBUxml implements CurrencyService {

    public static final String BASE_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";
    private static final Logger LOGGER = Logger.getLogger(NBUxml.class.getName());

    @Override
    public RateView getRateFor(LocalDate date, String currency) {
        NBURate nbuRate = parseCurrentExchangeRateXML(date, currency);
        return RateMapper.from(nbuRate);
    }

    private NBURate parseCurrentExchangeRateXML(LocalDate date, String currency) {

        String errorText;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStrUrl = date.format(dtf);

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
                            return new NBURate(date,
                                    Double.parseDouble(eElement.getElementsByTagName("rate").item(0).getTextContent()),
                                    eElement.getElementsByTagName("cc").item(0).getTextContent(),
                                    Integer.parseInt(eElement.getElementsByTagName("r030").item(0).getTextContent()));
                        }

                    }
                }
                errorText = String.format("Bank NBU: date %s; currency %s; No rate for currency.", date.toString(), currency);
            } else {
                errorText = String.format("Bank NBU: date %s; currency %s; No rate for date.", date.toString(), currency);
            }
        } catch (SAXException | UnirestException | ParserConfigurationException | IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return new NBURate(date, currency);
        }
        if (errorText != null) {
            LOGGER.log(Level.INFO, errorText);
        }
        return new NBURate(date, currency);
    }
}
