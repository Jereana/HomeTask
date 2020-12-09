package com.yeremenko.myProject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.banks.PrivateBank;
import com.yeremenko.myProject.controllers.ExchangeRateController;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.views.RateView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@SpringBootApplication
public class MyProjectApplication {

	public static void main(String[] args) throws ParseException {

		SpringApplication.run(MyProjectApplication.class, args);

/*
		Date date = CurrencyHelper.stringToDate("04.12.2020");
		String todayDateStr = CurrencyHelper.convertDateToString(null, "dd.MM.yyyy");
		Date todayDate = CurrencyHelper.stringToDate(todayDateStr);

		System.out.println(date.equals(todayDate));

		System.out.println("--------START--------");

		ExchangeRateController erc = new ExchangeRateController();

		List<RateView> listRV= erc.getRates(null,null,"30.11.2020","04.12.2020",0);

		for (RateView r: listRV) {
			System.out.println("Bank: " + r.getBank()+
					"; Date: " + r.getDate() +
					"; Currency: " + r.getCurrency() +
					"; Rate: " + r.getSaleRate());

		}

*/
/*

*/


		Date dateFrom = DateHelper.stringToDate("01.12.2020");
		Date dateTo = DateHelper.stringToDate("05.12.2020");
		Date checkDate = DateHelper.stringToDate("05.12.2020");

		List <Date> dateList = DateHelper.getDatesList(0, dateFrom, dateTo);
		for (Date d: dateList) {
			System.out.println(d);
		}

		Boolean isIn = DateHelper.checkDateIsInPeriod(dateFrom, dateTo, checkDate);
		System.out.println(isIn);
		System.out.println("Main program is finished");
	}





}
