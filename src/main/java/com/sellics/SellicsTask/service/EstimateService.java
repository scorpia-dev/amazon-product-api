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

		for (int i = 0; i < keyWord.length(); i++) {

			String keyWordSubString = keyWord.substring(0, i + 1);
			JsonArray subStringJsonArray = getProductList(keyWordSubString, startTime);
			Set<String> subStringList = convertToArrayList(subStringJsonArray);

			score = getScore(subStringList, keyWord) + score;
		}

		finalScore = (100 / (keyWord.length() * 10)) * score;

		return new Estimate(keyWord, finalScore);

	}

	private JsonArray getProductList(String keyword, Long startTime) throws MalformedURLException, IOException {
		JsonArray jsonArray = null;
		
		if (timeOutCheck(startTime)) {
			
			String sURL = "https://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q="
					+ keyword;

			URL url = new URL(sURL);
			URLConnection request = url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();

			JsonElement jsonElement = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			jsonArray = jsonElement.getAsJsonArray();
		}
		return jsonArray;

	}

	private boolean timeOutCheck(Long startTime) {
		return (System.currentTimeMillis() < startTime + 10000);
	}

	private int getScore(Set<String> subStringList, String keyWord) {
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
			TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>() {
			};
			ArrayList<String> productList = new Gson().fromJson(jsonArray.get(1), token.getType());
			for (int i = 0; i < productList.size(); i++) {
				subStringList.add(productList.get(i));
			}
		}
		return subStringList;
	}

}
