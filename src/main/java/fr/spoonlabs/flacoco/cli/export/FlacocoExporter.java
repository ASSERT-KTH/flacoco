package fr.spoonlabs.flacoco.cli.export;

import fr.spoonlabs.flacoco.api.result.FlacocoResult;

import java.io.IOException;
import java.io.OutputStreamWriter;

public interface FlacocoExporter {

	void export(FlacocoResult result, OutputStreamWriter outputStream) throws IOException;

	String extension();

}
