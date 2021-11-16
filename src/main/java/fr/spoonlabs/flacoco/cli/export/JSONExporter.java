package fr.spoonlabs.flacoco.cli.export;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.spoonlabs.flacoco.api.result.FlacocoResult;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class JSONExporter implements FlacocoExporter {

	@Override
	public void export(FlacocoResult result, OutputStreamWriter outputStream) throws IOException {
		Gson gson = new Gson();
		Type gsonType = new TypeToken<List<JSONEntry>>() {}.getType();

		String gsonString = gson.toJson(
				result.getDefaultSuspiciousnessMap().entrySet().stream()
						.map(x -> new JSONEntry(
								x.getKey().getClassName(),
								x.getKey().getLineNumber(),
								x.getValue().getScore())
						).collect(Collectors.toList()),
				gsonType
		);

		outputStream.write(gsonString);
	}

	@Override
	public String extension() {
		return "json";
	}
}
