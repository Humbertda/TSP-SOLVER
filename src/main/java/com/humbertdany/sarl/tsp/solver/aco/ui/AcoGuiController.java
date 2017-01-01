package com.humbertdany.sarl.tsp.solver.aco.ui;

import com.humbertdany.sarl.tsp.core.ui.JfxController;
import com.humbertdany.sarl.tsp.core.ui.MCheckBox;
import com.humbertdany.sarl.tsp.core.ui.MGridPane;
import com.humbertdany.sarl.tsp.core.ui.MSlider;
import com.humbertdany.sarl.tsp.solver.aco.AntColonyTspSolver;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class AcoGuiController extends JfxController {

	final private MCheckBox checkboxTia;
	final private MCheckBox checkboxMmas;
	
	final private MSlider timeMsSlider;

	final private MSlider phEvaporation;
	final private MSlider phInitialLevel;
	final private MSlider phMinMaxRatio;

	final private MSlider alphaSlider;
	final private MSlider betaSlider;
	
	final private ChoiceBox<String> antNumberChoicebox;
	
	/**
	 * Solver reference
	 */
	private AntColonyTspSolver solver;
	

	public AcoGuiController(final AntColonyTspSolver solver){
		this.solver = solver;

		checkboxTia = new MCheckBox("TIA", getParams().isTia());
		checkboxMmas = new MCheckBox("MMAS", getParams().isMmas());

		timeMsSlider = new MSlider(AcoParameters.MS_TICKS_MIN_VALUE/1000.0, 5, getParams().getMsBetweenTick()/1000);
		
		phEvaporation = new MSlider(0, 1, getParams().getPhEvaporation());
		phInitialLevel = new MSlider(0, 10, getParams().getPhInitialLevel());
		phMinMaxRatio = new MSlider(0, 1, getParams().getPhMinMaxRatio());
		
		alphaSlider = new MSlider(0.0, 1.0, getParams().getAlpha());
		betaSlider = new MSlider(1.0, 10.0, getParams().getBeta());
		
		antNumberChoicebox = new ChoiceBox<String>(FXCollections.observableArrayList(
		    "Few", "Normal", "Alot")
		);
		antNumberChoicebox.setValue("Normal");

	}

	private AcoParameters getParams(){
		return this.solver.getParameters();
	}

	public void build(final Pane pane){
		// Empty the pane first
		pane.getChildren().clear();

		// Bind all the controllers
		bindSlider(timeMsSlider, (newValue) -> {
			Double valueInMs = (new Double(newValue.doubleValue()*1000));
			getParams().setMsBetweenTick(valueInMs.intValue());
		});
		bindCheckbox(checkboxTia, (newValue) -> getParams().setTia(newValue));
		bindCheckbox(checkboxMmas, (newValue) -> getParams().setMmas(newValue));
		bindSlider(phEvaporation, (newValue) -> getParams().setPhEvaporation(newValue.doubleValue()));
		bindSlider(phInitialLevel, (newValue) -> getParams().setPhInitialLevel(newValue.doubleValue()));
		bindSlider(phMinMaxRatio, (newValue) -> getParams().setPhMinMaxRatio(newValue.doubleValue()));
		bindSlider(alphaSlider, (newValue) -> getParams().setAlpha(newValue.doubleValue()));
		bindSlider(betaSlider, (newValue) -> getParams().setBeta(newValue.doubleValue()));
		bindStringChoicebox(antNumberChoicebox, (newValue) ->getParams().setAntsNumber(newValue));

		// Generate all the nodes in list order
		final ArrayList<Node> nodes = new ArrayList<>();
		nodes.add(wrapControl(timeMsSlider, "Time between ticks (in S)"));
		nodes.add(wrapControl(antNumberChoicebox, "Number of Ants / Vertex")); 
		nodes.add(checkboxTia);
		nodes.add(checkboxMmas);
		nodes.add(wrapControl(phEvaporation, "Pheromone evaporation"));
		nodes.add(wrapControl(phInitialLevel, "Pheromone initial level"));
		nodes.add(wrapControl(phMinMaxRatio, "Pheromone min/max ratio"));
		nodes.add(wrapControl(alphaSlider, "α"));
		nodes.add(wrapControl(betaSlider, "β"));
		
		// Add elements to the final pane
		final MGridPane iPane = new MGridPane();
		int counter = 0;
		for(Node n : nodes){
			iPane.add(n, 0, counter);
			counter++;
		}
		pane.getChildren().add(iPane);
	}

}
