package com.humbertdany.sarl.tsp.ui.tsppopup;

import com.humbertdany.sarl.tsp.core.ui.ClassicHyperlink;
import com.humbertdany.sarl.tsp.core.ui.JfxController;
import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.filereader.TspProblemReader;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspCommonLibrary;
import com.humbertdany.utils.HyperlinkRegister;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TspPopupController extends JfxController {

	// Free input

	@FXML
	private TextArea freeInputProblem;
	@FXML
	private Button loadFreeInputBtn;


	// Problem Library

	@FXML
	private ListView<TsplibListItem> listViewProblemLibrary;
	@FXML
	private Button loadProblemLibBtn;

	// File loader

	@FXML
	private TextFlow descLoadFileLabel;
	@FXML
	private Button openFileChooserBtn;
	@FXML
	private TextField pathToFileLabel;
	@FXML
	private Button loadOpenFileBtn;

	// Global

	private TspGraph temporarySelectedGraph;

	private List<PopupObserver> observersList = new ArrayList<>();

	public TspPopupController(){

	}

	@FXML
	public void initialize() {
		// Init in tab order
		initFreeInput();
		initFileLoader();
		initProblemLib();
	}

	private void initFreeInput(){
		this.bindButton(loadFreeInputBtn, evt -> {
			final String userInput = freeInputProblem.getText();
			final TspProblemReader reader = new TspProblemReader();
			try {
				temporarySelectedGraph = reader.readFromString(userInput);
				validGraphSelection();
			} catch (ParsingException e) {
				logError(e.getMessage());
				simpleErrorAlert("The input could not be parsed. We may not support your format, or the input comport errors (" + e.getUserCause() + ")");
				temporarySelectedGraph = null;
			}
		});
	}

	private void initFileLoader(){
		descLoadFileLabel.getChildren().addAll(
				new Text("You can download some famous TSP problem on the Heidelberg University website "),
				new ClassicHyperlink(HyperlinkRegister.HEIDELBERG_UNIVERSITY_TSPLIB_URL, "clicking here"),
				new Text(", Or you can learn about the available format (and more)"),
				new ClassicHyperlink(HyperlinkRegister.GITHUB_PROJECT_URL, "on the GitHub project"),
				new Text("(You can only import unzipped file for now)")
		);
		bindButton(openFileChooserBtn, evt -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File file = fileChooser.showOpenDialog(stage);
			if(file != null){
				pathToFileLabel.setText(file.getAbsolutePath());
			}
		});
		bindButton(loadOpenFileBtn, evt -> {
			final File selectedFile = new File(pathToFileLabel.getText());
			if(selectedFile.exists() && !selectedFile.isDirectory()) {
				try {
					final String content = new String(Files.readAllBytes(selectedFile.toPath()));
					final TspProblemReader reader = new TspProblemReader();
					try {
						temporarySelectedGraph = reader.readFromString(content);
						validGraphSelection();
					} catch (ParsingException e) {
						logError(e.getMessage());
						simpleErrorAlert("The file could not be parsed. We may not support your format, or the input comport errors (" + e.getUserCause() + ")");
						temporarySelectedGraph = null;
					}
				} catch (IOException e) {
					logError(e.getMessage());
				}
			}
		});
	}

	private void initProblemLib(){
		final ObservableList<TsplibListItem> list = FXCollections.observableArrayList();
		list.addAll(TspCommonLibrary.getAllCommonProblem().stream().map(TsplibListItem::fromTspProblem).collect(Collectors.toList()));
		listViewProblemLibrary.setItems(list);
		listViewProblemLibrary.setOnMouseClicked(event -> {
			TsplibListItem problemSelected = listViewProblemLibrary.getSelectionModel().getSelectedItem();
			if(problemSelected != null){
				temporarySelectedGraph = problemSelected.generateGraph();
			}
		});
		this.bindButton(loadProblemLibBtn, e -> validGraphSelection());

	}

	private void validGraphSelection(){
		if(temporarySelectedGraph != null){
			for(PopupObserver obs : this.observersList){
				obs.newGraphSelected(temporarySelectedGraph);
			}
			stage.close();
		} else {
			logError("The user hasn't selected a graph");
			simpleErrorAlert("There is no Graph currently selected ; please choose one or exit the window");
		}
	}

	private void simpleErrorAlert(final String msg){
		final Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error in Graph selection");
		alert.setHeaderText(msg);
		alert.setContentText("Reminder: You can still create your own Graph in the WebView");
		alert.showAndWait();
	}

	public void addPopupObserver(final PopupObserver o){
		this.observersList.add(o);
	}

}
