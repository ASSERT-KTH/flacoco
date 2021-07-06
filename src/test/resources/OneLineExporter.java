import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class OneLineExporter implements fr.spoonlabs.flacoco.cli.export.FlacocoExporter {

	@Override
	public void export(Map<String, Double> results, OutputStreamWriter outputStream) throws IOException {
		for (Map.Entry<String, Double> entry : results.entrySet()) {
			outputStream.write(entry.getKey() + "," + entry.getValue());
		}
	}

	@Override
	public String extension() {
		return "custom";
	}
}
