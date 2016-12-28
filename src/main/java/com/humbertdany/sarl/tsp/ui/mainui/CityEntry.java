package com.humbertdany.sarl.tsp.ui.mainui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;

public class CityEntry {

	@JsonInclude
	double x;

	@JsonInclude
	double y;

	public CityEntry(){
		this(0,0);
	}

	public CityEntry(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	@JsonInclude
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	@JsonInclude
	public void setY(double y) {
		this.y = y;
	}

	public String toString(){
		return "{x=" + x + "; y=" + y + "}";
	}

	@JsonIgnore
	public VertexInfo makeVertexInfo(){
		return new VertexInfo(x, y);
	}
}