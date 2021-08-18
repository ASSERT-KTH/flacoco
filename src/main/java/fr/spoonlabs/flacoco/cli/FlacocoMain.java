package fr.spoonlabs.flacoco.cli;

import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.api.Suspiciousness;
import fr.spoonlabs.flacoco.cli.export.CSVExporter;
import fr.spoonlabs.flacoco.cli.export.FlacocoExporter;
import fr.spoonlabs.flacoco.cli.export.JSONExporter;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(name = "FlacocoMain", mixinStandardHelpOptions = true, version = "0.0.1", description = "Flacoco: fault localization")
public class FlacocoMain implements Callable<Integer> {

	@Option(names = {"-w", "--workspace"}, description = "Path to the workspace directory of flacoco.", defaultValue = "./")
	String workspace;

	@Option(names = {"-p", "--projectpath"}, description = "Path to the project to analyze.", defaultValue = "./")
	String projectPath;

	@Option(names = {"-f", "--formula"}, description = "Spectrum formula to use. Valid values: ${COMPLETION-CANDIDATES}", defaultValue = "OCHIAI")
	SpectrumFormula spectrumFormula;

	@Option(names = {"-c", "--classpath"}, description = "Classpath of the project under analyzis.")
	String classpath;

	@Option(names = {"--srcJavaDir"}, arity = "0..*", description = "Paths to the directories containing java source files. Defaults to {projectpath}/src/main/java")
	List<String> srcJavaDir;

	@Option(names = {"--srcTestDir"}, arity = "0..*", description = "Paths to the directories containing java test source files. Defaults to {projectpath}/src/test")
	List<String> srcTestDir;

	@Option(names = {"--binJavaDir"}, arity = "0..*", description = "Paths to the directories containing java class files. Defaults to {projectpath}/target/classes")
	List<String> binJavaDir;

	@Option(names = {"--binTestDir"}, arity = "0..*", description = "Paths to the directories containing java test class files. Defaults to {projectpath}/target/test-classes")
	List<String> binTestDir;

	@Option(names = {"--junitClasspath"}, description = "Classpath to junit dependencies.")
	String customJUnitClasspath;

	@Option(names = {"--jacocoClasspath"}, description = "Classpath to jacoco dependencies.")
	String customJacocoClasspath;

	@Option(names = {"--mavenHome"}, description = "Path to maven home.")
	String mavenHome;

	@Option(names = {"--coverTest"}, description = "Indicates if coverage must also cover the tests.", defaultValue = "false")
	boolean coverTest = false;

	@Option(names = {"--testRunnerVerbose"}, description = "Test-runner verbose mode.", defaultValue = "false")
	boolean testRunnerVerbose = false;

	@Option(names = {"--testRunnerTimeoutInMs"}, description = "Timeout for each test execution with test-runner. Must be greater than 0. Default value is 1000000", defaultValue = "1000000")
	int testRunnerTimeoutInMs = 1000000;

	@Option(names = {"--testRunnerJVMArgs"}, description = "JVM args for test-runner's test execution VMs.")
	String testRunnerJVMArgs = null;

	@Option(names = {"--threshold"}, description = "Threshold for suspiciousness score. Flacoco will only return suspicious results with score >= threshold. Results with a score of 0 are only included if the -includeZeros flag is set.", defaultValue = "0.0")
	double threshold = 0.0;

	@Option(names = {"--includeZeros"}, description = "Flag for including lines with a suspiciousness sore of 0.", defaultValue = "false")
	boolean includeZeros = false;

	@Option(names = {"--complianceLevel"}, description = "Compliance level for Spoon. Default value is 8", defaultValue = "8")
	int complianceLevel = 8;

	@Option(names = {"-o", "--output"},
			description = "Path to the output file. If no path is provided but the flag is, the result will be stored in flacoco_result.{extension}",
			arity = "0..1",
			fallbackValue = ""
	)
	String output;

	@CommandLine.ArgGroup(exclusive = true, multiplicity = "0..1")
	FormatOption formatOption = new FormatOption();

	static class FormatOption {
		// default value for formatOption
		public FormatOption() {
			this.format = Format.CSV;
		}

		public enum Format {
			CSV,
			JSON
		}

		@Option(names = {"--format"}, description = "Format of the output. Valid values: ${COMPLETION-CANDIDATES}", defaultValue = "CSV")
		Format format;

		@Option(names = {"--formatter"}, description = "Path to java file of a custom FlacocoExporter.")
		String customExporter;
	}

	@Option(names = {"--testDetectionStrategy"}, description = "Strategy for test detection stage. Defaults to CLASSLOADER. Valid values: ${COMPLETION-CANDIDATES}")
	FlacocoConfig.TestDetectionStrategy testDetectionStrategy = FlacocoConfig.TestDetectionStrategy.CLASSLOADER;

	@Option(names = {"--ignoredTests"}, description = "Tests to be ignored during test execution. Both qualified class and qualified method names are supported.")
	Set<String> ignoredTests = new HashSet<>();

	@Option(names = {"--jacocoIncludes"}, description = "Class patterns to be recorded in by jacoco")
	Set<String> jacocoIncludes = new HashSet<>();

	@Option(names = {"--jacocoExcludes"}, description = "Class patterns to be excluded by jacoco")
	Set<String> jacocoExcludes = new HashSet<>();

	@CommandLine.ArgGroup(exclusive = false, multiplicity = "0..1", heading = "Setting any of these options will result in test detection being bypassed.")
	Tests tests = new Tests();

	static class Tests {
		// default value for tests, empty lists mean no option was set
		public Tests() {
			this.jUnit4Tests = new HashSet<>();
			this.jUnit5Tests = new HashSet<>();
		}

		@Option(names = {"--junit4tests"}, description = "JUnit4 or JUnit3 tests to be ran.")
		Set<String> jUnit4Tests;

		@Option(names = {"--junit5tests"}, description = "JUnit5 tests to be ran.")
		Set<String> jUnit5Tests;
	}

	@Option(names = "-v", scope = CommandLine.ScopeType.INHERIT, description = "Verbose mode.")
	public void setVerbose(boolean[] verbose) {
		// For now we have these two levels, but in the future we might want to add more
		Logger.getRootLogger().setLevel(verbose.length > 0 ? Level.DEBUG : Level.INFO);
	}

	public static void main(String[] args) {
		int exitCode = new CommandLine(new FlacocoMain()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() {
		setupFlacocoConfig();

		Flacoco flacoco = new Flacoco();
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		exportResults(susp);

		return 0;
	}

	private void setupFlacocoConfig() {
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setWorkspace(new File(this.workspace).getAbsolutePath());
		config.setProjectPath(new File(this.projectPath).getAbsolutePath());

		if (this.srcJavaDir != null && !this.srcJavaDir.isEmpty())
			config.setSrcJavaDir(this.srcJavaDir);
		if (this.srcTestDir != null && !this.srcTestDir.isEmpty())
			config.setSrcTestDir(this.srcTestDir);
		if (this.binJavaDir != null && !this.binJavaDir.isEmpty())
			config.setBinJavaDir(this.binJavaDir);
		if (this.binTestDir != null && !this.binTestDir.isEmpty())
			config.setBinTestDir(this.binTestDir);
		if (this.classpath != null && !this.classpath.trim().isEmpty())
			config.setClasspath(classpath);
		if (this.customJUnitClasspath != null && !this.customJUnitClasspath.trim().isEmpty())
			config.setCustomJUnitClasspath(this.customJUnitClasspath);
		if (this.customJacocoClasspath != null && !this.customJacocoClasspath.trim().isEmpty())
			config.setCustomJacocoClasspath(this.customJacocoClasspath);
		if (this.mavenHome != null && !this.mavenHome.trim().isEmpty())
			config.setMavenHome(this.mavenHome);

		config.setCoverTests(coverTest);
		config.setTestRunnerVerbose(testRunnerVerbose);
		if (this.testRunnerTimeoutInMs > 0)
			config.setTestRunnerTimeoutInMs(this.testRunnerTimeoutInMs);
		config.setTestRunnerTimeoutInMs(testRunnerTimeoutInMs);
		if (this.testRunnerJVMArgs != null && !this.testRunnerJVMArgs.trim().isEmpty())
			config.setTestRunnerJVMArgs(testRunnerJVMArgs);
		config.setThreshold(threshold);
		config.setIncludeZeros(includeZeros);
		config.setComplianceLevel(complianceLevel);

		config.setTestDetectionStrategy(this.testDetectionStrategy);
		config.setIgnoredTests(this.ignoredTests);
		config.setjUnit4Tests(this.tests.jUnit4Tests);
		config.setjUnit5Tests(this.tests.jUnit5Tests);
		config.setJacocoIncludes(this.jacocoIncludes);
		config.setJacocoExcludes(this.jacocoExcludes);

		config.setSpectrumFormula(this.spectrumFormula);
	}

	private void exportResults(Map<String, Suspiciousness> results) {
		try {
			FlacocoExporter exporter = getExporter();
			OutputStreamWriter outputStreamWriter = getOutputStreamWriter(exporter);
			exporter.export(results, outputStreamWriter);
			outputStreamWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private OutputStreamWriter getOutputStreamWriter(FlacocoExporter exporter) throws IOException {
		if (this.output == null) {
			return new OutputStreamWriter(System.out);
		} else if (this.output.isEmpty()) {
			File file = new File("flacoco_results." + exporter.extension());
			if (!file.exists()) {
				file.createNewFile();
			}
			return new OutputStreamWriter(new FileOutputStream(file));
		} else {
			File file = new File(this.output);
			if (!file.exists()) {
				file.createNewFile();
			}
			return new OutputStreamWriter(new FileOutputStream(file));
		}
	}

	private FlacocoExporter getExporter() {
		if (this.formatOption.customExporter == null) {
			switch (this.formatOption.format) {
				case CSV:
					return new CSVExporter();
				case JSON:
					return new JSONExporter();
				default:
					// should never happen as the argument is parsed by picocli
					return null;
			}
		} else {
			Launcher launcher = new Launcher();
			launcher.addInputResource(this.formatOption.customExporter);
			CtClass<FlacocoExporter> exporterClass = (CtClass<FlacocoExporter>) launcher.buildModel().getAllTypes()
					.stream().findFirst().get();

			return exporterClass.newInstance();
		}
	}
}
