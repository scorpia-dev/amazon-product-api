package com.amazon.amazon.model;


public class Estimate {

	private String keyWord;
	private float score;

	public Estimate(String newKeyWord, float newScore) {
		keyWord = newKeyWord;
		score = newScore;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public float getScore() {
		return score;
	}
}
