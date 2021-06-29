package fr.spoonlabs.flacoco;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumRunner;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "FlacocoMain", mixinStandardHelpOptions = true, version = "0.0.1", description = "Flacoco: fault localization")
public class FlacocoMain implements Callable<Integer> {
	static boolean log = true;

	@Option(names = { "-w", "--workspace"}, description = "Path to the workspace directory of flacoco.", defaultValue = "./")
	String workspace;

	@Option(names = { "-p", "--projectpath" }, description = "Path to the project to analyze.")
	String projectPath = null;

	@Option(names = { "-f", "--formula" }, description = "Spectrum formula to use.", defaultValue = "OCHIAI")
	String spectrumFormula;

	@Option(names = { "-c", "--classpath" }, description = "Classpath of the project under analyzis.")
	String classpath;

	@Option(names = { "--junitClasspath" }, description = "Classpath to junit dependencies")
	String customJUnitClasspath;

	@Option(names = { "--jacocoClasspath" }, description = "Classpath to jacoco dependencies")
	String customJacocoClasspath;

	@Option(names = { "--mavenHome" }, description = "Path to maven home")
	String mavenHome;

	@Option(names = {
			"--coverTest" }, description = "Indicates if coverage must also cover the tests.", defaultValue = "false")
	boolean coverTest = false;

	@Option(names = {
			"--framework" }, description = "Test framework that the project under analysis uses.", defaultValue = "JUNIT4")
	String testFramework;

	public static void main(String[] args) throws Exception {

		int exitCode = new CommandLine(new FlacocoMain()).execute(args);
		System.out.println("exit code " + exitCode);

		if (exitCode != 1) {
			throw new IllegalArgumentException("Flacoco could finish well, see error output");
		}

	}

	@Override
	public Integer call() {

		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setWorkspace(new File(this.workspace).getAbsolutePath());
		config.setProjectPath(new File(this.projectPath).getAbsolutePath());

		if (this.classpath != null && !this.classpath.trim().isEmpty())
			config.setClasspath(classpath);
		if (this.customJUnitClasspath != null && !this.customJUnitClasspath.trim().isEmpty())
			config.setCustomJacocoClasspath(this.customJUnitClasspath);
		if (this.customJacocoClasspath != null && !this.customJacocoClasspath.trim().isEmpty())
			config.setCustomJacocoClasspath(this.customJacocoClasspath);
		if (this.mavenHome != null && !this.mavenHome.trim().isEmpty())
			config.setMavenHome(this.mavenHome);

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
