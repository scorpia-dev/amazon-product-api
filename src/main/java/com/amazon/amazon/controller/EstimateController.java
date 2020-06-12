package com.amazon.amazon.controller;

import com.amazon.amazon.model.Estimate;
import com.amazon.amazon.service.EstimateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EstimateController {

    @Autowired
    EstimateService estimateService;

    @GetMapping("/estimate/{product}")
    public Estimate getEstimate(@PathVariable String product) {

        if (!StringUtils.isAlphanumeric(product.substring(0, 1))) {
            throw new IllegalArgumentException("Invalid input, key word must start with Alpha numeric character");
        }

        return estimateService.getEstimate(product);
    }

}
