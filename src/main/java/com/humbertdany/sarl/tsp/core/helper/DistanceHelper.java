package com.humbertdany.sarl.tsp.core.helper;

import java.util.HashMap;

public class DistanceHelper extends AHelper {

	private HashMap<String, Double> calculatedDistance = new HashMap<>();

	private DistanceHelper(){

	}

	private double euclideanDistance(double x1, double y1, double x2, double y2) {
		final String mapKey = x1 + " " + y1 + " " + x2 + " " + y2;
		if(calculatedDistance.containsKey(mapKey)){
			return calculatedDistance.get(mapKey);
		} else {
			final double xDiff = x2 - x1;
			final double yDiff = y2 - y1;
			final Double d =  Math.abs((Math.sqrt((xDiff * xDiff) + (yDiff * yDiff))));
			calculatedDistance.put(mapKey, d);
			return d;
		}
	}

	// Static Calls

	private static final DistanceHelper dh = new DistanceHelper();

	public static double calculateEuclideanDistance(double x1, double y1, double x2, double y2) {
		return dh.euclideanDistance(x1, y1, x2, y2);
	}

}
