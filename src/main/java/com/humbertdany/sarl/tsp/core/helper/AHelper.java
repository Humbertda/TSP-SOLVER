package com.humbertdany.sarl.tsp.core.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class AHelper {

	static private Logger logger;

	private void initLogger(){
		logger = LogManager.getLogger(this.getClass());
	}

	void log(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.info("\n" + o.toString());
	}

	void logError(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.error("\n" + o.toString());
	}

}
