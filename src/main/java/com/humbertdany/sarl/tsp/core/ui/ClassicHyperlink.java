package com.humbertdany.sarl.tsp.core.ui;

import javafx.scene.control.Hyperlink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ClassicHyperlink extends Hyperlink {

	private Logger logger;

	public ClassicHyperlink(final String url){
		this(url, url);
	}

	public ClassicHyperlink(final String url, final String text) {
		super(text);
		this.setOnAction(evt -> {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (URISyntaxException | IOException e) {
				logError(e.getMessage());
			}
		});
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

}
