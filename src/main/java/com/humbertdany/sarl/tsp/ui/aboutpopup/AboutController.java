package com.humbertdany.sarl.tsp.ui.aboutpopup;

import com.humbertdany.sarl.tsp.core.ui.ClassicHyperlink;
import com.humbertdany.sarl.tsp.core.ui.JfxController;
import com.humbertdany.utils.HyperlinkRegister;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AboutController extends JfxController {

	@FXML
	public TextFlow aboutLabel;

	@FXML
	public void initialize() {
		aboutLabel.getChildren().addAll(
				new Text("This project was made for the IA54 course taught in the"),
				new ClassicHyperlink(HyperlinkRegister.UTBM_URL, "UTBM"),
				new Text(", you can check the sources and learn more about the project on the"),
				new ClassicHyperlink(HyperlinkRegister.GITHUB_PROJECT_URL, "GitHub")
		);
	}

}
