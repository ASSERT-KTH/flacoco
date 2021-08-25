import fr.spoonlabs.flacoco.api.result.FlacocoResult;
import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class OneLineExporter implements fr.spoonlabs.flacoco.cli.export.FlacocoExporter {

	@Override
	public void export(FlacocoResult result, OutputStreamWriter outputStream) throws IOException {
		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			outputStream.write(entry.getKey() + "," + entry.getValue().getScore());
		}
	}

	@Override
	public String extension() {
		return "custom";
	}
}
