package com.humbertdany.sarl.tsp.core.helper;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.math3.util.FastMath;

public class DistanceHelper extends AHelper {

	private ConcurrentHashMap <String, Double> calculatedDistance = new ConcurrentHashMap <>();

	private DistanceHelper(){

	}

	private double euclideanDistance(double x1, double y1, double x2, double y2) {
		final String part1 = x1 + " " + y1;
		final String part2 = x2 + " " + y2; 
		if(calculatedDistance.contains(part1 + " " + part2)){
			return calculatedDistance.get(part1 + " " + part2); 
		} else if (calculatedDistance.contains(part2 + " " + part1)){
			return calculatedDistance.get(part2 + " " + part1); 
		} else {
			return this.computeDistance((part1 + " " + part2), x1, y1, x2, y2);
		}
	}
	
	private double computeDistance(String key, double x1, double y1, double x2, double y2) {
		final double xDiff = x2 - x1;
		final double yDiff = y2 - y1;
		final Double d =  FastMath.abs((Math.sqrt((xDiff * xDiff) + (yDiff * yDiff))));
		calculatedDistance.put(key, d);
		return d;
	}

	// Static Calls

	private static final DistanceHelper dh = new DistanceHelper();

	public static double calculateEuclideanDistance(double x1, double y1, double x2, double y2) {
		return dh.euclideanDistance(x1, y1, x2, y2);
	}

}
