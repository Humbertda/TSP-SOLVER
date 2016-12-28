package com.humbertdany.sarl.tsp.ui.tsppopup;

import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.filereader.TspProblemReader;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspProblem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TsplibListItem implements IGraphGenerator {

	private Logger logger;

	private final String name;
	private final String desc;

	private TsplibListItem(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public String toString(){
		final StringBuilder sb = new StringBuilder();
		sb.append(name);
		return sb.toString();
	}


	public static TsplibListItem fromTspProblem(final TspProblem p){
		return new TsplibListItem(p.getName(), p.getDesc());
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

	@Override
	public TspGraph generateGraph() {
		final TspProblemReader reader = new TspProblemReader();
		TspGraph g;
		try {
			g = reader.readFromString(this.desc);
		} catch (ParsingException e) {
			g = new TspGraph();
			logError(e.getMessage());
		}
		return g;
	}
}
