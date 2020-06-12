package com.amazon.amazon.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Estimate {

	@Getter
	private final String keyWord;

	@Getter
	private final float score;

}
