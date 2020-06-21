package com.amazon.amazon.service;

import com.amazon.amazon.model.Estimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EstimateService {

    @Autowired
    ProductServiceImpl productServiceImpl;

    public Estimate getEstimate(String product) {
        List<String> allProductsReturned =
                getSubstringProductList(product).stream()
                        .map(word -> CompletableFuture.supplyAsync(
                                () -> productServiceImpl.getProductList(word)))
                        .map(CompletableFuture::join)
                        .map(productServiceImpl::convertJsonArrayToList)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

        return new Estimate(product, getFinalScore(allProductsReturned.size(), getSearchedProductOccurrence(allProductsReturned, product)));
    }

    private List<String> getSubstringProductList(String product) {
        List<String> productList = new ArrayList<>();
        for (int i = 0; i < product.length(); i++) {
            productList.add(product.substring(0, i + 1));
        }
        return productList;
    }

    private float getFinalScore(int totalReturnedProducts, long productOccurrence) {
        float percent = (float) 100 / totalReturnedProducts;
        return percent * productOccurrence;
    }

    private long getSearchedProductOccurrence(List<String> subStringList, String keyWord) {
        return subStringList.stream().filter(s -> s.contains(keyWord)).count();
    }

}
