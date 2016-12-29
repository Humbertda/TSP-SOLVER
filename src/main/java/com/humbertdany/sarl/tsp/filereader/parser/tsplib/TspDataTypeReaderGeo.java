package com.humbertdany.sarl.tsp.filereader.parser.tsplib;

import com.humbertdany.sarl.tsp.filereader.parser.Record;

public class TspDataTypeReaderGeo extends ATspDataTypeReader {

	private String displayDataType = null;

	@Override
	protected void newParamsAdded(final String params){
		final String[] split = params.split(":");
		if(split[0] != null){
			final String paramsName = split[0].trim();
			if (split[1] != null) {
				final String paramsValue = split[1].trim();
				switch (paramsName.toUpperCase()){
					case "DISPLAY_DATA_TYPE":
						displayDataType = paramsValue;
						break;
				}
			}
		}
	}

	@Override
	public Record read(String line) {
		switch (displayDataType){
			case "COORD_DISPLAY":
				// We cheat here and ignore the scaling factor, since it does not matter anywhere
				final String[] split = line.split(" ");
				return new Record(Double.parseDouble(split[1].trim()), Double.parseDouble(split[2].trim()));
			default:
				return null;
		}
	}

}
