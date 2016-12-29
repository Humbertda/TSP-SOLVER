package com.humbertdany.sarl.tsp.ui.mainui;

public class PathEntry {

	private double cost;

	private CityEntry from;

	private CityEntry to;

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public CityEntry getFrom() {
		return from;
	}

	public void setFrom(CityEntry from) {
		this.from = from;
	}

	public CityEntry getTo() {
		return to;
	}

	public void setTo(CityEntry to) {
		this.to = to;
	}
}
