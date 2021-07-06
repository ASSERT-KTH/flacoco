package fr.spoonlabs.flacoco.cli.export;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public interface FlacocoExporter {

	void export(Map<String, Double> results, OutputStreamWriter outputStream) throws IOException;

	String extension();

}
