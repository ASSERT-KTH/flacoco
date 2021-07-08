package fr.spoonlabs.flacoco.cli.export;

import fr.spoonlabs.flacoco.api.Suspiciousness;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public interface FlacocoExporter {

	void export(Map<String, Suspiciousness> results, OutputStreamWriter outputStream) throws IOException;

	String extension();

}
