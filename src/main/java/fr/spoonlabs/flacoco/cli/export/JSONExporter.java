package fr.spoonlabs.flacoco.cli.export;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.spoonlabs.flacoco.api.Suspiciousness;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONExporter implements FlacocoExporter {

	@Override
	public void export(Map<String, Suspiciousness> results, OutputStreamWriter outputStream) throws IOException {
		Gson gson = new Gson();
		Type gsonType = new TypeToken<HashMap>() {}.getType();

		String gsonString = gson.toJson(
				results.entrySet().stream()
						.collect(Collectors.toMap(
								Map.Entry::getKey,
								e -> e.getValue().getScore(),
								(e1, e2) -> e1,
								LinkedHashMap::new
						)),
				gsonType
		);

		outputStream.write(gsonString);
	}

	@Override
	public String extension() {
		return "json";
	}
}
