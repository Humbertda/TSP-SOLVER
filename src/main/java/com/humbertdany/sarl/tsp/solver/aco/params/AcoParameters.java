package com.humbertdany.sarl.tsp.solver.aco.params;

import com.humbertdany.sarl.tsp.core.params.AApplicationParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class AcoParameters extends AApplicationParameters {

	/**
	 *
	 */
	private static final long serialVersionUID = -3291340246882818235L;
	
	public static final int MS_TICKS_MIN_VALUE = 500;

	private Boolean isTia = false;
	private Boolean isMmas = false;
	private int msBetweenTick = 1000;
	private double phEvaporation = 0.8;
	private double phInitialLevel = 1;
	private double phMinMaxRatio = 0.01;
	private double alpha = 0.1;
	private double beta =  9;
	private String antsNumber = "Normal";

	// -- constructors

	private AcoParameters() {
	}

	public static AcoParameters buildDefault(){
		return new AcoParameters();
	}

	// -- Serialize

	public String toString(){
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append( this.getClass().getName() );
		result.append( " Object {" );
		result.append(newLine);

		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		//print field names paired with their values
		for ( Field field : fields  ) {
			result.append("  ");
			try {
				result.append( field.getName() );
				result.append(": ");
				//requires access to private field:
				result.append( field.get(this) );
			} catch (IllegalAccessException e) {
				Logger logger =  LogManager.getLogger(this.getClass());
				logger.error("\n" + e.getMessage());
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}

	// -- getters & setters

	public Boolean isTia() {
		return isTia;
	}

	public void setTia(Boolean checked) {
		isTia = checked;
		notify(this);
	}

	public Boolean isMmas() {
		return isMmas;
	}

	public void setMmas(Boolean checked) {
		isMmas = checked;
		notify(this);
	}

	public int getMsBetweenTick(){
		return msBetweenTick;
	}

	public void setMsBetweenTick(int ms){
		if(ms < MS_TICKS_MIN_VALUE) ms = MS_TICKS_MIN_VALUE;
		msBetweenTick = ms;
		notify(this);
	}

	public double getPhEvaporation() {
		return phEvaporation;
	}

	public void setPhEvaporation(double phEvaporation) {
		this.phEvaporation = phEvaporation;
		notify(this);
	}

	public double getPhInitialLevel() {
		return phInitialLevel;
	}

	public void setPhInitialLevel(double v) {
		this.phInitialLevel = v;
		notify(this);
	}

	public double getPhMinMaxRatio() {
		return phMinMaxRatio;
	}

	public void setPhMinMaxRatio(double e) {
		this.phMinMaxRatio = e;
		notify(this);
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
		notify(this);
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
		notify(this);
	}

	public String getAntsNumber() {
		return antsNumber;
	}

	public void setAntsNumber(String antsNumber) {
		this.antsNumber = antsNumber;
		notify(this);
	}




}
