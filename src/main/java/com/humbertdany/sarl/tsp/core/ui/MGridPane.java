package com.humbertdany.sarl.tsp.core.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class MGridPane extends GridPane {

	public MGridPane(){
		this.setHgap(0);
		this.setVgap(0);
		this.setAlignment(Pos.CENTER);
		this.setFocusTraversable(true);
	}

}
