package com.sellics.SellicsTask.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

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

		if (isKeyWordValidInput(keyWord)) {

			long startTime = System.currentTimeMillis();
			int finalScore = 0;
			int keyWordOccurance = 0;

			for (int i = 0; i < keyWord.length(); i++) {

				String keyWordSubString = keyWord.substring(0, i + 1);

				if (underTenSeconds(startTime)) {

					JsonArray subStringJsonArray = getProductList(keyWordSubString);
					List<String> subStringProductList = convertJsonArrayToList(subStringJsonArray);

					keyWordOccurance = getKeyWordOccuranceInSubString(subStringProductList, keyWord) + keyWordOccurance;

				} else {
					throw new RuntimeException("microservice only has an SLA of 10 seconds for a request round-trip​.");
				}
			}

			finalScore = getFinalScore(keyWord, keyWordOccurance);
			return new Estimate(keyWord, finalScore);
			
		} else {
			throw new RuntimeException("Invalid input, key word must start with Alpha numeric character");
		}
	}

	private boolean isKeyWordValidInput(String keyWord) {
		String firstChar = keyWord.substring(0, 1);
		return (StringUtils.isAlphanumeric(firstChar));
	}

	private int getFinalScore(String keyWord, int keyWordOccurance) {
		return (100 / (keyWord.length() * 10)) * keyWordOccurance;
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

	private int getKeyWordOccuranceInSubString(List<String> subStringList, String keyWord) {
		int result = 0;
		for (String s : subStringList) {
			if (s.contains(keyWord)) {
				result++;
			}
		}
		return result;
	}

	private List<String> convertJsonArrayToList(JsonArray jsonArray) {
		List<String> subStringProductList = new ArrayList<String>();
		if (jsonArray != null) {

			TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>() {
			};
			subStringProductList = new Gson().fromJson(jsonArray.get(1), token.getType());

		}
		return subStringProductList;
	}

}
