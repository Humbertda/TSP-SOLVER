package com.humbertdany.sarl.tsp.core.ui;

import javafx.scene.control.Slider;

public class MSlider extends Slider {
	
	public MSlider(double min, double max, double value){
		super(min, max, value);
		this.setShowTickLabels(true);
		this.setShowTickMarks(true);
	}

}
