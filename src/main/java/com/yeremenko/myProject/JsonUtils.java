package com.yeremenko.myProject;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.yeremenko.myProject.model.MonobankRate;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.model.PBRate;

import javafx.beans.binding.IntegerBinding;
import net.minidev.json.JSONValue;

import net.minidev.json.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class JsonUtils {

    public static String parseUrl(String urlStr) {
        URL url = createUrl(urlStr);
        if (url == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    // создаем объект URL из указанной в параметре строки
    public static URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


//jackson
    public static <T> T parseJsonWithJackson(String json, Class<T> bankType) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, bankType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

/*
    public static <T> T parseCurrentExchangeRateRestTemplate(String url, String currency, Class<T> bankType){
        RestTemplate restTemplate = new RestTemplate();
        //String className = bankType.getName();
        //try{
            //T[] rateArray;

         //   T[] rateArray = restTemplate.getForObject(url, bankType[]);
         // if (rateArray != null) {
               // return Arrays.stream(rateArray).
               //         filter(rate -> rate.
               //                 getCurrencyCodeA().
                //                equals("840")).//USD код = 840
                //        findAny().orElse(null);
        //    }


      //  } catch (RestClientException e) {
     //       e.printStackTrace();
     //   }

        return null;
    //}
*/
    /*
    //rest template
    private static MonobankRate parseCurrentExchangeRateRestTemplate(String currency) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            MonobankRate[] monobankRateArray = restTemplate.getForObject(BASE_URL, MonobankRate[].class);
            if (monobankRateArray != null) {
                return Arrays.stream(monobankRateArray).
                        filter(rate -> rate.
                                getCurrencyCodeA().
                                equals("840")).//USD код = 840
                        findAny().orElse(null);
            }
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            MonobankRate mbRate = new MonobankRate();
            mbRate.setErrorText(e.getMessage());
            return mbRate;
        }
        return null;
    }
*/
}
