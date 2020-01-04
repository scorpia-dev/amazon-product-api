package com.sellics.SellicsTask.model;

public class Estimate {

	private String keyWord;
	private int score;

	public Estimate(String keyWord, int score) {
		this.setKeyWord(keyWord);
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}



}
