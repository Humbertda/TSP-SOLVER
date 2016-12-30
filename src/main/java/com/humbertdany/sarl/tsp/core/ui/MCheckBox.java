package com.humbertdany.sarl.tsp.core.ui;

import javafx.scene.control.CheckBox;

public class MCheckBox extends CheckBox {
	
	public MCheckBox(String label, boolean defaultValue){
		super(label);
		this.setSelected(defaultValue);
	}

}
