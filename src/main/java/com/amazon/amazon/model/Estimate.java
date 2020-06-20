package com.amazon.amazon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Estimate {

    @Getter
    private final String product;

    @Getter
    private final float score;

}
