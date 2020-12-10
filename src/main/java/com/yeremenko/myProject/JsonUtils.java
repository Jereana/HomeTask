package com.yeremenko.myProject;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T parseJsonWithJackson(String json, Class<T> bankType) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, bankType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
