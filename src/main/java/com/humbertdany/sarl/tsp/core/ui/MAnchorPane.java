package com.humbertdany.sarl.tsp.core.ui;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class MAnchorPane extends AnchorPane {

	public MAnchorPane(){
		this.setFocusTraversable(true);
	}

	/**
	 * Add a node to the Pane with the selected margin
	 * Called only programmatically, it is usually used to
	 * add a single Node on the Pane which will take the
	 * entire free space (- the margins)
	 * @param n the node to add in the pane
	 * @param margin the selected margin as double
	 * @return true if it worked, false otherwise
	 */
	public boolean add(final Node n, final double margin){
		final boolean res = this.getChildren().add(n);
		MAnchorPane.setBottomAnchor(n, margin);
		MAnchorPane.setTopAnchor(n, margin);
		MAnchorPane.setLeftAnchor(n, margin);
		MAnchorPane.setRightAnchor(n, margin);
		return res;
	}

	/**
	 * @see public boolean add(final Node n, final double margin);
	 * @param n : The node to add in the pane
	 * @return public boolean add(final Node n, final double margin)
	 */
	public boolean add(final Node n){
		return this.add(n, 0);
	}

}
