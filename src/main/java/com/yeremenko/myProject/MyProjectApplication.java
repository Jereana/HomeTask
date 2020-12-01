package com.yeremenko.myProject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yeremenko.myProject.model.NBURate;
import com.yeremenko.myProject.views.RateView;
import net.minidev.json.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
public class MyProjectApplication {

	public static final String EXCHANGE_RATE_JSON_URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=01.12.2014";

	public static void main(String[] args) throws IOException, ParseException {

		SpringApplication.run(MyProjectApplication.class, args);

	//	URL url = JsonUtils.createUrl(EXCHANGE_RATE_JSON_URL);
	//	String resultJson = JsonUtils.parseUrl(url);
	//	System.out.println("Полученный JSON:\n" + resultJson);
	//	System.out.println("________________________________");

		//JsonUtils.parseCurrentExchangeRateJson(resultJson);

/*
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

		connection.setRequestMethod("GET");

		BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null){
			response.append(inputLine).append("\n");;

		}
		in.close();


		String jsonString = response.toString();

		//Map<String,String> jsonMap = new HashMap<String,String>();

		//JSONObject obj1 =

*/
		/*
		String jsonText = response.toString();
		JsonParser parser = new JsonParser() {
		JSONObject obj = new JSONObject(response.toString());
		List<String> list = new ArrayList<String>();
		JSONArray array = obj.getJSONArray("interests");
		//System.out.println(response.toString());
		*/
	//	JsonUtils.parseCurrentExchangeRateRestTemplate("https://api.monobank.ua/bank/

//https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date=20201021&json
//JsonUtils.parseCurrentExchangeRateJson("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange","25.11.2020",);
String BASE_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";
String urlXml = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date=20201122&xml";

String dateStr = "20201130";
String currency = "USD";


			String url = String.format("%s?date=%s&xml", BASE_URL, dateStr);

			List<Map<String, Object>> ratesData = new ArrayList<>();

		//Map<String, Object> hashMap = new HashMap<>();
		//ratesData.add((Map<String, Object>) hashMap.put(rateProp.getNodeName(),rateProp.getTextContent()));
		String currencyValue = "USD";

		//try (FileWriter writer = new FileWriter("Rates.xml", false)) {
		try{
			HttpResponse<String> response = Unirest.get(url)
					.queryString("date", dateStr).asString();
		//	writer.write(response.getBody());
		//	writer.flush();
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			//File inputFile = new File("Rates.xml");
			//Document document = documentBuilder.parse(inputFile);
			InputSource input = new InputSource(new StringReader(response.getBody()));
			Document document = documentBuilder.parse(input);
			document.getDocumentElement().normalize();

			NodeList nList = document.getElementsByTagName("currency");

			for (int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement
							.getElementsByTagName("cc")
							.item(0)
							.getTextContent().equals(currencyValue)) {
						System.out.println("r030 = "+ eElement.getElementsByTagName("r030").item(0).getTextContent());
						System.out.println("cc" + eElement.getElementsByTagName("cc").item(0).getTextContent());
						System.out.println("rate" + eElement.getElementsByTagName("rate").item(0).getTextContent());
						System.out.println("exchangedate" + eElement.getElementsByTagName("exchangedate").item(0).getTextContent());
					}
				}

			}


/*
			for (int i = 0; i < nList.getLength(); i++) {
				Node nCurrency = nList.item(i);  // дочерний узел
				if (nCurrency.getNodeType() != Node.TEXT_NODE) {
					NodeList currencyList = nCurrency.getChildNodes();
					for(int j = 0; j < currencyList.getLength(); j++) {
						Node currencyProp = currencyList.item(j);
						if (currencyProp.getNodeType() == Node.ELEMENT_NODE) {
						//	System.out.println(currencyProp.getNodeName() + ":" + currencyProp.getChildNodes().item(0).getTextContent());
							if (currencyProp.getNodeName() == "cc" && currencyProp.getChildNodes().item(0).getTextContent().equals(currencyValue)) {
								Node currencyN = currencyProp.getPreviousSibling();
								System.out.println(currencyProp.getNodeName() + ":" + currencyProp.getChildNodes().item(0).getTextContent());
								Map<String, Object> hashMap = new HashMap<>();
								hashMap.put(currencyProp.getNodeName(), currencyProp.getChildNodes().item(0).getTextContent());
								ratesData.add(hashMap);
								break;
							}
						}
						}

				}

			}
*/
			//NBURate rate = new NBURate(null, ratesData);

			//RateMapper.from(rate);


			//Document document = documentBuilder.parse(response.getBody());

			//Node root = document.getDocumentElement();

			//;

/*
			NodeList exchangeRates = root.getChildNodes();
			for (int i =0; i < exchangeRates.getLength(); i++){
				Node rate = exchangeRates.item(i);
				if (rate.getNodeType() != Node.TEXT_NODE) { // Если нода не текст, то это currency- заходим внутрь
					NodeList ratesProps = rate.getChildNodes();
					for (int j =0; j < ratesProps.getLength(); j++){
						Node rateProp = ratesProps.item(j);

						Map<String, Object> hashMap = new HashMap<>();
						ratesData.add((Map<String, Object>) hashMap.put(rateProp.getNodeName(),rateProp.getTextContent()));
					}

				}
*/


			} catch (SAXException | UnirestException | ParserConfigurationException e) {
			e.printStackTrace();

		}

		//returnResult(ratesData);


		//catch (ParserConfigurationException | SAXException | UnirestException| IOException e) {
		//	e.printStackTrace();
		//}


		System.out.println("Main program is finished");
	}

	private static List<String> getCurrencyRateByCurrency(Document doc, XPath xpath, String сс) {
		List<String> list = new ArrayList<>();
		try {
			XPathExpression xPathExpression = xpath.compile(
					"/exchange/currency[сс='" + сс + "']/rate/text()"
			);
			NodeList nodesList = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodesList.getLength(); i++)
				list.add(nodesList.item(i).getNodeValue());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return list;
	}



	public static RateView returnResult(List<Map<String, Object>> ratesData){
	return new RateView("TEST",
			"1000",
			"today",
			ratesData);
}

}
