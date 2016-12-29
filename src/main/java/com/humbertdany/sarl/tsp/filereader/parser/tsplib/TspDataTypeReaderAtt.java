package com.humbertdany.sarl.tsp.filereader.parser.tsplib;

import com.humbertdany.sarl.tsp.filereader.parser.Record;

public class TspDataTypeReaderAtt extends ATspDataTypeReader {

	@Override
	public Record read(String line) {
		// We cheat here and ignore the scaling factor, since it does not matter anywhere
		final String[] split = line.split(" ");
		return new Record(Double.parseDouble(split[1].trim()), Double.parseDouble(split[2].trim()));
	}

}
