package com.amazon.amazon.service;

import com.amazon.amazon.model.Estimate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class EstimateService {

	public Estimate getEstimate(String keyWord) {

		if (isKeyWordValidInput(keyWord)) {

		//	long startTime = System.currentTimeMillis();


			List<String> keyWordList = new ArrayList<>();
			for (int i = 0; i < keyWord.length(); i++) {
				keyWordList.add(keyWord.substring(0, i + 1));
			}

			//	if (underTenSeconds(startTime)) {

			List<String> subStringProductList =
					keyWordList.stream()
					.map(word -> CompletableFuture.supplyAsync(
							() -> this.getProductList(word)))
							.map(CompletableFuture::join)
					.map(this::convertJsonArrayToList)
							.flatMap(List::stream)
							.collect(Collectors.toList());

					int totalReturnedProducts = subStringProductList.size();
					int keyWordOccurance = getKeyWordOccuranceInSubString(subStringProductList, keyWord);

//				} else
//					throw new RuntimeException("microservice only has an SLA of 10 seconds for a request round-trip​.");
//			//}

			return new Estimate(keyWord, getFinalScore(totalReturnedProducts, keyWordOccurance));

		} else {
			throw new RuntimeException("Invalid input, key word must start with Alpha numeric character");
		}
	}

		private boolean isKeyWordValidInput(String keyWord) {
		String firstChar = keyWord.substring(0, 1);
		return (StringUtils.isAlphanumeric(firstChar));
	}

	private float getFinalScore(int totalReturnedProducts, int keyWordOccurance) {
		float percent = (float) 100 / totalReturnedProducts;
		return percent * keyWordOccurance;
	}

	private JsonArray getProductList(String keyword) {
		try {

			String encodedKeyWord = URLEncoder.encode(keyword, "UTF-8");

			String sURL = "https://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q="
					+ encodedKeyWord;

			URL url = new URL(sURL);
			URLConnection request = url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();

			JsonElement jsonElement = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			return jsonElement.getAsJsonArray();
		}
	 catch (Exception e) {
		 throw new RuntimeException(e.toString());
	 }
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
