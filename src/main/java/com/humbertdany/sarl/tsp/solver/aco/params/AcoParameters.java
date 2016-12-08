package com.humbertdany.sarl.tsp.solver.aco.params;

import com.humbertdany.sarl.tsp.core.params.AApplicationParameters;

public class AcoParameters extends AApplicationParameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3291340246882818235L;
	
	private Integer omega;
	private Boolean isChecked;
	private int msBetweenTick = 1000;

	// -- constructors

	private AcoParameters() {
	}

	public static AcoParameters buildDefault(){
		final AcoParameters p = new AcoParameters();
		p.setOmega(50);
		return p;
	}

	// -- Serialize

	public String toString(){
		return "AApplicationParameters: omega=" + this.getOmega() + " isChecked="+ this.getChecked()
				+ " msBetweenTick="+ this.getMsBetweenTick();
	}

	// -- getters & setters

	public Integer getOmega() {
		return omega;
	}

	public void setOmega(Integer omega) {
		this.omega = omega;
		notify(this);
	}

	public Boolean getChecked() {
		return isChecked;
	}

	public void setChecked(Boolean checked) {
		isChecked = checked;
		notify(this);
	}
	
	public int getMsBetweenTick(){
		return msBetweenTick;
	}

	public void setMsBetweenTick(int ms){
		msBetweenTick = ms;
	}

}
