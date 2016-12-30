package com.humbertdany.sarl.tsp.tspgraph;

import com.google.common.util.concurrent.AtomicDouble;
import com.humbertdany.sarl.tsp.core.graph.EdgeData;

public class TspEdgeData extends EdgeData {

	private AtomicDouble pheromoneLevel;
	
	private double pheromonePersistence; 

	public Double getPheromoneLevel() {
		if(pheromoneLevel != null){
			return pheromoneLevel.doubleValue();
		} else {
			return null;
		}
	}

	public void setPheromoneLevel(Double pheromoneLevel) {
		this.pheromoneLevel = new AtomicDouble(pheromoneLevel);
	}
	
	public void setPheromonePersistence(Double d){
		this.pheromonePersistence = d;
	}

	public void adjustWeight(double weightUpdate) {
		while (true) {
			final double val = pheromoneLevel.get();
			final double result = weightDecay(val, weightUpdate);
			boolean success = false;
			if (result >= 0.0d) {
				success = pheromoneLevel.compareAndSet(val, result);
			} else {
				success = pheromoneLevel.compareAndSet(val, 0);
			}
			if (success) {
				break;
			}
		}
	}

	private double weightDecay(double current, double weightUpdate) {
	    return weightUpdate + (1d - this.pheromonePersistence) * current;
	}
	
	// Serialize

	public String toString(){
		return "PH="+pheromoneLevel;
	}

}
