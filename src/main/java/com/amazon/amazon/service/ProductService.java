package com.amazon.amazon.service;

import com.google.gson.JsonArray;

import java.util.List;

public interface ProductService {

    JsonArray getProductList(String keyword);

    List<String> convertJsonArrayToList(JsonArray jsonArray);

}
