package fr.spoonlabs.flacoco.core.config;

import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;

import java.io.File;

/**
 * Config manager for Flacoco.
 *
 * @author andre15silva
 */
public class FlacocoConfig {

	public enum FaultLocalizationFamily {
		SPECTRUM_BASED,
	}

	public enum TestFramework {
		JUNIT4,
		JUNIT5
	}

	private static FlacocoConfig instance;

	private String workspace;
	private String projectPath;
	private String classpath;
	private String customJUnitClasspath;
	private String customJacocoClasspath;
	private String mavenHome;
	private TestFramework testFramework;
	private boolean coverTests;
	private boolean testRunnerVerbose;
	private int testRunnerTimeoutInMs;
	private String testRunnerJVMArgs;

	private FaultLocalizationFamily family;
	//------Options for spectrum-based fault localization------
	private SpectrumFormula spectrumFormula;

	private FlacocoConfig() {
		initDefaults();
	}

	public static FlacocoConfig getInstance() {
		if (instance == null) {
			instance = new FlacocoConfig();
		}
		return instance;
	}

	private void initDefaults() {
		this.workspace = new File("./").getAbsolutePath();
		this.projectPath = new File("./").getAbsolutePath();
		this.classpath = new File("./").getAbsolutePath();
		this.customJUnitClasspath = null;
		this.customJacocoClasspath = null;
		this.mavenHome = System.getProperty("user.home") + "/.m2/repository/";
		this.testFramework = TestFramework.JUNIT4;
		this.coverTests = false;
		this.testRunnerVerbose = false;
		this.testRunnerTimeoutInMs = 10000;
		this.testRunnerJVMArgs = null;

		this.family = FaultLocalizationFamily.SPECTRUM_BASED;
		this.spectrumFormula = SpectrumFormula.OCHIAI;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public String getCustomJUnitClasspath() {
		return customJUnitClasspath;
	}

	public void setCustomJUnitClasspath(String customJUnitClasspath) {
		this.customJUnitClasspath = customJUnitClasspath;
	}

	public String getCustomJacocoClasspath() {
		return customJacocoClasspath;
	}

	public void setCustomJacocoClasspath(String customJacocoClasspath) {
		this.customJacocoClasspath = customJacocoClasspath;
	}

	public String getMavenHome() {
		return mavenHome;
	}

	public void setMavenHome(String mavenHome) {
		this.mavenHome = mavenHome;
	}

	public TestFramework getTestFramework() {
		return testFramework;
	}

	public void setTestFramework(TestFramework testFramework) {
		this.testFramework = testFramework;
	}

	public boolean isCoverTests() {
		return coverTests;
	}

	public void setCoverTests(boolean coverTests) {
		this.coverTests = coverTests;
	}

	public boolean isTestRunnerVerbose() {
		return testRunnerVerbose;
	}

	public void setTestRunnerVerbose(boolean testRunnerVerbose) {
		this.testRunnerVerbose = testRunnerVerbose;
	}

	public int getTestRunnerTimeoutInMs() {
		return testRunnerTimeoutInMs;
	}

	public void setTestRunnerTimeoutInMs(int testRunnerTimeoutInMs) {
		this.testRunnerTimeoutInMs = testRunnerTimeoutInMs;
	}

	public String getTestRunnerJVMArgs() {
		return testRunnerJVMArgs;
	}

	public void setTestRunnerJVMArgs(String testRunnerJVMArgs) {
		this.testRunnerJVMArgs = testRunnerJVMArgs;
	}

	public FaultLocalizationFamily getFamily() {
		return family;
	}

	public void setFamily(FaultLocalizationFamily family) {
		this.family = family;
	}

	public SpectrumFormula getSpectrumFormula() {
		return spectrumFormula;
	}

	public void setSpectrumFormula(SpectrumFormula spectrumFormula) {
		this.spectrumFormula = spectrumFormula;
	}

	@Override
	public String toString() {
		return "FlacocoConfig{" +
				"workspace='" + workspace + '\'' +
				", projectPath='" + projectPath + '\'' +
				", classpath='" + classpath + '\'' +
				", customJUnitClasspath='" + customJUnitClasspath + '\'' +
				", customJacocoClasspath='" + customJacocoClasspath + '\'' +
				", mavenHome='" + mavenHome + '\'' +
				", testFramework=" + testFramework +
				", coverTests=" + coverTests +
				", testRunnerVerbose=" + testRunnerVerbose +
				", testRunnerTimeoutInMs=" + testRunnerTimeoutInMs +
				", testRunnerJVMArgs='" + testRunnerJVMArgs + '\'' +
				", family=" + family +
				", spectrumFormula=" + spectrumFormula +
				'}';
	}

	// For test purposes only
	public static void deleteInstance() {
		instance = null;
	}
}
