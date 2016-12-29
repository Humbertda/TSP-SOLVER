package com.humbertdany.sarl.tsp.core.ui;

import com.humbertdany.sarl.tsp.ui.icon.AppIconLib;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

abstract public class JfxController {

	protected Stage stage;
	private Logger logger;

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

	final protected void bindButton(final Button btn, final EventHandler<ActionEvent> evtHandler){
		btn.setOnAction(evtHandler);
	}

	private void initLogger(){
		logger = LogManager.getLogger(this.getClass());
	}

	protected final void log(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.info("\n" + o.toString());
	}

	protected final void logError(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.error("\n" + o.toString());
	}

	public JfxController openModal(final String pathToFxml, final String modalTitle) throws IOException {
		return this.openModal(pathToFxml, modalTitle, 600, 400);
	}

	/**
	 * How to use :
	 *    TspPopupController controller = (TspPopupController) this.openModal("/tspChooserUi/popup.fxml", "Graph Selector");
	 *    controller.addPopupObserver(this);
	 * controller.getStage().showAndWait();
	 * @param pathToFxml the path to the fxml file (usually in the resources folder)
	 * @param modalTitle the modal title displayed
	 * @param minWidth the minimum width
	 * @param minHeight the minimum height
	 * @return an instance of JfxController ; which can be casted to the Fxml Controller Class
	 * @throws IOException in case the file cannot be read
	 */
	public JfxController openModal(final String pathToFxml, final String modalTitle, final int minWidth, final int minHeight) throws IOException {
		final Stage stage = new Stage();
		final FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToFxml));
		final Parent rootPopup = loader.load();
		JfxController controller = loader.getController();
		controller.setStage(stage);
		stage.setScene(new Scene(rootPopup));
		stage.setTitle(modalTitle);
		stage.setMinHeight(minHeight);
		stage.setMinWidth(minWidth);
		stage.getIcons().add(AppIconLib.get250Image());
		stage.initModality(Modality.APPLICATION_MODAL);
		return controller;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
