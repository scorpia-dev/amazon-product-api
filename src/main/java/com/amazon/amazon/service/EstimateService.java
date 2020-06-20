package com.amazon.amazon.service;

import com.amazon.amazon.model.Estimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EstimateService {

    @Autowired
    ProductServiceImpl productServiceImpl;

    public Estimate getEstimate(String product) {

        List<String> productList = new ArrayList<>();
        for (int i = 0; i < product.length(); i++) {
            productList.add(product.substring(0, i + 1));
        }

        List<String> allReturnProducts =
                productList.stream()
                        .map(word -> CompletableFuture.supplyAsync(
                                () -> productServiceImpl.getProductList(word)))
                        .map(CompletableFuture::join)
                        .map(productServiceImpl::convertJsonArrayToList)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

        long productOccurrence = getKeyWordOccurrenceInSubString(allReturnProducts, product);

        return new Estimate(product, getFinalScore(allReturnProducts.size(), productOccurrence));

    }

    private float getFinalScore(int totalReturnedProducts, long productOccurrence) {
        float percent = (float) 100 / totalReturnedProducts;
        return percent * productOccurrence;
    }

    private long getKeyWordOccurrenceInSubString(List<String> subStringList, String keyWord) {
        return subStringList.stream().filter(s -> s.contains(keyWord)).count();
    }

}
