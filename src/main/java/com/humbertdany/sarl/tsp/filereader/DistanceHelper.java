package com.humbertdany.sarl.tsp.filereader;

public class DistanceHelper {

	public static double calculateEuclideanDistance(double x1, double y1, double x2, double y2) {
		final double xDiff = x2 - x1;
		final double yDiff = y2 - y1;
		return Math.abs((Math.sqrt((xDiff * xDiff) + (yDiff * yDiff))));
	}

	public static int calculateRoundEuclideanDistance(double x1, double y1, double x2, double y2){
		Double d = calculateEuclideanDistance(x1, y1, x2, y2);
		return d.intValue();
	}

}
