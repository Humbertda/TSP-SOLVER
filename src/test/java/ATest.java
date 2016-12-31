import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;

abstract class ATest {

	static private Logger logger;

	static private void initLogger(){
		logger = LogManager.getLogger(ATest.class);
	}

	static public void waitForRunLater() throws InterruptedException {
	    Semaphore semaphore = new Semaphore(0);
	    Platform.runLater(() -> semaphore.release());
	    semaphore.acquire();
	}

	static void log(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.info("\n" + o.toString());
	}

	static  void logError(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.error("\n" + o.toString());
	}

	static void newLine(){
		System.out.println("\n--\n");
	}

}
