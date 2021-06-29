package fr.spoonlabs.flacoco;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumRunner;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "FlacocoMain", mixinStandardHelpOptions = true, version = "0.0.1", description = "Flacoco: fault localization")
public class FlacocoMain implements Callable<Integer> {
	static boolean log = true;

	@Option(names = { "-p", "--projectpath" }, description = "Path to the project to analyze.")
	String projectPath = null;

	@Option(names = { "-f", "--formula" }, description = "Spectrum formula to use.", defaultValue = "OCHIAI")
	String spectrumFormula;

	@Option(names = { "-c", "--classpath" }, description = "Classpath of the project under analyzis.")
	String classpath;

	@Option(names = {
			"--coverTest" }, description = "Indicates if coverage must also cover the tests.", defaultValue = "false")
	boolean coverTest = false;

	@Option(names = {
			"--framework" }, description = "Test framework that the project under analysis uses.", defaultValue = "10")
	String testFramework;

	public static void main(String[] args) throws IOException {

		int exitCode = new CommandLine(new FlacocoMain()).execute(args);
		System.exit(exitCode);

	}

	@Override
	public Integer call() throws Exception {

		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File(this.projectPath).getAbsolutePath());

		if (this.classpath != null && !this.classpath.trim().isEmpty())
			config.setClasspath(classpath);

		config.setCoverTests(coverTest);

		SpectrumFormula spectrumFormulaSelected = SpectrumFormula.valueOf(this.spectrumFormula);

		config.setSpectrumFormula(spectrumFormulaSelected);

		FlacocoConfig.TestFramework testFrameworkSelected = FlacocoConfig.TestFramework.valueOf(testFramework);
		config.setTestFramework(testFrameworkSelected);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Double> susp = runner.run();

		for (String location : susp.keySet()) {
			System.out.println(location + " " + susp.get(location));
		}

		return 1;
	}

}
