package com.sellics.SellicsTask.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sellics.SellicsTask.model.Estimate;

@Service
public class EstimateService {

	public Estimate getEstimate(String keyWord)
			throws JsonIOException, JsonSyntaxException, IOException, InterruptedException {

		long startTime = System.currentTimeMillis();
		int finalScore = 0;
		int score = 0;
		
		if (isKeyWordValidInput(keyWord)) {

			for (int i = 0; i < keyWord.length(); i++) {

				String keyWordSubString = keyWord.substring(0, i + 1);

				if (underTenSeconds(startTime)) {

					JsonArray subStringJsonArray = getProductList(keyWordSubString);
					Set<String> subStringList = convertToArrayList(subStringJsonArray);
					score = getSubStringScore(subStringList, keyWord) + score;

				} else {
					throw new RuntimeException("microservice only has an SLA of 10 seconds for a request round-trip​.");
				}
			}
		} else {
			throw new RuntimeException("Invalid input, key word must start with Alpha numeric character");
		}

		finalScore = getFinalScore(keyWord, score);
		
		return new Estimate(keyWord, finalScore);

	}

	private boolean isKeyWordValidInput(String keyWord) {
		String firstChar = keyWord.substring(0, 1); 
		return (StringUtils.isAlphanumeric(firstChar));
	}

	private int getFinalScore(String keyWord, int score) {
		return (100 / (keyWord.length() * 10)) * score;
	}

	private JsonArray getProductList(String keyword) throws MalformedURLException, IOException {
		String sURL = "https://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q="
				+ keyword;

		URL url = new URL(sURL);
		URLConnection request = url.openConnection();
		request.connect();
		JsonParser jp = new JsonParser();

		JsonElement jsonElement = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		return jsonElement.getAsJsonArray();
	}

	private boolean underTenSeconds(Long startTime) {
		return (System.currentTimeMillis() < startTime + 10000);
	}

	private int getSubStringScore(Set<String> subStringList, String keyWord) {
		int result = 0;
		for (String s : subStringList) {
			if (s.contains(keyWord)) {
				result++;
			}
		}
		return result;
	}

	private Set<String> convertToArrayList(JsonArray jsonArray) {
		Set<String> subStringList = new HashSet<String>();
		if (jsonArray != null) {
			
			TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>() {};
			
			ArrayList<String> productList = new Gson().fromJson(jsonArray.get(1), token.getType());
			
			for (int i = 0; i < productList.size(); i++) {
				subStringList.add(productList.get(i));
			}
		}
		return subStringList;
	}

}
