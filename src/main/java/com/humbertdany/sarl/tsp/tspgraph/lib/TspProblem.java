package com.humbertdany.sarl.tsp.tspgraph.lib;

public class TspProblem {

	private final String name;
	private final String desc;

	public TspProblem(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}
}
