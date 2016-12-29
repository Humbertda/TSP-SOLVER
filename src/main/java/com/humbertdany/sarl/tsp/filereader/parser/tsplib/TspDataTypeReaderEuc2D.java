package com.humbertdany.sarl.tsp.filereader.parser.tsplib;

import com.humbertdany.sarl.tsp.filereader.parser.Record;

public class TspDataTypeReaderEuc2D extends ATspDataTypeReader {

	@Override
	public Record read(final String line) {
		final String[] split = line.split(" ");
		return new Record(Double.parseDouble(split[1].trim()), Double.parseDouble(split[2].trim()));
	}

}
