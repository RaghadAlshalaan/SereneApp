package com.ksu.serene.controller.liveChart.draw.data;

public class InputData {

	private double value;
	private long millis;

	public InputData(double v) {
	}

	public InputData(int value) {
		this.value = value;
	}

	public InputData(int value, long millis) {
		this.value = value;
		this.millis = millis;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getMillis() {
		return millis;
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}
}
