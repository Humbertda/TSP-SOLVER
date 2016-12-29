package com.humbertdany.sarl.tsp.filereader.parser.tsplib;

import com.humbertdany.sarl.tsp.filereader.parser.Record;

import java.util.ArrayList;
import java.util.List;

abstract public class ATspDataTypeReader {

	final private List<String> unknownParams = new ArrayList<>();

	protected void newParamsAdded(final String params){
		// Nothing to do in this case
		// The extend class could override the function
	}

	abstract public Record read(final String line);

	final public List<String> getUnknownParams() {
		return unknownParams;
	}

	final public void addUnknownParams(String params){
		this.unknownParams.add(params);
		newParamsAdded(params);
	}

}
