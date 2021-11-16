package fr.spoonlabs.flacoco.cli.export;

import fr.spoonlabs.flacoco.api.result.FlacocoResult;
import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class CSVExporter implements FlacocoExporter {

	private static final CsvPreference csvPreference = new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).build();

	@Override
	public void export(FlacocoResult result, OutputStreamWriter outputStream) throws IOException {
		// TODO: Using a CsvListWriter because CsvMapWriter had some issues. Ideally, we could use CsvMapWriter and reduce the complexity here
		CsvListWriter writer = new CsvListWriter(outputStream, csvPreference);
		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			writer.write(entry.getKey().getClassName(), entry.getKey().getLineNumber(), entry.getValue().getScore());
		}
		writer.close();
	}

	@Override
	public String extension() {
		return "csv";
	}
}
