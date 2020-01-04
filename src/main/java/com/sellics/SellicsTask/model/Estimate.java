package com.sellics.SellicsTask.model;

public class Estimate {

	private String keyWord;
	private int score;

	public Estimate(String newKeyWord, int newScore) {
		keyWord = newKeyWord;
		score =newScore;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public int getScore() {
		return score;
	}
}
