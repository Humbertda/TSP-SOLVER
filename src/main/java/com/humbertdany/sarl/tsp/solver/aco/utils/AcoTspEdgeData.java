package com.humbertdany.sarl.tsp.solver.aco.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.util.concurrent.AtomicDouble;
import com.humbertdany.sarl.tsp.tspgraph.TspEdgeData;

public class AcoTspEdgeData extends TspEdgeData {

	public AcoTspEdgeData(){
		this.pheromoneLevel = 0.0;
	}

	@JsonInclude
	private Double pheromoneLevel;

	@JsonInclude
	public Double getPheromoneLevel() {
		return pheromoneLevel;
	}

	@JsonInclude
	public void setPheromoneLevel(Double pheromoneLevel) {
		this.pheromoneLevel = pheromoneLevel;
	}

	@JsonIgnore
	public void adjustWeight(double weightUpdate, double pheromoneEvaporation) {
		while (true) {
			final AtomicDouble phLevelAto = new AtomicDouble(this.pheromoneLevel);
			final double val = phLevelAto.get();
			final double result = weightDecay(val, weightUpdate, pheromoneEvaporation);
			boolean success = false;
			if (result >= 0.0d) {
				success = phLevelAto.compareAndSet(val, result);
			} else {
				success = phLevelAto.compareAndSet(val, 0);
			}
			if (success) {
				this.pheromoneLevel = phLevelAto.doubleValue();
				break;
			}
		}
	}

	@JsonIgnore
	private double weightDecay(double current, double weightUpdate, double pheromoneEvaporation) {
		return weightUpdate + (1d - pheromoneEvaporation) * current;
	}
}
