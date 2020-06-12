package com.amazon.amazon.service;

import com.amazon.amazon.model.Estimate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EstimateService {

	public Estimate getEstimate(String product) {

			List<String> productList = new ArrayList<>();
			for (int i = 0; i < product.length(); i++) {
				productList.add(product.substring(0, i + 1));
			}

			List<String> allReturnProducts =
					productList.stream()
							.map(word -> CompletableFuture.supplyAsync(
									() -> this.getProductList(word)))
							.map(CompletableFuture::join)
							.map(this::convertJsonArrayToList)
							.flatMap(List::stream)
							.collect(Collectors.toList());

			long productOccurrence = getKeyWordOccurrenceInSubString(allReturnProducts, product);

			return new Estimate(product, getFinalScore(allReturnProducts.size(), productOccurrence));

	}

	private float getFinalScore(int totalReturnedProducts, long productOccurrence) {
		float percent = (float) 100 / totalReturnedProducts;
		return percent * productOccurrence;
	}

	private JsonArray getProductList(String keyword) {
		try {

			String encodedKeyWord = URLEncoder.encode(keyword, "UTF-8");

			String sURL = "https://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q="
					+ encodedKeyWord;

			URL url = new URL(sURL);
			URLConnection request = url.openConnection();
			request.setConnectTimeout(10000);
			request.setReadTimeout(10000);
			request.connect();

			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
			return jsonElement.getAsJsonArray();
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}

	private long getKeyWordOccurrenceInSubString(List<String> subStringList, String keyWord) {
		return subStringList.stream().filter(s -> s.contains(keyWord)).count();
	}

	private List<String> convertJsonArrayToList(JsonArray jsonArray) {
		List<String> subStringProductList = new ArrayList<>();
		if (jsonArray != null) {
			TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>() {
			};
			subStringProductList = new Gson().fromJson(jsonArray.get(1), token.getType());
		}
		return subStringProductList;
	}
}
