package com.humbertdany.sarl.tsp.solver.aco.ui;

import com.humbertdany.sarl.tsp.core.ui.JfxController;
import com.humbertdany.sarl.tsp.solver.aco.AntColonyTspSolver;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class AcoGuiController extends JfxController {

	@FXML
	private CheckBox checkbox;
	@FXML
	private Slider slider;

	private AntColonyTspSolver solver;

	public AcoGuiController(final AntColonyTspSolver solver){
		this.solver = solver;

		checkbox = new CheckBox("Should use test");

		slider = new Slider(0, 10000, this.getParams().getMsBetweenTick());
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setSnapToTicks(true);

	}

	private AcoParameters getParams(){
		return this.solver.getParameters();
	}

	public void build(final Pane pane){
		pane.getChildren().clear();

		// binds elements
		this.bindSlider(slider, (newValue) -> getParams().setOmega(newValue.intValue()));
		this.bindCheckbox(checkbox, (newValue) -> getParams().setChecked(newValue));

		final GridPane iPane = new GridPane();
		iPane.add(slider, 1, 1);
		iPane.add(checkbox, 1, 2);
		pane.getChildren().add(iPane);
	}

}
