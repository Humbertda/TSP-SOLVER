import com.fasterxml.jackson.databind.ObjectMapper;
import com.humbertdany.sarl.tsp.ui.mainui.CityEntry;

import java.io.IOException;

public class CityEntryMappingTest extends ATest {

	private static final String RETURN_FROM_JS = "{\"x\":303,\"y\":175}";

	public static void main(String[] args){
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final CityEntry cityEntry = mapper.readValue(RETURN_FROM_JS, CityEntry.class);
			log(RETURN_FROM_JS);
			log(cityEntry.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
