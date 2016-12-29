package com.humbertdany.sarl.tsp.ui.icon;

import javafx.scene.image.Image;

final public class AppIconLib {

	public static final String ICON_ROOT_PATH = "/appIcon/";

	public static final String ICON_NAME_250 = "icon_250.png";

	private static Image img250 = null;
	public static Image get250Image(){
		if(img250 == null){
			img250 = new Image(ICON_ROOT_PATH + ICON_NAME_250);
		}
		return img250;
	}

}
