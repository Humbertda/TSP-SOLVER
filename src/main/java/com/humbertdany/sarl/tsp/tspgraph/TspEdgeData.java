package com.humbertdany.sarl.tsp.tspgraph;

import com.google.common.util.concurrent.AtomicDouble;
import com.humbertdany.sarl.tsp.core.graph.EdgeData;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;

public class TspEdgeData extends EdgeData {

	private AtomicDouble pheromoneLevel;
	
	private AcoParameters params; 

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
	
	public void setAcoParameters(AcoParameters p){
		this.params = p;
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
				// TODO Take care about params : min max ratio
				// if(pheromoneLevel.get() > params.getPhMinMaxRatio())
				break;
			}
		}
	}

	private double weightDecay(double current, double weightUpdate) {
	    return weightUpdate + (1d - params.getPhEvaporation()) * current;
	}
	
	// Serialize

	public String toString(){
		return "PH="+pheromoneLevel;
	}

}
