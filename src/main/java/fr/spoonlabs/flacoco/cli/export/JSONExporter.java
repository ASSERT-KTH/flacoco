package fr.spoonlabs.flacoco.cli.export;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JSONExporter implements FlacocoExporter {

	@Override
	public void export(Map<String, Double> results, OutputStreamWriter outputStream) throws IOException {
		Gson gson = new Gson();
		Type gsonType = new TypeToken<HashMap>(){}.getType();

		String gsonString = gson.toJson(results, gsonType);

		outputStream.write(gsonString);
	}
}
