package com.humbertdany.utils.jspipe;

import javafx.scene.web.WebEngine;

abstract public class AJsApplication {

	private WebEngine webEngine;

	public AJsApplication(final WebEngine we){
		this.webEngine = we;
	}

	/**
	 * Open Firebug Lite in the WebView
	 * from the amazing post : http://stackoverflow.com/questions/9398879/html-javascript-debugging-in-javafx-webview
	 */
	final public void openFirebug(){
		webEngine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] "    +
				"&& document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : "     +
				"document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', "  +
				"'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4')" +
				";(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0])."        +
				"appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
	}

}
