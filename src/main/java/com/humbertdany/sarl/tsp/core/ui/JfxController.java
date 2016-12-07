package com.humbertdany.sarl.tsp.core.ui;

import javafx.scene.control.*;

abstract public class JfxController {

	public interface Bindable<T> {
		void setValue(final T newValue);
	}

	final protected void bindSlider(final Slider slider, final Bindable<Number> method){
		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			method.setValue(newValue);
		});
	}

	final protected void bindCheckbox(final CheckBox checkbox, final Bindable<Boolean> method){
		checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			method.setValue(newValue);
		});
	}

}
