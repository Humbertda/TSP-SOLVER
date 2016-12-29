package com.humbertdany.sarl.tsp.ui.mainui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;

public class CityEntry {

	@JsonInclude
	double x;

	@JsonInclude
	double y;

	@JsonInclude
	String name;

	@JsonInclude
	int id;

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

	@JsonInclude
	public double getY() {
		return y;
	}

	@JsonInclude
	public String getName() {
		return name;
	}

	@JsonInclude
	public void setName(String name) {
		this.name = name;
	}

	@JsonInclude
	public int getId() {
		return id;
	}

	@JsonInclude
	public void setId(int id) {
		this.id = id;
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