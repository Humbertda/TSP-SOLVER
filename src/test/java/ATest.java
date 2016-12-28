import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class ATest {

	static private Logger logger;

	static void newLine(){
		System.out.println("\n--\n");
	}

	static private void initLogger(){
		logger = LogManager.getLogger(ATest.class);
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

}
